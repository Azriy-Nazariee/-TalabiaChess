public class HourGlass extends Piece {
    private boolean hasBeenClicked = false;

    public HourGlass(int color, int col, int row) {
        super(color, col, row);

        if (color == GamePanel.YELLOW) {
            image = getImage("res/YHourGlass");
        } else {
            image = getImage("res/BHourGlass");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {
        if (!hasBeenClicked) {
            // HourGlass hasn't been clicked, disallow movement
            return false;
        }

        int colDiff = Math.abs(targetCol - col);
        int rowDiff = Math.abs(targetRow - row);

        // The HourGlass can move in a 3x2 L shape in any orientation, and it can skip over other pieces
        return (colDiff == 2 && rowDiff == 3) || (colDiff == 3 && rowDiff == 2);
    }


    public boolean hasBeenClicked() {
        return hasBeenClicked;
    }

    public void setClicked(boolean clicked) {
        hasBeenClicked = clicked;
    }
}