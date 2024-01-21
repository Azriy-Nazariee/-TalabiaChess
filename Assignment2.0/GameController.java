import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GameController {

    private GamePanel gamePanel;

    public GameController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        initializeListeners();
    }

    private void initializeListeners() {
        MouseAction mouseAction = new MouseAction(gamePanel);
        gamePanel.addMouseMotionListener((MouseMotionListener) mouseAction);
        gamePanel.addMouseListener((MouseListener) mouseAction);
    }

    public void handleInput() {
        // Handle user input logic here
    }
}
