public class Point extends Piece {

    public Point(int color, int col, int row,boolean isReversed, boolean isFlipBoard) {
        super(color, col, row);
        this.isReversed = isReversed;
        if(isFlipBoard){
            if (color == GamePanel.YELLOW) {
                image = getImage("res/YPointR");
            }
    
            else {
                image = getImage("res/BPointR");
            }
        } else{
            if (color == GamePanel.YELLOW) {
                image = getImage("res/YPoint");
            }
    
            else {
                image = getImage("res/BPoint");
            }
        }
        
    }

    public boolean canMove(int targetCol, int targetRow) {
        if ((color == GamePanel.YELLOW && preRow == 0) || (color == GamePanel.YELLOW && preRow == 5) 
            || (color == GamePanel.BLUE && preRow == 0) || (color == GamePanel.BLUE && preRow == 5)) {
            isReversed = true;
        }
        
        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow) && !flipped) {
            // Move forward by 1 or 2 square(s) (depending on the color)
            int rowDifference = targetRow - preRow;
            int colDifference = targetCol - preCol;
            if ((color == GamePanel.YELLOW && !isReversed && (rowDifference == -1 || rowDifference == -2)) ||
                    (color == GamePanel.YELLOW && isReversed && (rowDifference == 1 || rowDifference == 2)) ||
                    (color == GamePanel.BLUE && !isReversed && (rowDifference == 1 || rowDifference == 2)) ||
                    (color == GamePanel.BLUE && isReversed && (rowDifference == -1 || rowDifference == -2))) {
                if (Math.abs(rowDifference) == 1 || Math.abs(rowDifference) == 2) {
                    if (Math.abs(colDifference) == 0 && isValidSquare(targetCol, targetRow)
                            && pieceOnStraightLine(targetCol, targetRow) == false) {
                        return true;
                    }
                }
            }
        }

        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow) && flipped) {
            // Move forward by 1 or 2 square(s) (depending on the color)
            int rowDifference = targetRow - preRow;
            int colDifference = targetCol - preCol;
            if ((color == GamePanel.YELLOW && !isReversed && (rowDifference == 1 || rowDifference == 2)) ||
                    (color == GamePanel.YELLOW && isReversed && (rowDifference == -1 || rowDifference == -2)) ||
                    (color == GamePanel.BLUE && !isReversed && (rowDifference == -1 || rowDifference == -2)) ||
                    (color == GamePanel.BLUE && isReversed && (rowDifference == 1 || rowDifference == 2))) {
                if (Math.abs(rowDifference) == 1 || Math.abs(rowDifference) == 2) {
                    if (Math.abs(colDifference) == 0 && isValidSquare(targetCol, targetRow)
                            && pieceOnStraightLine(targetCol, targetRow) == false) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
