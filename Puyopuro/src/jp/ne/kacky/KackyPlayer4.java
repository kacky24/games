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

public class KackyPlayer4 extends AbstractPlayer {

	public KackyPlayer4(String kacky) {
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
					if (rensaNum[i][dirNum] > 2) {
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
		int h =rnsP.getNextNextRensaNum();
		int h1 = rnsP.returnNNCol();
		int h2=rnsP.returnNNDir();
		PuyoDirection aDir=rnsP.exchangeIntDir(h2);
		if (aDir != null && h1 != 0) {
			action=new Action(aDir,h1);
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

				
				
				if (!isEnable(dir, i)) {
					continue;
				}

				int score = getScore(i, dir);
				if(score > maxScore){
					action = new Action(dir, i);
					maxScore = score;
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
	
	private int getScore(int x, PuyoDirection dir) {
//		System.out.printf("%d,%s\n", x, dir);
		Field field = getMyBoard().getField();
		Puyo puyo = getMyBoard().getCurrentPuyo();
		puyo.setDirection(dir);
		Field nextField = field.getNextField(puyo, x);

		if(nextField == null){
			return 0;
		}

		//危機的状況かどうか
		boolean emergency = false;

		int totalPuyoNum = 0;
		for(int i = 0; i < field.getWidth(); i++){
			totalPuyoNum += field.getTop(i);
		}
		if(getMyBoard().getTotalNumberOfOjama() > 0 || totalPuyoNum > field.getWidth()*field.getHeight()/3){
			emergency = true;
		}


		int score = 0;

		//つながりが強いほど高スコア
		ConnectionCounter cnt = new ConnectionCounter(nextField);
		int[][] countField = cnt.getConnectedPuyoNum();

		for(int i = 0; i < countField.length; i++){
			for(int j = 0; j < countField[i].length; j++){
				score += countField[i][j];
			}
		}

		if(emergency){
			//危機的状況の時は積極的に消しに行く
			score += field.getHeight()*field.getWidth()-getPuyoNum(nextField);
			score += (getPuyoNum(field) - getPuyoNum(nextField))*2;

			/*
			int max = 0;
			for(int i = 0; i < nextField.getWidth(); i++){
				max = Math.max(max, nextField.getTop(i)+1);
			}
			score += field.getHeight()-max;
			*/
		}
		else{
			//危機的状況のでなければ，つながりを多くする

			int max = 0;
			int min = field.getHeight();
			for(int i = 0; i < nextField.getWidth(); i++){
				max = Math.max(max, nextField.getTop(i)+1);
				min = Math.min(min, nextField.getTop(i)+1);
			}

			score += (field.getHeight()-(max-min));

			//3連鎖以上する場合は積極的に置く
			if(getPuyoNum(nextField) < getPuyoNum(field)-4*3){
				score += (getPuyoNum(field)-getPuyoNum(nextField));
				score *= 2;
			}

			/**
			 * 一番右にはあまり置かない
			 */
			if(x == field.getWidth()/2){
				score /= 2;
			}
		}


		System.out.printf("[%d-%s]\t%d\t%d\n", x, dir.toString(), score, getPuyoNum(nextField));
		return score;
	}

	/**
	 * 指定したフィールドのぷよ数を返す
	 * @param field
	 * @return
	 */
	int getPuyoNum(Field field){
		int num = 0;
		//ここでぷよの数を数える．
		//field.getTop(columnNum)で，ぷよが存在する場所を返すので，
		//それより1大きい数のぷよがその列には存在する
		//ぷよが一つもない列は-1が返ってくることに注意．

		for(int i = 0; i < field.getWidth(); i++){
			num+=field.getTop(i)+1;
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

		AbstractPlayer player = new KackyPlayer4("kacky");

		PuyoPuyo puyopuyo = new PuyoPuyo(player);
		puyopuyo.puyoPuyo();
	}

}
