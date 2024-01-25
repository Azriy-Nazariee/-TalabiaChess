public class Plus extends Piece {
    public Plus(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.YELLOW) {
            image = getImage("res/YPlus");
        } else {
            image = getImage("res/BPlus");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {

            // Move only horizontally or vertically without any distance limit
            if (targetCol == preCol || targetRow == preRow) {
                // Check if there are no pieces in the path of the move
                if (isPathClear(targetCol, targetRow)) {
                    return isValidSquare(targetCol, targetRow);
                }
            }
        }

        return false;
    }

    private boolean isPathClear(int targetCol, int targetRow) {
        // Check if there are no pieces in the path (excluding the start and end
        // squares)
        if (targetCol == preCol) {
            // Vertical movement
            int startRow = Math.min(preRow, targetRow) + 1;
            int endRow = Math.max(preRow, targetRow) - 1;

            for (Piece piece : GamePanel.otherpieces) {
                if (piece.col == targetCol && piece.row >= startRow && piece.row <= endRow) {
                    return false; // There is a piece in the path
                }
            }
        } else if (targetRow == preRow) {
            // Horizontal movement
            int startCol = Math.min(preCol, targetCol) + 1;
            int endCol = Math.max(preCol, targetCol) - 1;

            for (Piece piece : GamePanel.otherpieces) {
                if (piece.row == targetRow && piece.col >= startCol && piece.col <= endCol) {
                    return false; // There is a piece in the path
                }
            }
        }

        return true; // Path is clear
    }
}