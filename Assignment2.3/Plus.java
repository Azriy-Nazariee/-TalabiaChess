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
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow)== false) {

            // Move only horizontally or vertically without any distance limit
            if (targetCol == preCol || targetRow == preRow) {
                return isValidSquare(targetCol, targetRow);
            }
        }

        return false;
    }
}
