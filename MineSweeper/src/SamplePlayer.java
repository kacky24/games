
import java.util.Random;

import jp.ne.kuramae.torix.lecture.ms.core.Cell;
import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;

/**
 * サンプルプレイヤー<br>
 * Playerクラスを継承したクラスを作成すればよい．
 * @author tori
 *
 */
public class SamplePlayer extends Player {

	static public void main(String[] args){
		//ここで自分のプレイヤーを作成する
		Player player = new SamplePlayer();
		
		MineSweeper mineSweeper = new MineSweeper();
		mineSweeper.setRandomSeed(0);
		//MineSweeper mineSweeper = new MineSweeper(lv); //lv=0,1,2でレベルを指定して作成
		//MineSweeper mineSweeper = new MineSweeper(width, height, bombNum); 

		
		mineSweeper.start(player);
	}
	
	/**
	 * マインスイーパーを実行するメソッド<br>
	 * getWidth()で幅を取得<br>
	 * getHeight()で高さを取得<br>
	 * getCell(x, y)でx, y座標の周りにある爆弾の数を獲得可能．まだ開けていない場合は-1<br>
	 * 
	 * <br>
	 * 
	 */
	@Override
	protected void start() {
		LOOP:while(!isClear() && !isGameOver()){
			for(int y = 0; y < getHeight(); y++){
				for(int x = 0; x < getWidth(); x++){
					int bombNum = getCell(x, y);
//					System.out.println(x+","+y+"="+bombNum);
					if(bombNum < 0){
						open(x, y);
						continue LOOP;
					}
				}
			}
		}				
	}


}
