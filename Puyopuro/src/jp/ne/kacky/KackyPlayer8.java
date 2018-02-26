package jp.ne.ezweb.kicker.kacky;

import java.util.ArrayList;
import java.util.List;

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

public class KackyPlayer8 extends AbstractPlayer {

	public KackyPlayer8(String playerName) {
		super(playerName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Action doMyTurn() {
		// TODO Auto-generated method stub

		Field field = getMyBoard().getField();
		Field enemyField = getEnemyBoard().getField();

		Puyo puyo = getMyBoard().getCurrentPuyo();
		Puyo nextPuyo = getMyBoard().getNextPuyo();
		Puyo nextNextPuyo = getMyBoard().getNextNextPuyo();
		Puyo enemyPuyo = getEnemyBoard().getCurrentPuyo();

		RensaCounter rnsC = new RensaCounter(field, puyo);
		int[][] rensaNum = rnsC.getRensaNum();
		RensaCounter ErnsC = new RensaCounter(enemyField, enemyPuyo);

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
				System.out.println("かなり危険8");
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

						System.out.println("危険スルー8");
						action = new Action(Dir, ColumnN);
						return action;
					}
				} else {
					ColumnN = rnsC.returnCol();
					int dirNum2 = rnsC.returnDir();
					Dir = rnsC.exchangeIntDir(dirNum2);
					if (isEnable(Dir, ColumnN)) {

						System.out.println("危険8");
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
							System.out.println("ちょい危険8");
							action = new Action(Dir, ColumnN);
							return action;
						}
					}
					if (maxNR2 > maxR2 && maxNR2 >= maxNNR2) {
						int dirNum4 = rnsP.returnNDir();
						Dir = rnsP.exchangeIntDir(dirNum4);
						ColumnN = rnsP.returnNCol();
						if (isEnable(Dir, ColumnN)) {
							System.out.println("ちょい危険スルー8");
							action = new Action(Dir, ColumnN);
							return action;
						}
					}
					if (maxNNR2 > maxR2 && maxNNR2 > maxNR2) {
						int dirNum5 = rnsP.returnNNDir();
						Dir = rnsP.exchangeIntDir(dirNum5);
						ColumnN = rnsP.returnNNCol();
						if (isEnable(Dir, ColumnN)) {
							System.out.println("ちょい危険スルースルー8");
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

		if (myMaxRensa > enemyMaxRensa + 1) {
			ColumnN = rnsC.returnCol();
			int dirNum6 = rnsC.returnDir();
			Dir = rnsC.exchangeIntDir(dirNum6);
			if (rensaNum[ColumnN][dirNum6] > 2) {
				action = new Action(Dir, ColumnN);
				System.out.println("いけ8！");
				return action;
			}
		}

		// 8連鎖以上ならうつ
		if (myMaxRensa > 4) {
			ColumnN = rnsC.returnCol();
			int dirNum7 = rnsC.returnDir();
			Dir = rnsC.exchangeIntDir(dirNum7);
			action = new Action(Dir, ColumnN);
			System.out.println("連鎖8！");
			return action;
		}

		// score
		int maxNNR3 = rnsP.getNextNextRensaNum();
		int dirNum8 = rnsP.returnNNDir();
		Dir = rnsP.exchangeIntDir(dirNum8);
		ColumnN = rnsP.returnNNCol();
		
		//発火点を決める
		int hakkaColumn=0;
		PuyoDirection hakkaDir=null;
		int maxR=0;
		
		RensaCounter rns1 =new RensaCounter(field,puyo);
		RensaCounter rns2 =new RensaCounter(field,nextPuyo);
		RensaCounter rns3 =new RensaCounter(field,nextNextPuyo);
		
		int maxR1 =rns1.getMaxRensaNum();
		int maxR2 =rns2.getMaxRensaNum();
		int maxR3 =rns3.getMaxRensaNum();
		
		int DN1=rns1.returnDir();
		PuyoDirection Dir1=rns1.exchangeIntDir(DN1);
		int Column1=rns1.returnCol();
		
		int DN2=rns2.returnDir();
		PuyoDirection Dir2=rns2.exchangeIntDir(DN2);
		int Column2=rns2.returnCol();
		
		int DN3=rns3.returnDir();
		PuyoDirection Dir3=rns3.exchangeIntDir(DN3);
		int Column3=rns3.returnCol();
		
		if(maxR1>=maxR2&&maxR1>=maxR3){
			hakkaColumn=Column1;
			hakkaDir=Dir1;
			maxR=maxR1;
		}else{
			if(maxR2>maxR1&&maxR2>=maxR3){
				hakkaColumn=Column2;
				hakkaDir=Dir2;
				maxR=maxR2;
			}else{
				if(maxR3>maxR1&&maxR3>maxR2){
					hakkaColumn=Column3;
					hakkaDir=Dir3;
					maxR=maxR3;
				}
			}
		}

		int score = 0;
		int maxScore = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			for (PuyoDirection dir : PuyoDirection.values()) {

				if (!isEnable(dir, i)) {
					continue;
				}

				int dirnum = rnsC.exchangeDirInt(dir);
				if (rensaNum[i][dirnum] > 0) {
					continue;
				}

				//発火点を消さない
				if(maxR>1){
					if(i==hakkaColumn){
						continue;
					}
					if(i>0){
						if(i==hakkaColumn+1&&dir==PuyoDirection.LEFT){
							continue;
						}
					}
					if(i<5){
						if(i==hakkaColumn-1&&dir==PuyoDirection.RIGHT){
							continue;
						}
					}
				}
				
				score = getScore(i, dir);

				// nextNext
				if (i == ColumnN && dir == Dir) {
					if (maxNNR3 > 2) {
						score += maxNNR3 * 20;
						System.out.println("＋よち8");

					}
				}

				if (maxScore < score) {
					maxScore = score;
					action = new Action(dir, i);
				}
			}
		}
		if (action != null) {
			System.out.println("つむつむ8");
			return action;
		}

		if (action == null) {
			System.out.println("Default8");
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

	private int getScore(int x, PuyoDirection dir) {
		// System.out.printf("%d,%s\n", x, dir);
		Field field = getMyBoard().getField();
		Puyo puyo = getMyBoard().getCurrentPuyo();
		puyo.setDirection(dir);
		Field nextField = field.getNextField(puyo, x);

		if (nextField == null) {
			return 0;
		}

		// 危機的状況かどうか
		boolean emergency = false;

		int totalPuyoNum = 0;
		for (int i = 0; i < field.getWidth(); i++) {
			totalPuyoNum += field.getTop(i);
		}
		if (getMyBoard().getTotalNumberOfOjama() > 0
				|| totalPuyoNum > field.getWidth() * field.getHeight() / 3) {
			emergency = true;
		}

		int score = 0;

		// つながりが強いほど高スコア
		ConnectionCounter cnt = new ConnectionCounter(nextField);
		int[][] countField = cnt.getConnectedPuyoNum();

		for (int i = 0; i < countField.length; i++) {
			for (int j = 0; j < countField[i].length; j++) {
				score += countField[i][j];
			}
		}

		if (emergency) {
			// 危機的状況の時は積極的に消しに行く
			score += field.getHeight() * field.getWidth()
					- getPuyoNum(nextField);
			score += (getPuyoNum(field) - getPuyoNum(nextField)) * 2;

			/*
			 * int max = 0; for(int i = 0; i < nextField.getWidth(); i++){ max =
			 * Math.max(max, nextField.getTop(i)+1); } score +=
			 * field.getHeight()-max;
			 */
		} else {
			// 危機的状況のでなければ，つながりを多くする

			int max = 0;
			int min = field.getHeight();
			for (int i = 0; i < nextField.getWidth(); i++) {
				max = Math.max(max, nextField.getTop(i) + 1);
				min = Math.min(min, nextField.getTop(i) + 1);
			}

			score += (field.getHeight() - (max - min));

			// 3連鎖以上する場合は積極的に置く
			if (getPuyoNum(nextField) < getPuyoNum(field) - 4 * 3) {
				score += (getPuyoNum(field) - getPuyoNum(nextField));
				score *= 2;
			}

			/**
			 * 一番右にはあまり置かない
			 */
			if (x == 5) {
				score = score * 2 / 3;
			}
			if (x == 0) {
				score = score * 2 / 3;
			}

		}
		return score;
	}

	/**
	 * 指定したフィールドのぷよ数を返す
	 * 
	 * @param field
	 * @return
	 */
	int getPuyoNum(Field field) {
		int num = 0;
		// ここでぷよの数を数える．
		// field.getTop(columnNum)で，ぷよが存在する場所を返すので，
		// それより1大きい数のぷよがその列には存在する
		// ぷよが一つもない列は-1が返ってくることに注意．

		for (int i = 0; i < field.getWidth(); i++) {
			num += field.getTop(i) + 1;
		}

		return num;
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
		AbstractPlayer player = new KackyPlayer8("kacky");

		PuyoPuyo puyopuyo = new PuyoPuyo(player);
		puyopuyo.puyoPuyo();

	}

}
