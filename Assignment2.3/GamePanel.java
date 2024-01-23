import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;


import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    final int FPS = 60;

    Thread gameThread;
    Board board = new Board();
    MouseAction mouseAction = new MouseAction();

    // PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> otherpieces = new ArrayList<>();
    Piece activePiece;

    // COLOR

    public static final int YELLOW = 0;
    public static final int BLUE = 1;

    // INITIAL
    int currentColor = YELLOW;

    // Booleans
    boolean canMove;
    boolean validSquare;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouseAction);
        addMouseListener(mouseAction);

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
        // Mouse pressed
        if (mouseAction.pressed) {
            if (activePiece == null) {
                // If the activePiece is null, check if you can pick up a piece
                for (Piece piece : otherpieces) {
                    if (piece.color == currentColor &&
                            piece.col == mouseAction.x / Board.SQUARE_SIZE &&
                            piece.row == mouseAction.y / Board.SQUARE_SIZE) {
                        activePiece = piece;
                        return;
                    }
                }
            } else {
                // If the player is holding a piece, simulate the move
                simulate();
            }
        }

        // Reset activePiece when mouse is released
        // if (!mouseAction.pressed && activePiece != null) {
        if (mouseAction.pressed == false) {
            if (activePiece != null) {
                if (validSquare) {

                    // move made

                    // update position if a piece is captured
                    copyPieces(otherpieces, pieces);
                    activePiece.updatePosition();

                    changeTurn();

                } else {
                    // restore originalk position
                    copyPieces(otherpieces, pieces);

                    activePiece.resetPosition();
                    activePiece = null;

                }
            }

        }
    }

    private void simulate() {

        canMove = false;
        validSquare = false;

        // this will reset the pieces list in every loop and will restore the removed
        // piece
        copyPieces(pieces, otherpieces);

        if (activePiece != null) {
            activePiece.x = mouseAction.x - Board.HALF_SQUARE_SIZE;
            activePiece.y = mouseAction.y - Board.HALF_SQUARE_SIZE;

            activePiece.col = activePiece.getCol(activePiece.x);
            activePiece.row = activePiece.getRow(activePiece.y);

            if (activePiece.canMove(activePiece.col, activePiece.row)) {
                canMove = true;

                if (activePiece.GotAPiece != null) {
                    otherpieces.remove(activePiece.GotAPiece.getIndex());
                }

                validSquare = true;
            }
        }
    }

    private void changeTurn() {
        if (currentColor == YELLOW) {
            currentColor = BLUE;
        } else {
            currentColor = YELLOW;
        }
        activePiece = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
    
        // Board
        board.draw(g2);
    
        // PIECES
        for (int i = 0; i < otherpieces.size(); i++) {
            Piece p = otherpieces.get(i);
            if (p != null) {
                p.draw(g2);
            }
        }
    
        // Null check for activePiece
        if (activePiece != null) {
            if (canMove) {
                g2.setColor(Color.green);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
    
                int col = activePiece.col * Board.SQUARE_SIZE;
                int row = activePiece.row * Board.SQUARE_SIZE;
    
                g2.fillRect(col, row, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
    
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
    
            // Draw the active piece only if it's not null and has a valid image
            if (activePiece.image != null) {
                activePiece.draw(g2);
            }
        }
    }
}    