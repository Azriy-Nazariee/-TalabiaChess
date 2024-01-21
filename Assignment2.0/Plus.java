public class Plus extends Piece {
    public Plus(int color, int col, int row){
        super(color, col, row);

        if(color == GamePanel.YELLOW){
            image = getImage("res/YPlus");
        }

        else{
            image = getImage("res/BPlus");
        }
    }
    
}
