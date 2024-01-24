public class Point extends Piece {
    public Point(int color, int col, int row){
        super(color, col, row);

        if(color == GamePanel.YELLOW){
            image = getImage("res/YPoint");
        }

        else{
            image = getImage("res/BPoint");
        }
    }

    public boolean canMove(int targetCol, int targetRow){
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
            // Move forward by 1 square (depending on the color)
            int rowDifference = targetRow - preRow;
            if (color == GamePanel.YELLOW && rowDifference == -1) {
                // Yellow pieces move towards the top of the board
                if (Math.abs(targetCol - preCol) + Math.abs(rowDifference) == 1 && isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            } else if (color == GamePanel.BLUE && rowDifference == 1) {
                // Blue pieces move towards the bottom of the board
                if (Math.abs(targetCol - preCol) + Math.abs(rowDifference) == 1 && isValidSquare(targetCol, targetRow)) {
                    return true;
                }
            }
        }

        return false;
    }
}
