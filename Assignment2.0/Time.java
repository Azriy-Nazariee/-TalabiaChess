public class Time extends Piece{
    public Time(int color, int col, int row){
        super(color, col, row);

        if(color == GamePanel.YELLOW){
            image = getImage("res/YTime");
        }

        else{
            image = getImage("res/BTime");
        }
    }
    
}
