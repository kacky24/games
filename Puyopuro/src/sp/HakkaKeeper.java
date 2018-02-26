package sp;

import sp.ConnectionCounter;
import sp.RensaCounter;
import sp.RensaPredictor;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Board;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoNumber;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;

public class HakkaKeeper {

	Field field;
	Puyo puyo;

	PuyoDirection Dir;
	int ColumnN;

	int[][] rensaNum;// 1つめは左からの行数、二つ目はぷよの向き（上から時計回り）

	// コンストラクタ
	public HakkaKeeper(Field field, Puyo puyo) {
		this.field = field;
		this.puyo = puyo;
		Dir = null;
		ColumnN = 0;
	}
	
	public int returnDir(){
		int dirNum=exchangeDirInt(Dir);
		return dirNum;
		
	}
	
	public int returnCol(){
		return ColumnN;
	}
	
	public int getMaxRensaNum() {
		int maxRensaNum = 0;
		int dirNum = 0;

		rensaNum=getRensaNum();
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {
				if (!isEnable(dir, i)) {
					continue;
				}
				
				dirNum=exchangeDirInt(dir);
				
				if (maxRensaNum < rensaNum[i][dirNum]) {
					maxRensaNum = rensaNum[i][dirNum];
					Dir=dir;
					ColumnN=i;
				}

			}
		}

		return maxRensaNum;
	}
	
	public int[][] getRensaNum() {

		if (rensaNum == null) {

			rensaNum = new int[6][4];

			PuyoDirection dir = null;

			for (int x = 0; x < field.getWidth(); x++) {
				for (int y = 0; y < 4; y++) {

					dir=exchangeIntDir(y);

					puyo.setDirection(dir);
					
					if (!isEnable(dir, x)) {
						rensaNum[x][y]=-1;
						continue;
					}
				
					Field nextField = field.getNextField(puyo, x);

					if (nextField == null) {
						return null;
					}

					int redRensaNum = 0;
					int blueRensaNum = 0;
					int greenRensaNum = 0;
					int yellowRensaNum = 0;
					int purpleRensaNum = 0;

					redRensaNum = (getColorPuyoNum(PuyoType.RED_PUYO, field)
							+ getFallColorPuyoNum(PuyoType.RED_PUYO, puyo) - getColorPuyoNum(
							PuyoType.RED_PUYO, nextField)) / 4;

					blueRensaNum = (getColorPuyoNum(PuyoType.BLUE_PUYO, field)
							+ getFallColorPuyoNum(PuyoType.BLUE_PUYO, puyo) - getColorPuyoNum(
							PuyoType.BLUE_PUYO, nextField)) / 4;

					greenRensaNum = (getColorPuyoNum(PuyoType.GREEN_PUYO, field)
							+ getFallColorPuyoNum(PuyoType.GREEN_PUYO, puyo) - getColorPuyoNum(
							PuyoType.GREEN_PUYO, nextField)) / 4;

					yellowRensaNum = (getColorPuyoNum(PuyoType.YELLOW_PUYO,
							field)
							+ getFallColorPuyoNum(PuyoType.YELLOW_PUYO, puyo) - getColorPuyoNum(
							PuyoType.YELLOW_PUYO, nextField)) / 4;

					purpleRensaNum = (getColorPuyoNum(PuyoType.PURPLE_PUYO,
							field)
							+ getFallColorPuyoNum(PuyoType.PURPLE_PUYO, puyo) - getColorPuyoNum(
							PuyoType.PURPLE_PUYO, nextField)) / 4;

					rensaNum[x][y] = redRensaNum + blueRensaNum + greenRensaNum
							+ yellowRensaNum + purpleRensaNum;

					redRensaNum = 0;
					blueRensaNum = 0;
					greenRensaNum = 0;
					yellowRensaNum = 0;
					purpleRensaNum = 0;
				}
			}
		}

		return rensaNum;
	}


	/**
	 * フィールド全体の、ある色のぷよの総数を返す
	 * 
	 * @param puyoType
	 * @param field
	 * @return
	 */
	public int getColorPuyoNum(PuyoType puyoType, Field field) {
		int colorPuyoNum = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			for (int j = 0; j < field.getHeight(); j++) {
				if (field.getPuyoType(i, j) == null) {
					continue;
				}
				if (field.getPuyoType(i, j) == puyoType) {
					colorPuyoNum++;
				}
			}
		}

		return colorPuyoNum;
	}

	/**
	 * 落ちてくるぷよの中の、ある色の数を数える
	 * 
	 * @param puyoType
	 * @param puyo
	 * @return
	 */
	public int getFallColorPuyoNum(PuyoType puyoType, Puyo puyo) {
		int fallColorPuyoNum = 0;
		if (puyo.getPuyoType(PuyoNumber.FIRST) == puyoType) {
			fallColorPuyoNum++;
		}
		if (puyo.getPuyoType(PuyoNumber.SECOND) == puyoType) {
			fallColorPuyoNum++;
		}

		return fallColorPuyoNum;
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
	
	public boolean isEnable(PuyoDirection dir, int i) {
		

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
