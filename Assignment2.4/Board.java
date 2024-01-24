import java.awt.Color;
import java.awt.Graphics2D;

public class Board {

    final int MAX_COL= 7;
    final int MAX_ROW= 6;

    public static final int SQUARE_SIZE = 100; 
    public static final int HALF_SQUARE_SIZE = SQUARE_SIZE/2; 

    public void draw(Graphics2D g2){
        int c=0;
        for(int row = 0; row< MAX_ROW; row++){
            for(int col = 0; col< MAX_COL; col++){
                if(c==0){
                    g2.setColor(Color.white);
                    c= 1;
                }
                else{
                    g2.setColor(Color.gray);
                    c= 0;
                }
                g2.fillRect(col*SQUARE_SIZE, row*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
}