package sp;

import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Field;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.game.Puyo;
import jp.ac.nagoya_u.is.ss.kishii.usui.system.storage.PuyoType;

public class ConnectionCounter2
{

    public ConnectionCounter2(Field field)
    {
        nextNum = new int[6][14];
        connectNum = new int[6][14];
        ptField = new PuyoType[6][14];
        this.field = field;
        ptField = getPTField();
    }

   

    /**
     * フィールド上すべてのぷよタイプを返す
     * @return
     */
    public PuyoType[][] getPTField()
    {
        for(int column = 0; column < 6; column++)
        {
            for(int row = 0; row < 14; row++)
                ptField[column][row] = field.getPuyoType(column, row);

        }

        return ptField;
    }

    /**
     * 
     * @return
     */
    private int[][] getNextPuyoNum()
    {
        for(int column = 0; column < 6; column++)
        {
            for(int row = 0; row < 14; row++)
                if(ptField[column][row] != null)
                {
                    nextNum[column][row]++;
                    if(field.isOnField(column - 1, row) && ptField[column - 1][row] == ptField[column][row])
                        nextNum[column][row]++;
                    if(field.isOnField(column, row - 1) && ptField[column][row - 1] == ptField[column][row])
                        nextNum[column][row]++;
                    if(field.isOnField(column + 1, row) && ptField[column + 1][row] == ptField[column][row])
                        nextNum[column][row]++;
                    if(field.isOnField(column, row + 1) && ptField[column][row + 1] == ptField[column][row])
                        nextNum[column][row]++;
                }

        }

        return nextNum;
    }

    /**
     * 
     * @return
     */
    public int[][] getConnectPuyoNum()
    {
        getNextPuyoNum();
        for(int c = 0; c < 6; c++)
        {
            for(int r = 0; r < 14; r++)
                connectNum[c][r] = nextNum[c][r];

        }

        for(int c = 0; c < 6; c++)
        {
            for(int r = 0; r < 14; r++)
            {
                if(connectNum[c][r] != 3)
                    continue;
                if(field.isOnField(c - 1, r) && ptField[c - 1][r] == ptField[c][r] && connectNum[c - 1][r] == 3)
                {
                    roundToN(c, r, 4);
                    roundToN(c - 1, r, 4);
                    break;
                }
                if(field.isOnField(c, r - 1) && ptField[c][r - 1] == ptField[c][r] && connectNum[c][r - 1] == 3)
                {
                    roundToN(c, r, 4);
                    roundToN(c, r - 1, 4);
                    break;
                }
                roundToN(c, r, 3);
            }

        }

        return connectNum;
    }

    public void setAPuyoType(PuyoType puyotype, int x, int y)
    {
        if(field.isOnField(x, y))
            ptField[x][y] = puyotype;
    }

  

 
    public int getTopPoint(int column)
    {
        int top;
        for(top = 0; ptField[column][top] != null; top++);
        return --top;
    }

  

    private void roundToN(int c, int r, int n)
    {
        connectNum[c][r] = n;
        if(field.isOnField(c - 1, r) && ptField[c - 1][r] == ptField[c][r])
            connectNum[c - 1][r] = n;
        if(field.isOnField(c, r - 1) && ptField[c][r - 1] == ptField[c][r])
            connectNum[c][r - 1] = n;
        if(field.isOnField(c + 1, r) && ptField[c + 1][r] == ptField[c][r])
            connectNum[c + 1][r] = n;
        if(field.isOnField(c, r + 1) && ptField[c][r + 1] == ptField[c][r])
            connectNum[c][r + 1] = n;
    }

    int nextNum[][];
    int connectNum[][];
    Field field;
    PuyoType ptField[][];
}
