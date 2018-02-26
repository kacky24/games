package sp;

import sp.RensaCounter;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Board;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoNumber;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;

/**
 * 次やその次の連鎖数を考えて現在のフィールドでの置き方を決める
 * @author kakky
 *
 */
public class RensaPredictor {

	Field field;
	Puyo puyo;
	Puyo nextPuyo;
	Puyo nextNextPuyo;
	PuyoDirection nextNextMaxDir;
	int nextNextMaxColumn;
	PuyoDirection nextMaxDir;
	int nextMaxColumn;
	
    int[][] nextRensaNum;
	int[][] nextNextRensaNum;

	public RensaPredictor(Field field, Puyo puyo, Puyo nextPuyo,
			Puyo nextNextPuyo) {
		this.field = field;
		this.puyo = puyo;
		this.nextPuyo = nextPuyo;
		this.nextNextPuyo = nextNextPuyo;
		nextNextMaxDir=null;
		nextNextMaxColumn=0;
		nextMaxDir=null;
		nextMaxColumn=0;
		nextRensaNum=null;
		nextNextRensaNum=null;
	}
	
	public int returnNDir(){
		int dirNum=exchangeDirInt(nextMaxDir);
		return dirNum;
		
	}
	
	public int returnNCol(){
		return nextMaxColumn;
	}
	
	public int returnNNDir(){
		int dirNum=exchangeDirInt(nextNextMaxDir);
		return dirNum;
		
	}
	
	public int returnNNCol(){
		return nextNextMaxColumn;
	}
	
	
	
	public int getNextRensaNum(){
		int dirNum;
		int nextMaxRensaNum=0;
		
		if(nextRensaNum==null){
			//現在のfield
			for(int i=0;i<field.getWidth();i++){
				for(PuyoDirection dir1:PuyoDirection.values()){
					if(!isEnable(field,dir1,i)){
						continue;
					}
					
					puyo.setDirection(dir1);
					
					Field nextField=field.getNextField(puyo,i);
					if(nextField==null){
						return 0;
					}
					
					//nextField
					RensaCounter rns = new RensaCounter(nextField,nextPuyo);
					
					int n=rns.getMaxRensaNum();
					if(n>nextMaxRensaNum){
						nextMaxRensaNum=n;
					}
					
				}
			}
		}
		
		return nextMaxRensaNum;
		
		
	}

	

	/**
	 * 次の次のフィールドを読み、そこで連鎖が最大になるような現在のフィールドでの置き方を格納する。
	 * @return
	 */
	public int getNextNextRensaNum() {

		int dirNum;
		int nextNextMaxRensaNum=0;
		
		if (nextNextRensaNum == null) {


			// 現在のフィールド
			for (int i = 0; i < field.getWidth(); i++) {
				for (PuyoDirection dir1 : PuyoDirection.values()) {
					if (!isEnable(field,dir1, i)) {
						continue;
					}
					
					puyo.setDirection(dir1);
					
					Field nextField = field.getNextField(puyo, i);

					if (nextField == null) {
						continue;
					}

					// nextField
					for (int j = 0; j < field.getWidth(); j++) {
						for (PuyoDirection dir2 : PuyoDirection.values()) {
							if (!isEnable(nextField,dir2, j)) {
								continue;
							}
							
							nextPuyo.setDirection(dir2);
							Field nextNextField = nextField.getNextField(
									nextPuyo, j);

							if (nextNextField == null) {
								continue;
							}

							RensaCounter rns = new RensaCounter(nextNextField,
									nextNextPuyo);
							nextNextRensaNum = rns.getRensaNum();
							
							for(int x=0;x<6;x++){
								for(int y=0;y<4;y++){
									if(nextNextRensaNum[x][y]>nextNextMaxRensaNum){
										nextNextMaxRensaNum=nextNextRensaNum[x][y];
										nextNextMaxDir=dir1;
										nextNextMaxColumn=i;
									}
								}
							}

						}
					}
				}
			}
			
		}
		
		return nextNextMaxRensaNum;
		
	}
	
	public int exchangeDirInt(PuyoDirection dir) {
		if (dir == PuyoDirection.UP) {
			return 0;
		}
		if (dir == PuyoDirection.RIGHT) {
			return 1;
		}
		if (dir == PuyoDirection.DOWN) {
			return 2;
		}
		if (dir == PuyoDirection.LEFT) {
			return 3;
		}
		return 0;
	}

	public PuyoDirection exchangeIntDir(int columnNum){
		if(columnNum==0){
			return PuyoDirection.UP;
		}
		if(columnNum==1){
			return PuyoDirection.RIGHT;
		}
		if(columnNum==2){
			return PuyoDirection.DOWN;
		}
		if(columnNum==3){
			return PuyoDirection.LEFT;
		}
		return PuyoDirection.UP;
	}
	
	
	/**
	 * 配置可能か，あるいは死んでしなわないかをチェックする
	 * 
	 * @param i
	 * @param dir
	 * @return 配置不能または死んでしまう場合はfalse
	 */
	private boolean isEnable(Field field,PuyoDirection dir, int i) {
		

		// 配置不能ならfalse
		if (!field.isEnable(dir, i)) {
			return false;
		}

		if (dir == PuyoDirection.DOWN || dir == PuyoDirection.UP) {
			if (field.getTop(i) >= field.getDeadLine() - 2) {
				return false;
			}
		} else if (dir == PuyoDirection.RIGHT) {
			if (field.getTop(i) >= field.getDeadLine() - 1
					|| field.getTop(i + 1) >= field.getDeadLine() - 1) {
				return false;
			}
		} else if (dir == PuyoDirection.LEFT) {
			if (field.getTop(i) >= field.getDeadLine() - 1
					|| field.getTop(i - 1) >= field.getDeadLine() - 1) {
				return false;
			}
		}
		return true;
	}


}
