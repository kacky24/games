
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import player.SamplePlayer02;
/**
 * メインクラス
 */

public class MainClass {

	public static void main(String args[]) {
		/**
		 * プレイヤーの作成
		 * SamplePlayerの部分を別のエージェントクラスに変更すればそのエージェントで実行される
		 */
		AbstractPlayer player = new SamplePlayer02();

		/**
		 * ぷよぷよの実行
		 */
		PuyoPuyo puyopuyo = new PuyoPuyo(player);
		puyopuyo.puyoPuyo();



	}
}
