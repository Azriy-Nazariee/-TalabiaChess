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
}
