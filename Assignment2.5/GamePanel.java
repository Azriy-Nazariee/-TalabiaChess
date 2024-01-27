import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.BorderLayout;

public class GamePanel extends JPanel implements Runnable {
    private int turnCounter = 0;

    public static final int WIDTH = 700;
    public static final int HEIGHT = 620;
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
    boolean pieceFlipped = false;

    //Flipped Indicator
    private boolean flipBoard = false;

    // Load Indicator
    private boolean loadGame = false;

    // getter for loadGame
    public boolean getLoadGame() {
        return loadGame;
    }

    // setter for loadGame
    public void setLoadGame(boolean loadGame) {
        this.loadGame = loadGame;
    }

    // JButton declaration
    JButton saveButton;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setLayout(new BorderLayout());
        addMouseMotionListener(mouseAction);
        addMouseListener(mouseAction);
        
        // JButton initialization
        saveButton = new JButton("Save Game Progress");
        saveButton.addActionListener(e -> {
            // save progress
            SaveGame saveGame = new SaveGame(turnCounter, flipBoard, currentColor, loadGame, otherpieces);
            saveGame.saveToTxtFile("save.txt");
            JOptionPane.showMessageDialog(this, "Game Saved!");
        });

        // Add saveButton to the panel

        add(saveButton, BorderLayout.SOUTH);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setPieces() {
        // Yellow
        pieces.add(new Point(YELLOW, 0, 4, false, false));
        pieces.add(new Point(YELLOW, 1, 4, false, false));
        pieces.add(new Point(YELLOW, 2, 4, false, false));
        pieces.add(new Point(YELLOW, 3, 4, false, false));
        pieces.add(new Point(YELLOW, 4, 4, false, false));
        pieces.add(new Point(YELLOW, 5, 4, false, false));
        pieces.add(new Point(YELLOW, 6, 4, false, false));

        pieces.add(new Plus(YELLOW, 0, 5));
        pieces.add(new Plus(YELLOW, 6, 5));

        pieces.add(new HourGlass(YELLOW, 1, 5));
        pieces.add(new HourGlass(YELLOW, 5, 5));

        pieces.add(new Time(YELLOW, 2, 5));
        pieces.add(new Time(YELLOW, 4, 5));

        pieces.add(new Sun(YELLOW, 3, 5));

        // Blue
        pieces.add(new Point(BLUE, 0, 1, false, false));
        pieces.add(new Point(BLUE, 1, 1, false, false));
        pieces.add(new Point(BLUE, 2, 1, false, false));
        pieces.add(new Point(BLUE, 3, 1, false, false));
        pieces.add(new Point(BLUE, 4, 1, false, false));
        pieces.add(new Point(BLUE, 5, 1, false, false));
        pieces.add(new Point(BLUE, 6, 1, false, false));

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

        if(loadGame){
            //load game
            LoadGame loadGame = new LoadGame(this, "save.txt");
            copyPieces(loadGame.getLoadedPieces(), otherpieces);
        } else{
            setPieces();
            copyPieces(pieces, otherpieces);
        }

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;

                try {
                    // This to stop pieces flickering
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            checkAndEndGame();
        }
    }

    private void update() {
        // Mouse pressed
        if (mouseAction.pressed) {
            if (activePiece == null) {

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

        if (!mouseAction.pressed && activePiece != null) {
            if (activePiece != null) {
                if (validSquare) {

                    // Update position if a piece is captured
                    copyPieces(otherpieces, pieces);
                    activePiece.updatePosition();

                    // Check for piece transformations after 2 turns
                    if (++turnCounter == 2) {
                        transformPieces();
                        turnCounter = 0;
                    }

                    changeTurn();

                } else {
                    // Restore original position
                    copyPieces(otherpieces, pieces);

                    activePiece.resetPosition();
                    activePiece = null;

                }
            }
        }

    }

    private void transformPieces() {

        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);

            if (piece instanceof Time) {
                // Transform Time to Plus
                pieces.set(i, new Plus(piece.color, piece.col, piece.row));
            } else if (piece instanceof Plus) {
                // Transform Plus to Time
                pieces.set(i, new Time(piece.color, piece.col, piece.row));
            }
        }

        copyPieces(pieces, otherpieces);
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

        //boolean yellowTurn = false;
        //JOptionPane.showMessageDialog(this, (yellowTurn ? "Blue" : "Yellow")+ " turn's is over. "+ (!yellowTurn ? "Blue" : "Yellow")+ "'s turn starts now.");

        if (currentColor == YELLOW) {
            currentColor = BLUE;
            flipBoard = true;
        } else {
            currentColor = YELLOW;
            flipBoard = false;
        }
        flipPiece();

        activePiece = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Board
        if (flipBoard) {
            board.drawFlipped(g2);
        } else {
            board.draw(g2);
        }
        //board.draw(g2);

        // PIECES
        for (int i = 0; i < otherpieces.size(); i++) {
            Piece p = otherpieces.get(i);
            if (p != null) {
                p.draw(g2);
            }
        }

        // Null check activePiece
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

    public SaveGame saveGame() {
        return new SaveGame(turnCounter, flipBoard, currentColor, loadGame, otherpieces);
    }

    private void checkAndEndGame() {
        boolean yellowSunExists = false;
        boolean blueSunExists = false;

        for (Piece piece : otherpieces) {
            if (piece instanceof Sun) {
                if (piece.color == YELLOW) {
                    yellowSunExists = true;
                } else if (piece.color == BLUE) {
                    blueSunExists = true;
                }
            }
        }

        // If either Sun is missing, end the game
        if (!yellowSunExists || !blueSunExists) {
            // Show a dialog box to announce i) the game end ii) which sun is captured iii)
            // who is the winner
            JOptionPane.showMessageDialog(this, "Game Over! " + (!yellowSunExists ? "Blue" : "Yellow") + " wins!");
            gameThread = null;
        }
    }

    public void flipPiece() {
        pieceFlipped = !pieceFlipped;

        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);

            if (piece instanceof Point) {
                pieces.set(i, new Point(piece.color, reverse(piece.col,'c'), reverse(piece.row,'r'),piece.isReversed, pieceFlipped));
                piece = pieces.get(i);
                piece.flipped = pieceFlipped;
            } else if (piece instanceof Plus) {
                pieces.set(i, new Plus(piece.color, reverse(piece.col,'c'), reverse(piece.row,'r')));
            } else if (piece instanceof HourGlass) {
                pieces.set(i, new HourGlass(piece.color, reverse(piece.col,'c'), reverse(piece.row,'r')));
            } else if (piece instanceof Time) {
                pieces.set(i, new Time(piece.color, reverse(piece.col,'c'), reverse(piece.row,'r')));
            } else if (piece instanceof Sun) {
                pieces.set(i, new Sun(piece.color, reverse(piece.col,'c'), reverse(piece.row,'r')));
            }
        }
        copyPieces(pieces, otherpieces);
    } 

    public int reverse(int num, char type){
        if(type == 'r'){
            return 5 - num;
        }
        else{
            return 6 - num;
        }
    }

    public void setFlipBoard(boolean flipBoard2) {
        flipBoard = flipBoard2;
    }

    public void setTurnCounter(int turnCounter2) {
        turnCounter = turnCounter2;
    }

}
