
import jp.ne.kuramae.torix.lecture.ms.core.MineSweeper;
import jp.ne.kuramae.torix.lecture.ms.core.Player;


public class Play100 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        int score = 0;
		for(int i = 0;i < 100;i++){
        	Player player = new FinalPlayer();
    		MineSweeper mineSweeper = new MineSweeper(2);
    		boolean isClear = mineSweeper.start(player);
    		if(isClear == true){
    			score++;
    			System.out.println(i);
    			System.out.println(score);
    		}
		}		
        System.out.println("Clear:"+score); 	
   }		

	
}
