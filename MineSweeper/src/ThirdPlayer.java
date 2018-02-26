import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;


public class ThirdPlayer extends Player {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Player player = new ThirdPlayer();
		MineSweeper mineSweeper = new MineSweeper();
		mineSweeper.start(player);

	}
	
	/**
	 * 爆弾がある場所にフラグを立てる
	 */
	boolean[][] flag;
	 

	
	@Override
	protected void start() {
		flag = new boolean[getWidth()][getHeight()];
		double[][] prob =new double[getWidth()][getHeight()];
		for(int y = 0;y < getHeight();y++){
			for(int x = 0; x < getWidth(); x++){
				flag[x][y] = false;
				prob[x][y] = 1 / 6;
			}
		}
		LOOP:while(!isClear() && !isGameOver()){
			
			//まず，フラグを立てる
			
			FLAG:for(int y = 0; y < getHeight(); y++){
				for(int x = 0; x < getWidth(); x++){
					boolean f = isFlag(x, y);
					flag[x][y] = f;
				}
			}				
		//確実にセーフのマスを開ける
		OPEN1:for(int y = 0;y < getHeight();y++){
		    for(int x = 0;x < getWidth();x++){
			    int fn = flagNum(x,y);
			    int bombnum =getCell(x,y);
			    if(bombnum <= 0){
			    	continue;
			    }
			    if(fn == bombnum){
			    	for(int dy = -1; dy <= 1; dy++){
						for(int dx = -1; dx <= 1; dx++){
							boolean out = isOut(x+dx,y+dy);
							if(out == false){
								continue;
							}
							if((dx == 0 && dy == 0) == true){
								continue;
							}
							int emp = getCell(x+dx,y+dy);
							if(emp < 0){
								if(flag[x+dx][y+dy] == false){
									open(x+dx,y+dy);
								}
								
							}
						}
			    	}
			    }
		    }
	    }
	     OPEN2:for(int y = 0; y < getHeight(); y++){ 
			for(int x = 0; x < getWidth(); x++){
			    if(flag[x][y] == true){
						continue;//continueの意味はこの後の作業を行わず上のforに戻ってxを１増やして続ける
				}
						
				int bombNum = getCell(x, y);
				if(bombNum < 0){
					prob[x][y] = getProb(x,y);
					double min = minProb();
					if(prob[x][y] == min){
						open(x,y);
						break OPEN2;//1マスあけたらまたフラグをたてるところからするためにOPENの外に出る
					}
				}
			}			    
		}
		   
			showFlag();
		}				
	}

	/**
	 * フラグがどこに立っているかをコンソールに表示する
	 */
	void showFlag() {
		System.out.println("======================");
		FLAG:for(int y = 0; y < getHeight(); y++){
			for(int x = 0; x < getWidth(); x++){
				if(flag[x][y] == true){
					System.out.print("x");
				}
				else if(getCell(x, y) >= 0){
					System.out.print(getCell(x, y));
				}
				else{
					System.out.print(".");
				}
			}
			System.out.println();
		}
		
	}
	
	/**
	 * 指定位置にフラグを立てるべきかどうかを返す
	 * 
	 * 1110
	 * 1.10
	 * 1110
	 * 0000
	 * で，x=1,y=1のとき，
	 * getCell(x+1, y+1)=1で，その周りに開いていないセルは一つだけなので
	 * 開いていないCell(x,y)は必ず爆弾となる．
	 * したがって，そこにはフラグを立てることになる．
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isFlag(int x, int y) {
		int bombNum = getCell(x, y);
		int num = 0;
		if(bombNum < 0){
			for(int dy = -1; dy <= 1; dy++){
				for(int dx = -1; dx <= 1; dx++){
					if((dx == 0 && dy == 0) == true){
						continue;//この後の作業は行わず上に戻る
					}
					boolean out = isOut(x+dx,y+dy);
					if(out == false){
						continue;
					}
					int n = getUnOpenCellNum(x+dx, y+dy);
					//まだ開いていないセルの数が，そのセルの周りにある爆弾の数と同じならばそこに爆弾がある．
					if(n == getCell(x+dx, y+dy)){
						num = 1;
					}
				}
			}
		}
        if(num == 1){
        	return true;
        }else{
        	return false;
        }
        
	}

	/**
	 * 指定したセルの周りで開いていないセルの数を返す
	 * @param i
	 * @param j
	 * @return
	 */
	int getUnOpenCellNum(int x, int y) {
		int unOpen = 0;
		for(int dy = -1; dy <= 1; dy++){
			for(int dx = -1; dx <= 1; dx++){
				if(isOut(x+dx,y+dy) == true){
				     if(getCell(x+dx,y+dy) < 0){
					 unOpen++;//x,yの周りに開いていないセルがあればunOpenを1増やす
				     }
				}
			}
		}		
		return unOpen;
	}

	/**
	 * 外に飛び出していないか
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isOut(int x, int y) {
		if(0<=x && x<getWidth() && 0<=y && y<getHeight() == true){
			return true;//x,yが範囲外ならfalseを返す
		}else{
		    return false;
		}
	}

	int flagNum(int x,int y){
		int num2 = 0;
		for(int dy = -1; dy <= 1; dy++){
			for(int dx = -1; dx <= 1; dx++){
				boolean out = isOut(x+dx,y+dy);
				if(out == false){
					continue;
				}
				if((dx == 0 && dy == 0) == true){
					continue;
				}
				if(flag[x+dx][y+dy] == true){
					num2 = num2 +1;
				}
			}
	
		}
        return num2;
	}
	
	double getProb(int x,int y){
		double prob1,cell,unopen;
		double prob2 = -1.0;
		for(int dy = -1; dy <= 1; dy++){
			for(int dx = -1; dx <= 1; dx++){
				if(isOut(x+dx,y+dy) == false){
					continue;
				}
				if((dx ==0 && dy == 0) == true){
					continue;
				}
				cell = getCell(x+dx,y+dy);
				unopen = getUnOpenCellNum(x+dx,y+dy);
				prob1 = cell / unopen;
				if(prob1 > prob2){
					prob2 = prob1;
				}
			 }
	    }
		if(prob2 < 0){
			return 1 / 6;
		}else{
		return prob2;
		}
	}
	
	double minProb(){
		double min1;
		double min2 = 1;
		for(int y = 0;y < getHeight();y++){
			for(int x = 0;x < getWidth();x++){
				int cell2 = getCell(x,y);
				if(cell2 < 0){
				   min1 = getProb(x,y);
				   if(min2 > min1){
					   min2 = min1;
				   }
			    }
			}	
		}
		return min2;
	}

}
