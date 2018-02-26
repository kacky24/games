

import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;


public class FlagPlayer extends Player {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Player player = new FlagPlayer();
		MineSweeper mineSweeper = new MineSweeper();
		mineSweeper.start(player);

	}
	
	/**
	 * ���e������ꏊ�Ƀt���O�𗧂Ă�
	 */
	boolean[][] flag;

	/**
	 * �}�C���X�C�[�p�[�����s���郁�\�b�h<br>
	 * getWidth()�ŕ����擾<br>
	 * getHeight()�ō������擾<br>
	 * getCell(x, y)��x, y���W�̎���ɂ��锚�e�̐����l���\�D�܂��J���Ă��Ȃ��ꍇ��-1<br>
	 * open(x, y)�Ŏw��ʒu���I�[�v������
	 * <br>
	 */
	@Override
	protected void start() {
		flag = new boolean[getWidth()][getHeight()];
		for(int y = 0;y < getHeight();y++){
			for(int x = 0; x < getWidth(); x++){
				flag[x][y] = false;
			}
		}
		LOOP:while(!isClear() && !isGameOver()){
			
			//�܂��C�t���O�𗧂Ă�
			
			FLAG:for(int y = 0; y < getHeight(); y++){
				for(int x = 0; x < getWidth(); x++){
					boolean f = isFlag(x, y);
					flag[x][y] = f;
				}
			}				
		    OPEN:for(int y = 0; y < getHeight(); y++){ 
			    for(int x = 0; x < getWidth(); x++){
					if(flag[x][y] == true){
						continue;//continue�̈Ӗ��͂��̌�̍�Ƃ��s�킸���for�ɖ߂���x���P���₵�đ�����
					}
						
					int bombNum = getCell(x, y);
					if(bombNum < 0){
						open(x, y);
						break OPEN;//1�}�X��������܂��t���O�����Ă�Ƃ��납�炷�邽�߂�OPEN�̊O�ɏo��
					}
				}			    
			}
		   
			showFlag();
		}				
	}

	/**
	 * �t���O���ǂ��ɗ����Ă��邩���R���\�[���ɕ\������
	 */
	void showFlag() {
		System.out.println("======================");
		FLAG:for(int y = 0; y < getHeight(); y++){
			for(int x = 0; x < getWidth(); x++){
				if(flag[x][y] == true){
					System.out.print("x");
				}
				else if(getCell(x, y) >= 0){
					System.out.print(getCell(x, y));
				}
				else{
					System.out.print(".");
				}
			}
			System.out.println();
		}
		
	}
	
	/**
	 * �w��ʒu�Ƀt���O�𗧂Ă�ׂ����ǂ�����Ԃ�
	 * 
	 * 1110
	 * 1.10
	 * 1110
	 * 0000
	 * �ŁCx=1,y=1�̂Ƃ��C
	 * getCell(x+1, y+1)=1�ŁC���̎���ɊJ���Ă��Ȃ��Z���͈�����Ȃ̂�
	 * �J���Ă��Ȃ�Cell(x,y)�͕K�����e�ƂȂ�D
	 * ���������āC�����ɂ̓t���O�𗧂Ă邱�ƂɂȂ�D
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isFlag(int x, int y) {
		int bombNum = getCell(x, y);
		int num = 0;
		if(bombNum < 0){
			for(int dy = -1; dy <= 1; dy++){
				for(int dx = -1; dx <= 1; dx++){
					if((dx == 0 && dy == 0) == true){
						continue;//���̌�̍�Ƃ͍s�킸��ɖ߂�
					}
					if(isOut(x+dx,y+dy) == false){
						continue;
					}
					int n = getUnOpenCellNum(x+dx, y+dy);
					//�܂��J���Ă��Ȃ��Z���̐����C���̃Z���̎���ɂ��锚�e�̐��Ɠ����Ȃ�΂����ɔ��e������D
					if(n == getCell(x+dx, y+dy)){
						num = 1;
					}
				}
			}
		}
        if(num == 1){
        	return true;
        }else{
        	return false;
        }
        
	}

	/**
	 * �w�肵���Z���̎���ŊJ���Ă��Ȃ��Z���̐���Ԃ�
	 * @param i
	 * @param j
	 * @return
	 */
	int getUnOpenCellNum(int x, int y) {
		int unOpen = 0;
		for(int dy = -1; dy <= 1; dy++){
			for(int dx = -1; dx <= 1; dx++){
				if(isOut(x+dx,y+dy) == true){
				     if(getCell(x+dx,y+dy) < 0){
					 unOpen++;//x,y�̎���ɊJ���Ă��Ȃ��Z���������unOpen��1���₷
				     }
				}
			}
		}		
		return unOpen;
	}

	/**
	 * �O�ɔ�яo���Ă��Ȃ���
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isOut(int x, int y) {
		if((0<=x && x<getWidth() && 0<=y && y<getHeight()) == true){
			return true;//x,y���͈͊O�Ȃ�false��Ԃ�
		}else{
		    return false;
		}
	}



}
