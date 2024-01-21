import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    private GameController gameController;

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    final int FPS = 60;

    Thread gameThread;
    Board board = new Board();
    MouseAction mouseAction = new MouseAction(null);

    // PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> otherpieces = new ArrayList<>();
    Piece activePiece;

    // COLOR
    public static final int YELLOW = 0;
    public static final int BLUE = 1;

    // INITIAL
    int currentColor = YELLOW;

    public GamePanel() {
        gameController = new GameController(this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener((MouseMotionListener) mouseAction);
        addMouseListener((MouseListener) mouseAction);

        setPieces();
        copyPieces(pieces, otherpieces);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setPieces() {
        // Yellow
        pieces.add(new Point(YELLOW, 0, 4));
        pieces.add(new Point(YELLOW, 1, 4));
        pieces.add(new Point(YELLOW, 2, 4));
        pieces.add(new Point(YELLOW, 3, 4));
        pieces.add(new Point(YELLOW, 4, 4));
        pieces.add(new Point(YELLOW, 5, 4));
        pieces.add(new Point(YELLOW, 6, 4));

        pieces.add(new Plus(YELLOW, 0, 5));
        pieces.add(new Plus(YELLOW, 6, 5));

        pieces.add(new HourGlass(YELLOW, 1, 5));
        pieces.add(new HourGlass(YELLOW, 5, 5));

        pieces.add(new Time(YELLOW, 2, 5));
        pieces.add(new Time(YELLOW, 4, 5));

        pieces.add(new Sun(YELLOW, 3, 5));

        // Blue
        pieces.add(new Point(BLUE, 0, 1));
        pieces.add(new Point(BLUE, 1, 1));
        pieces.add(new Point(BLUE, 2, 1));
        pieces.add(new Point(BLUE, 3, 1));
        pieces.add(new Point(BLUE, 4, 1));
        pieces.add(new Point(BLUE, 5, 1));
        pieces.add(new Point(BLUE, 6, 1));

        pieces.add(new Plus(BLUE, 0, 0));
        pieces.add(new Plus(BLUE, 6, 0));

        pieces.add(new HourGlass(BLUE, 1, 0));
        pieces.add(new HourGlass(BLUE, 5, 0));

        pieces.add(new Time(BLUE, 2, 0));
        pieces.add(new Time(BLUE, 4, 0));

        pieces.add(new Sun(BLUE, 3, 0));
    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }
    

    @Override
    public void run() {
        double drawInterval = 100000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        gameController.handleInput();
        // Mouse pressed
        if (mouseAction.pressed) {
            if (activePiece == null) {
                // Check for any piece at the clicked position
                for (Piece piece : otherpieces) {
                    if (piece.col == mouseAction.x / Board.SQUARE_SIZE &&
                            piece.row == mouseAction.y / Board.SQUARE_SIZE) {
                        activePiece = piece;
                        break; // Stop checking once a piece is found
                    }
                }
            } else {
                simulate();
            }
        }
    
        if (!mouseAction.pressed) {
            if (activePiece != null) {
                activePiece.updatePosition();
                activePiece = null;
            }
        }
    }
    

    private void simulate() {
        if (activePiece != null && activePiece instanceof HourGlass) {
            HourGlass hourGlass = (HourGlass) activePiece;
    
            // Check if the HourGlass has been clicked
            if (mouseAction.pressed && !hourGlass.hasBeenClicked()) {
                hourGlass.setClicked(true);
                System.out.println("HourGlass clicked!");
            }
    
            // Only allow movement if the HourGlass has been clicked
            if (hourGlass.hasBeenClicked()) {
                int targetCol = mouseAction.x / Board.SQUARE_SIZE;
                int targetRow = mouseAction.y / Board.SQUARE_SIZE;
    
                // Check if the movement is valid for a 3x2 L shape
                if (hourGlass.canMove(targetCol, targetRow)) {
                    activePiece.x = mouseAction.x - Board.HALF_SQUARE_SIZE;
                    activePiece.y = mouseAction.y - Board.HALF_SQUARE_SIZE;
    
                    activePiece.col = activePiece.getCol(activePiece.x);
                    activePiece.row = activePiece.getRow(activePiece.y);
                }
            }
        }
    }
    
    
    


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Board
        board.draw(g2);

        // PIECES
        for (Piece p : otherpieces) {
            p.draw(g2);
        }

        if (activePiece != null) {
            g2.setColor(Color.green);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

            g2.fillRect(activePiece.col * Board.SQUARE_SIZE, activePiece.row * Board.SQUARE_SIZE,
                    Board.SQUARE_SIZE, Board.SQUARE_SIZE);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // Draw the active piece in the end so that it would not be hidden by the board or the colored square
            activePiece.draw(g2);
        }
    }
}