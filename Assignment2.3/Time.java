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

    public boolean canMove(int targetCol, int targetRow){
        if(isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow)== false){
            //the x and y coordinate need to be the same as they are moving diagnolly
            if (Math.abs(targetCol-preCol) == Math.abs(targetRow-preRow)){
                    if(isValidSquare(targetCol, targetRow) && pieceOnDiagnolLine(targetCol, targetRow)==false){
                        return true;
                    }
            }

        }

        return false;
    }
    
}
