package jp.ne.ezweb.kicker.kacky;

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

public class KackyPlayer3 extends AbstractPlayer {

	public KackyPlayer3(String kacky) {
		super(kacky);
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
		Puyo nextPuyo = getMyBoard().getNextPuyo();
		Puyo nextNextPuyo = getMyBoard().getNextNextPuyo();

		Action action = null;

		int maxScore = 0;

		RensaCounter rnsC = new RensaCounter(field, puyo);

		int[][] rensaNum = rnsC.getRensaNum();
		int dirNum = 0;

		RensaPredictor rnsP = new RensaPredictor(field, puyo, nextPuyo,
				nextNextPuyo);

		MAIN: for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {

				puyo.setDirection(dir);

				Field nextField = field.getNextField(puyo, i);

				if (!isEnable(dir, i)) {
					continue;
				}

				dirNum = rnsC.exchangeDirInt(dir);
				int max = rnsC.getMaxRensaNum();

				//ある連鎖数を超えるなら、連鎖させる
				if (rensaNum != null) {
					System.out.print(i);
					System.out.print(dirNum);
					System.out.println(max);
					if (rensaNum[i][dirNum] > 3) {
						action = new Action(dir, i);
						System.out.println("Rensa");

						break MAIN;
					}
				}
			}
		}
		
		if(action!=null){
			return action;
		}

		//次の次のフィールドで連鎖数が最大になるように置く
		rnsP.getNextNextRensaNum();
		if (rnsP.nextNextMaxDir != null && rnsP.nextNextMaxColumn != 0) {
			action=new Action(rnsP.nextNextMaxDir,rnsP.nextNextMaxColumn);
			System.out.println("よち");

		}
		
		if(action!=null){
			return action;
		}

		//score
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {

				dirNum = rnsC.exchangeDirInt(dir);
				
				if(rensaNum[i][dirNum]>0){  //１連鎖ををおこさせない
					continue;
				}
				
				puyo.setDirection(dir);

				Field nextField = field.getNextField(puyo, i);
				
				if (!isEnable(dir, i)) {
					continue;
				}

				if (nextField != null) {

					int oneConnectNum = getConnectNum(1, nextField);
					int twoConnectNum = getConnectNum(2, nextField);
					int threeConnectNum = getConnectNum(3, nextField);
					int score = 0;

					score = threeConnectNum * 6 + twoConnectNum * 4
							- oneConnectNum;
					System.out.println("score" + score);
					if (score > maxScore) {
						System.out.println("なんで" + i);
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
		AbstractPlayer player = new KackyPlayer3("kacky");

		PuyoPuyo puyopuyo = new PuyoPuyo(player);
		puyopuyo.puyoPuyo();

	}

}
