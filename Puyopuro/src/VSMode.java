
import com.rallyemusashi.tamura.Tamu12;

import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.AbstractPlayer;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.PuyoPuyo;
import jp.ne.ezweb.kicker.kacky.KackyFinal1;
import jp.ne.ezweb.kicker.kacky.KackyFinal2;
import jp.ne.ezweb.kicker.kacky.KackyPlayer1;
import jp.ne.ezweb.kicker.kacky.KackyPlayer10;
import jp.ne.ezweb.kicker.kacky.KackyPlayer11;
import jp.ne.ezweb.kicker.kacky.KackyPlayer12;
import jp.ne.ezweb.kicker.kacky.KackyPlayer3;
import jp.ne.ezweb.kicker.kacky.KackyPlayer4;
import jp.ne.ezweb.kicker.kacky.KackyPlayer5;
import jp.ne.ezweb.kicker.kacky.KackyPlayer6;
import jp.ne.ezweb.kicker.kacky.KackyPlayer7;
import jp.ne.ezweb.kicker.kacky.KackyPlayer8;
import jp.ne.ezweb.kicker.kacky.KackyPlayer9;
import kajiGod.KajiGodGodGod;
import kajiGod.KajiPlayer;
import maou2014.Maou;
import moc.liamtoh900ognek.KajiGodGod;
import SamplePlayer.Nohoho;
import SamplePlayer.ScoringPlayer;
import UsuiPlayer.UsuiPlayer;
import UsuiPlayerLv2.UsuiPlayerLv2;

/**
 * 任意の二体のエージェント同士を対戦させるためのクラス
 */
public class VSMode {

	public static void main(String args[]) {

		AbstractPlayer maou = new Maou("Maou");			//最強
		AbstractPlayer TA2 = new KajiGodGod("KajiGod");	//TA2
		AbstractPlayer TA1= new UsuiPlayer("Usui");		//TA1
		AbstractPlayer Nohoho = new Nohoho("Nohoho");	//カエル積み
		AbstractPlayer KackyPlayer3 = new KackyPlayer3("kacky");
		AbstractPlayer ScoringPlayer = new ScoringPlayer("score");
		AbstractPlayer KackyPlayer4 = new KackyPlayer4("kacky");
		AbstractPlayer TA3=new UsuiPlayerLv2("Usui2"); 
		AbstractPlayer KackyPlayer5 = new KackyPlayer5("kacky");
		AbstractPlayer Tamura=new Tamu12("tamu"); 
		AbstractPlayer KackyPlayer6 = new KackyPlayer6("kacky6");
		AbstractPlayer KackyPlayer7 = new KackyPlayer7("kacky7");
		AbstractPlayer KackyPlayer8 = new KackyPlayer8("kacky8");
		AbstractPlayer KackyPlayer9 = new KackyPlayer9("kacky9");	
		AbstractPlayer KackyPlayer10 = new KackyPlayer10("kacky10");	
		AbstractPlayer KackyPlayer11 = new KackyPlayer11("かきうち11");
		AbstractPlayer KackyPlayer12 = new KackyPlayer12("かきうち12");
		AbstractPlayer KajiPlayer = new  KajiPlayer("kaji");
		AbstractPlayer KajiGodGodGod = new  KajiGodGodGod("kaji");
		AbstractPlayer KackyFinal1=new KackyFinal1("kackyFinal1");
		AbstractPlayer KackyFinal2=new KackyFinal2("kackyFinal2");
		
		/**
		 * 任意の二つのエージェントを対戦させる．<br>
		 */
		PuyoPuyo puyopuyo = new PuyoPuyo(TA1, KackyFinal2);
		puyopuyo.puyoPuyo();



	}
}
