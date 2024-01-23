public class HourGlass extends Piece {
    public HourGlass(int color, int col, int row){
        super(color, col, row);

        if(color == GamePanel.YELLOW){
            image = getImage("res/YHourGlass");
        }

        else{
            image = getImage("res/BHourGlass");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow)) {
            int colDiff = Math.abs(targetCol - preCol);
            int rowDiff = Math.abs(targetRow - preRow);
    
            // Check if the move forms a 3x2 L shape
            if ((colDiff == 1 && rowDiff == 2) || (colDiff == 2 && rowDiff == 1)) {
                // Check if the destination square is a valid square for the move
                if (isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }
        }
    
        return false;
    }
}
    
