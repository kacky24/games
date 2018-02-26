package jp.ne.ezweb.kicker.kacky;

import java.util.ArrayList;
import java.util.List;

import sp.ConnectionCounter;
import sp.ConnectionCounter2;
import sp.RensaCounter;
import sp.RensaPredictor;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Action;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Board;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;

public class KackyFinal2 extends AbstractPlayer {

	public KackyFinal2(String playerName) {
		super(playerName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action doMyTurn() {
		// TODO Auto-generated method stub

		Field field = getMyBoard().getField();
		Field enemyField = getEnemyBoard().getField();
		Field nextField;
		Field nextDField;
		Field nextD2Field;
		Field next2Field;
		Field next3Field;

		Puyo puyo = getMyBoard().getCurrentPuyo();
		Puyo nextPuyo = getMyBoard().getNextPuyo();
		Puyo nextNextPuyo = getMyBoard().getNextNextPuyo();
		Puyo enemyPuyo = getEnemyBoard().getCurrentPuyo();
		Puyo enemyNPuyo = getEnemyBoard().getNextPuyo();
		Puyo enemyNNPuyo = getEnemyBoard().getNextNextPuyo();

		RensaCounter rnsC = new RensaCounter(field, puyo);
		int[][] rensaNum = rnsC.getRensaNum();
		RensaCounter ErnsC = new RensaCounter(enemyField, enemyPuyo);
		RensaCounter ENrnsC = new RensaCounter(enemyField, enemyNPuyo);
		RensaCounter ENNrnsC = new RensaCounter(enemyField, enemyNNPuyo);

		RensaPredictor rnsP = new RensaPredictor(field, puyo, nextPuyo,
				nextNextPuyo);

		boolean emargency2 = false;
		boolean emargency1 = false;
		boolean emargency0 = false;

		Action action = null;

		int ColumnN;
		PuyoDirection Dir;

		List<Integer> ojamaList = new ArrayList<Integer>();
		ojamaList = getMyBoard().getNumbersOfOjamaList();

		// 危険かどうかの判定（降ってくるおじゃまによる）
		if (ojamaList.get(2) > 4) {
			emargency2 = true;
		}
		if (ojamaList.get(1) > 4) {
			emargency1 = true;
		}
		if (ojamaList.get(0) > 4) {
			emargency0 = true;
		}

		// 危険状態での置き方
		if (emargency0 == true) {

			int maxR0 = rnsC.getMaxRensaNum();
			ColumnN = rnsC.returnCol();
			int dirNum0 = rnsC.returnDir();
			Dir = rnsC.exchangeIntDir(dirNum0);
			if (isEnable(Dir, ColumnN)) {
				System.out.println("かなり危険11");
				action = new Action(Dir, ColumnN);
				return action;
			}

		} else {
			if (emargency1 == true) {

				int maxR1 = rnsC.getMaxRensaNum();
				int maxNR1 = rnsP.getNextRensaNum();

				if (maxR1 < maxNR1) {
					int dirNum1 = rnsP.returnNDir();
					Dir = rnsP.exchangeIntDir(dirNum1);
					ColumnN = rnsP.returnNCol();
					if (isEnable(Dir, ColumnN)) {

						System.out.println("危険スルー11");
						action = new Action(Dir, ColumnN);
						return action;
					}
				} else {
					ColumnN = rnsC.returnCol();
					int dirNum2 = rnsC.returnDir();
					Dir = rnsC.exchangeIntDir(dirNum2);
					if (isEnable(Dir, ColumnN)) {

						System.out.println("危険11");
						action = new Action(Dir, ColumnN);
						return action;
					}
				}
			} else {
				if (emargency2 == true) {

					int maxR2 = rnsC.getMaxRensaNum();
					int maxNR2 = rnsP.getNextRensaNum();
					int maxNNR2 = rnsP.getNextNextRensaNum();

					if (maxR2 >= maxNR2 && maxR2 >= maxNNR2) {
						ColumnN = rnsC.returnCol();
						int dirNum3 = rnsC.returnDir();
						Dir = rnsC.exchangeIntDir(dirNum3);
						if (isEnable(Dir, ColumnN)) {
							System.out.println("ちょい危険11");
							action = new Action(Dir, ColumnN);
							return action;
						}
					}
					if (maxNR2 > maxR2 && maxNR2 >= maxNNR2) {
						int dirNum4 = rnsP.returnNDir();
						Dir = rnsP.exchangeIntDir(dirNum4);
						ColumnN = rnsP.returnNCol();
						if (isEnable(Dir, ColumnN)) {
							System.out.println("ちょい危険スルー11");
							action = new Action(Dir, ColumnN);
							return action;
						}
					}
					if (maxNNR2 > maxR2 && maxNNR2 > maxNR2) {
						int dirNum5 = rnsP.returnNNDir();
						Dir = rnsP.exchangeIntDir(dirNum5);
						ColumnN = rnsP.returnNNCol();
						if (isEnable(Dir, ColumnN)) {
							System.out.println("ちょい危険スルースルー11");
							action = new Action(Dir, ColumnN);
							return action;
						}
					}
				}
			}
		}

		// 安全状態での行動

		// 相手の連鎖数より自分が有利ならうつ
		int myMaxRensa = rnsC.getMaxRensaNum();
		int enemyMaxRensa = ErnsC.getMaxRensaNum();
		int enemyNMaxRensa = ENrnsC.getMaxRensaNum();
		int enemyNNMaxRensa = ENNrnsC.getMaxRensaNum();

		if (myMaxRensa > enemyMaxRensa + 1) {
			if (myMaxRensa > enemyNMaxRensa) {
				if (myMaxRensa > enemyNNMaxRensa) {

					ColumnN = rnsC.returnCol();
					int dirNum6 = rnsC.returnDir();
					Dir = rnsC.exchangeIntDir(dirNum6);
					if (rensaNum[ColumnN][dirNum6] > 2) {
						action = new Action(Dir, ColumnN);
						System.out.println("いけ11！");
						return action;
					}
				}
			}
		}

		// 8連鎖以上ならうつ
		if (myMaxRensa > 3) {
			ColumnN = rnsC.returnCol();
			int dirNum7 = rnsC.returnDir();
			Dir = rnsC.exchangeIntDir(dirNum7);
			action = new Action(Dir, ColumnN);
			System.out.println("連鎖11！");
			return action;
		}

		// よち
		int maxNNR3 = rnsP.getNextNextRensaNum();
		int dirNum8 = rnsP.returnNNDir();
		Dir = rnsP.exchangeIntDir(dirNum8);
		ColumnN = rnsP.returnNNCol();

		// score
		int score = 0;
		int score1 = 0;
		int score2 = 0;
		int score3 = 0;

		int stopNum;

		int hakkaKeep;

		int yochiScore;

		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir1 : PuyoDirection.values()) {
				puyo.setDirection(dir1);
				nextPuyo.setDirection(dir1);
				nextNextPuyo.setDirection(dir1);

				if (!isEnable2(field, puyo, i)) {
					continue;
				}

				yochiScore=0;
				
				stopNum = rnsC.exchangeDirInt(dir1);

				if (rensaNum[i][stopNum] > 0) {// 発火を防ぐ
					continue;
				}

				nextField = field.getNextField(puyo, i);
				nextDField = field.getNextField(nextPuyo, i);
				nextD2Field = field.getNextField(nextNextPuyo, i);

				

				score1 = getScore(nextField);
				// nextNext
				if (i == ColumnN && dir1 == Dir) {
					if (maxNNR3 > 1) {
						yochiScore = maxNNR3 * 100;
						System.out.println("＋よち11");
					}
				}
				
				if(yochiScore>0){
					score1+=yochiScore;
				}

				if (score < score1) {
					score = score1;
					action = new Action(puyo, i);
				}

				for (int j = 0; j < field.getWidth(); j++) {
					for (PuyoDirection dir2 : PuyoDirection.values()) {
						nextPuyo.setDirection(dir2);

						if (!isEnable2(nextField, nextPuyo, j)) {
							continue;
						}

						next2Field = nextField.getNextField(nextPuyo, j);
						score2 = getScore(next2Field);

						if(yochiScore>0){
							score2+=yochiScore;
						}

						if (score < score2) {
							score = score2;
							action = new Action(puyo, i);
						}

						for (int k = 0; k < field.getWidth(); k++) {
							for (PuyoDirection dir3 : PuyoDirection.values()) {
								nextNextPuyo.setDirection(dir3);

								if (!isEnable2(next2Field, nextNextPuyo, k)) {
									continue;
								}

								next3Field = next2Field.getNextField(
										nextNextPuyo, k);
								score3 = getScore(next3Field);

								if(yochiScore>0){
									score3+=yochiScore;
								}
								
								if (score < score3) {
									score = score3;
									action = new Action(puyo, i);
								}
							}
						}
					}
				}

			}
		}
		if (action != null) {
			System.out.println("つむつむ");
			return action;
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
	 * フィールドの点数を返す
	 * 
	 * @param field
	 * @return
	 */
	private int getScore(Field field) {
		int score = 0;
		int score1 = 0;
		int score2 = 0;
		int score3 = 0;
		int score4 = 0;
		int score5 = 0;
		int score6 = 0;
		int sum = getSumPuyoOnField(field);
		ConnectionCounter2 connect = new ConnectionCounter2(field);
		int  connectField[][]=connect.getConnectPuyoNum();
		PuyoType puyoType[][] = connect.getPTField();
		for (int c = 0; c < 6; c++) {
			for (int r = 0; r < 14; r++) {
				// つながりが
				score1 = (int) ((double) score1 + Math.pow(
						connectField[c][r], 3D));// pow(a,b):aのb乗
				if (connectField[c][r] == 1) {
					int iso = 0;
					if (r < 13 && connectField[c][r + 1] == 0)
						iso++;
					if (c >= 1 && connectField[c - 1][r] == 0)
						iso++;
					if (c <= 4 && connectField[c + 1][r] == 0)
						iso++;
					switch (iso)// ひとつだけのぷよを、周囲を違う色で囲まれているほどマイナスにする。まわりに何もないとマイナスしない。
					{
					case 0: 
						score2 -= 100;
						break;

					case 1: 
						score2 -= 3;
						break;

					case 2: 
						score2 += 0;
						break;

					case 3: 
						score2 += 0;
						break;
					}
				}
				if (connectField[c][r] == 3)// 同色３つの塊のとき
				{
					for (int c2 = c - 1; c2 <= c + 1; c2++)
						// 自身と両隣
						if (c2 != -1 && c2 != 6) {
							boolean countOverFlag = false;
							for (int r2 = r; r2 < 14; r2++)
								// 自身以上
								if (countOverFlag) {
									if (puyoType[c2][r2] == puyoType[c][r])// 自身を含む塊以外に、両隣そして上に同色があるならポイント＋
									{
										score3 += 8 / ((r2 - r) + 1);
										break;
									}
									if (puyoType[c2][r2] == null)
										;
								} else if (puyoType[c2][r2] != puyoType[c][r])
									countOverFlag = true;

						}

					boolean countFireFrag = false;
					if (c != 0 && connectField[c - 1][r] == 0
							&& field.getTop(c - 1) + 2 <= r)
						countFireFrag = true;
					if (c != 5 && connectField[c + 1][r] == 0
							&& field.getTop(c + 1) + 2 <= r)
						countFireFrag = true;
					if (countFireFrag)
						score4 += Math.min(field.getTop(c) - r, 3);
				}
			}

		}

		if ((field.getTop(2) + field.getTop(3)) * 3 < sum)// 中心２列に集まるようにポイントー
			score5 -= 5 * (sum - (field.getTop(2) + field
					.getTop(3)));
		for (int c = 0; c < 5; c++)// となりとの高低差が大きいほどポイントー
		{
			int div = Math.abs(field.getTop(c) - field.getTop(c + 1));// abs：絶対値
			if (div > 3)
				score6 -= div * 4;
		}

		score = score1 + score2 + score3 + score4
				+ score5 + score6;
		return score;
	}

	/**
	 * フィールドの総ぷよ数
	 * 
	 * @param field
	 * @return
	 */
	public int getSumPuyoOnField(Field field) {
		int sum = 0;
		for (int i = 0; i < 6; i++)
			sum += field.getTop(i) + 1;

		return sum;
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

	private boolean isEnable2(Field field, Puyo puyo, int i) {
		jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection dir = puyo
				.getDirection();
		if (!field.isEnable(dir, i))
			return false;
		if (dir == jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection.DOWN
				|| dir == jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection.UP) {
			if (field.getTop(i) >= field.getDeadLine() - 2)
				return false;
		} else if (dir == jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection.RIGHT) {
			if (field.getTop(i) >= field.getDeadLine() - 2
					|| field.getTop(i + 1) >= field.getDeadLine() - 2)
				return false;
		} else if (dir == jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo.PuyoDirection.LEFT
				&& (field.getTop(i) >= field.getDeadLine() - 2 || field
						.getTop(i - 1) >= field.getDeadLine() - 2))
			return false;
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

	}

}
