

import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;


public class FlagPlayer extends Player {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Player player = new FlagPlayer();
		MineSweeper mineSweeper = new MineSweeper();
		mineSweeper.start(player);

	}
	
	/**
	 * 爆弾がある場所にフラグを立てる
	 */
	boolean[][] flag;

	/**
	 * マインスイーパーを実行するメソッド<br>
	 * getWidth()で幅を取得<br>
	 * getHeight()で高さを取得<br>
	 * getCell(x, y)でx, y座標の周りにある爆弾の数を獲得可能．まだ開けていない場合は-1<br>
	 * open(x, y)で指定位置をオープンする
	 * <br>
	 */
	@Override
	protected void start() {
		flag = new boolean[getWidth()][getHeight()];
		for(int y = 0;y < getHeight();y++){
			for(int x = 0; x < getWidth(); x++){
				flag[x][y] = false;
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
		    OPEN:for(int y = 0; y < getHeight(); y++){ 
			    for(int x = 0; x < getWidth(); x++){
					if(flag[x][y] == true){
						continue;//continueの意味はこの後の作業を行わず上のforに戻ってxを１増やして続ける
					}
						
					int bombNum = getCell(x, y);
					if(bombNum < 0){
						open(x, y);
						break OPEN;//1マスあけたらまたフラグをたてるところからするためにOPENの外に出る
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
					if(isOut(x+dx,y+dy) == false){
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
		if((0<=x && x<getWidth() && 0<=y && y<getHeight()) == true){
			return true;//x,yが範囲外ならfalseを返す
		}else{
		    return false;
		}
	}



}
