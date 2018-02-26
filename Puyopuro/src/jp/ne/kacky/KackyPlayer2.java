package jp.ne.ezweb.kicker.kacky;

import SamplePlayer.ScoringPlayer;
import sp.ConnectionCounter;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Board;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoNumber;

public class KackyPlayer2 extends AbstractPlayer {

	public KackyPlayer2(String KackyPlayer2) {
		super(KackyPlayer2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action doMyTurn() {
		// TODO Auto-generated method stub
		/**
		 * 現在のフィールドの状況
		 */
		Field field = getMyBoard().getField();

		Puyo puyo = getMyBoard().getCurrentPuyo();

		Action action = null;

		int maxScore = 0;


		MAIN: for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {
				Field nextField = field.getNextField(puyo, i);

				if (!isEnable(dir, i)) {
					continue;
				}

				int rensaNum = getRensaNum(i, dir, field, puyo);
				System.out.print(i);
				System.out.println(rensaNum);
				if (rensaNum > 1) {
					action = new Action(dir, i);

					System.out.println("Rensa");
					break MAIN;
				}

				if (nextField != null) {//なぜかi=0のときはnullになっている

					int oneConnectNum = getConnectNum(1, nextField);
					int twoConnectNum = getConnectNum(2, nextField);
					int threeConnectNum = getConnectNum(3, nextField);
					int score = 0;

					score = threeConnectNum * 4 + twoConnectNum * 3
							+ oneConnectNum;
					
					if (i == 5) {
						score = score / 2;
					}
					
					if (score > maxScore ) {
						System.out.println("なんで"+i);
						action = new Action(dir, i);
						maxScore = score;
					}
				}
			}
		}

		if (action == null) {
			System.out.println("Default");
			action = getDefaultAction();
		}

		return action;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputResult() {
		// TODO Auto-generated method stub

	}

	/**
	 * 同じ色のぷよがn個つながった塊が、フィールド全体に何個あるか数える。
	 * 
	 * @param n
	 *            :同色が何個つながっているものを探すか
	 * @param field
	 * @return
	 */
	private int getConnectNum(int n, Field field) {
		ConnectionCounter cnt = new ConnectionCounter(field);
		int[][] countField = cnt.getConnectedPuyoNum();

		int connectNum = 0;
		for (int i = 0; i < countField.length; i++) {
			for (int j = 0; j < countField[i].length; j++) {
				if (countField[i][j] == n) {
					connectNum++;
				}
			}
		}
		connectNum = connectNum / n;

		return connectNum;
	}

	/**
	 * ある列にある向きでぷよを落としたら何連鎖するか調べる。
	 * 
	 * @param x
	 * @param dir
	 * @param field
	 * @param puyo
	 * @return
	 */
	private int getRensaNum(int x, PuyoDirection dir, Field field, Puyo puyo) {
		puyo.setDirection(dir);
		Field nextField = field.getNextField(puyo, x);

		if (nextField == null) {
			return 0;
		}

		int rensaNum = 0;
		int redRensaNum;
		int blueRensaNum;
		int greenRensaNum;
		int yellowRensaNum;
		int purpleRensaNum;

		redRensaNum = (getColorPuyoNum(PuyoType.RED_PUYO, field)
				+ getFallColorPuyoNum(PuyoType.RED_PUYO, puyo) - getColorPuyoNum(
				PuyoType.RED_PUYO, nextField)) / 4;

		blueRensaNum = (getColorPuyoNum(PuyoType.BLUE_PUYO, field)
				+ getFallColorPuyoNum(PuyoType.BLUE_PUYO, puyo) - getColorPuyoNum(
				PuyoType.BLUE_PUYO, nextField)) / 4;

		greenRensaNum = (getColorPuyoNum(PuyoType.GREEN_PUYO, field)
				+ getFallColorPuyoNum(PuyoType.GREEN_PUYO, puyo) - getColorPuyoNum(
				PuyoType.GREEN_PUYO, nextField)) / 4;

		yellowRensaNum = (getColorPuyoNum(PuyoType.YELLOW_PUYO, field)
				+ getFallColorPuyoNum(PuyoType.YELLOW_PUYO, puyo) - getColorPuyoNum(
				PuyoType.YELLOW_PUYO, nextField)) / 4;

		purpleRensaNum = (getColorPuyoNum(PuyoType.PURPLE_PUYO, field)
				+ getFallColorPuyoNum(PuyoType.PURPLE_PUYO, puyo) - getColorPuyoNum(
				PuyoType.PURPLE_PUYO, nextField)) / 4;

		rensaNum = redRensaNum + blueRensaNum + greenRensaNum + yellowRensaNum
				+ purpleRensaNum;

		return rensaNum;
	}

	/**
	 * フィールド全体の、ある色のぷよの総数を返す
	 * 
	 * @param puyoType
	 * @param field
	 * @return
	 */
	private int getColorPuyoNum(PuyoType puyoType, Field field) {
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
	private int getFallColorPuyoNum(PuyoType puyoType, Puyo puyo) {
		int fallColorPuyoNum = 0;
		if (puyo.getPuyoType(PuyoNumber.FIRST) == puyoType) {
			fallColorPuyoNum++;
		}
		if (puyo.getPuyoType(PuyoNumber.SECOND) == puyoType) {
			fallColorPuyoNum++;
		}

		return fallColorPuyoNum;
	}

	/**
	 * 全ての場所に全ての向きでおいて最も大きい連鎖数を返す。
	 * 
	 * @param field
	 * @param puyo
	 * @return
	 */
	private int getMaxRensaNum(Field field, Puyo puyo) {
		int maxRensaNum = 0;
		int rensaNum = 0;

		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {
				rensaNum = getRensaNum(i, dir, field, puyo);
				if (maxRensaNum < rensaNum) {
					maxRensaNum = rensaNum;
				}

			}
		}

		return maxRensaNum;
	}

	

	/**
	 * 配置可能か，あるいは死んでしなわないかをチェックする
	 * 
	 * @param i
	 * @param dir
	 * @return 配置不能または死んでしまう場合はfalse
	 */
	private boolean isEnable(PuyoDirection dir, int i) {
		Field field = getMyBoard().getField();

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

	/**
	 * 特に配置する場所がなかった場合の基本行動
	 * 
	 * @return
	 */
	Action getDefaultAction() {
		Board board = getGameInfo().getBoard(getMyPlayerInfo());
		Field field = board.getField();
		int minColumn = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			if (field.getTop(i) < field.getTop(minColumn)) {
				minColumn = i;
			}
		}

		Action action = new Action(PuyoDirection.DOWN, minColumn);

		return action;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AbstractPlayer player = new KackyPlayer2("kacky");

		PuyoPuyo puyopuyo = new PuyoPuyo(player);
		puyopuyo.puyoPuyo();

	}

}
