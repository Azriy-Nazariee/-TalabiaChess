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

        if(isWithinBoard(targetCol, targetRow)){
            if (Math.abs(targetCol-preCol) + Math.abs(targetRow-preRow)==1 
                || Math.abs(targetCol-preCol) * Math.abs(targetRow-preRow)==1){
                    if(isValidSquare(targetCol, targetRow)){
                        return true;
                    }
            }

        }
    
        return false;
    }
    
}
