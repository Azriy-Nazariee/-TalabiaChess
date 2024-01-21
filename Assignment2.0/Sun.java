public class Sun extends Piece {
    public Sun(int color, int col, int row){
        super(color, col, row);

        if(color == GamePanel.YELLOW){
            image = getImage("res/YSun");
        }

        else{
            image = getImage("res/BSun");
        }

        
    }

    public boolean canMove(int targetCol, int targetRow){
        return false;
    }
    
}
