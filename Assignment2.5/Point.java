public class Point extends Piece {

    public Point(int color, int col, int row, boolean isReversed, boolean isFlipBoard) {
        super(color, col, row);
        this.isReversed = isReversed;
    
        if (isFlipBoard) {
            if (color == GamePanel.YELLOW) {
                image = getImage(isReversed ? "res/YPoint" : "res/YPointR");
            } else {
                image = getImage(isReversed ? "res/BPoint" : "res/BPointR");
            }
        } else {
            if (color == GamePanel.YELLOW) {
                image = getImage(isReversed ? "res/YPointR" : "res/YPoint");
            } else {
                image = getImage(isReversed ? "res/BPointR" : "res/BPoint");
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

    private boolean flippedtoNormal(int targetCol, int targetRow) {
        //check if the point returns to the original side's end of the board
        if ((color == GamePanel.YELLOW && targetRow == 0) ||  (color == GamePanel.BLUE && targetRow == 5)) {
            return false; // the point is no longer reversed, hence the isReversed is false
        }
        return true; //the point is still reversed, hence the isReversed is true
    }
}
