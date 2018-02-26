package jp.ne.ezweb.kicker.kacky;

import sp.RensaCounter;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Board;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;

public class PracticePlayer extends AbstractPlayer {

	public PracticePlayer(String playerName) {
		super(playerName);
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
		RensaCounter rns = new RensaCounter(field, puyo);
		int rensaNum[][] = rns.getRensaNum();
		int dirNum = 0;

		if (rensaNum != null) {
			System.out.println(rensaNum[3][3]);
		}
		System.out.println("Default");
		action = getDefaultAction();
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
	
	public boolean isEnable(PuyoDirection dir, int i) {
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


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AbstractPlayer player = new PracticePlayer("kacky");

		PuyoPuyo puyopuyo = new PuyoPuyo(player);
		puyopuyo.puyoPuyo();

	}

}
