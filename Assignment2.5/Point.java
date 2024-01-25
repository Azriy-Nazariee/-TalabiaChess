public class Point extends Piece {

    private boolean isReversed = false;

    public Point(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.YELLOW) {
            image = getImage("res/YPoint");
        }

        else {
            image = getImage("res/BPoint");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if ((color == GamePanel.YELLOW && preRow == 0) || (color == GamePanel.BLUE && preRow == 5)) {
            isReversed = true;
        }

        if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
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

        return false;
    }
}
