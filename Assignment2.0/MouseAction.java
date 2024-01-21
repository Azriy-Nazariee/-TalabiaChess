import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseAction extends MouseAdapter {

    int x;
    int y;
    boolean pressed;
    public MouseAction(GamePanel gamePanel) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;

        // Check if the mouse is pressed on an HourGlass piece
        for (Piece piece : GamePanel.otherpieces) {
            if (piece instanceof HourGlass && ((HourGlass) piece).hasBeenClicked()) {
                if (piece.col == x / Board.SQUARE_SIZE && piece.row == y / Board.SQUARE_SIZE) {
                    System.out.println("HourGlass clicked!");
                    // Handle any additional logic here if needed
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}
