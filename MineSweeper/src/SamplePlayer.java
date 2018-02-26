
import java.util.Random;

import jp.ne.kuramae.torix.lecture.ms.core.Cell;
import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;

/**
 * �T���v���v���C���[<br>
 * Player�N���X���p�������N���X���쐬����΂悢�D
 * @author tori
 *
 */
public class SamplePlayer extends Player {

	static public void main(String[] args){
		//�����Ŏ����̃v���C���[���쐬����
		Player player = new SamplePlayer();
		
		MineSweeper mineSweeper = new MineSweeper();
		mineSweeper.setRandomSeed(0);
		//MineSweeper mineSweeper = new MineSweeper(lv); //lv=0,1,2�Ń��x�����w�肵�č쐬
		//MineSweeper mineSweeper = new MineSweeper(width, height, bombNum); 

		
		mineSweeper.start(player);
	}
	
	/**
	 * �}�C���X�C�[�p�[�����s���郁�\�b�h<br>
	 * getWidth()�ŕ����擾<br>
	 * getHeight()�ō������擾<br>
	 * getCell(x, y)��x, y���W�̎���ɂ��锚�e�̐����l���\�D�܂��J���Ă��Ȃ��ꍇ��-1<br>
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
