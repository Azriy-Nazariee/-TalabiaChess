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
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import java.awt.Image;
public class GamePanel extends JPanel implements Runnable {
    private int turnCounter = 0;

    public static final int WIDTH = 700;
    public static final int HEIGHT = 650;
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

    // Flipped Indicator
    private boolean flipBoard = false;

    // Load Indicator
    private boolean loadGame = false;

    // JButton declaration
    JButton saveButton;

    public GamePanel() {

        // Setting up of the game panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setLayout(new BorderLayout());
        addMouseMotionListener(mouseAction);
        addMouseListener(mouseAction);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        // Create and configure the save button
        JButton saveButton = new JButton("Save Game Progress");
        saveButton.setPreferredSize(new Dimension(100, 50));
        saveButton.addActionListener(e -> {

            // Save progress Button
            SaveGame saveGame = new SaveGame(turnCounter, flipBoard, currentColor, loadGame, otherpieces, pieceFlipped);
            saveGame.saveToTxtFile("save.txt");
            ImageIcon oriSave = new ImageIcon("res/save.png");
            Image originalSave = oriSave.getImage();
            Image scaledSave = originalSave.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledSave);
            JOptionPane.showMessageDialog(this, "Game Saved Successfully!,", "Game Saved",
                    JOptionPane.INFORMATION_MESSAGE,scaledIcon);

            // Confimarion from user to continue or not
            int response = JOptionPane.showConfirmDialog(this, "Do you want to continue the game?",
                    "Want to Continue?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.NO_OPTION) {
                Main.switchToMainMenu();
            }
        });

        // Exit Button
        JButton exitButton = new JButton("Exit Current Game");
        exitButton.setPreferredSize(new Dimension(100, 50));
        exitButton.addActionListener(e -> {

            // Confimarion from user to exit or not
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the current game?",
                    "Confirm Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                Main.switchToMainMenu();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
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

        if (!loadGame) { // setting the pieces if the game is new
            // clear the board
            pieces.clear();
            setPieces();
        } else {
        }
        copyPieces(pieces, otherpieces);

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
                    // print breakpoint
                    changeTurn();
                    checkAndEndGame();

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
        // board.draw(g2);

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

    private void checkAndEndGame() {
        boolean yellowSunExists = false;
        boolean blueSunExists = false;

        // Create a copy of otherpieces
        ArrayList<Piece> otherpiecesChecking = new ArrayList<>(otherpieces);

        // Check if either Sun is missing
        for (Piece piece : otherpiecesChecking) {
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

            // Show a dialog box to announce the winner
            ImageIcon oriWin = new ImageIcon("Assignment2.5/res/Win.png");
            Image originalWin = oriWin.getImage();
            Image scaledIWin = originalWin.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledIWin);
            JOptionPane.showMessageDialog(this, "Game Over! " + (!yellowSunExists ? "Blue" : "Yellow") + " wins this game!", "Congratulations!",
                    JOptionPane.INFORMATION_MESSAGE,scaledIcon);
            gameThread = null;
            Main.switchToMainMenu();
        }
    }

    public void flipPiece() {
        pieceFlipped = !pieceFlipped;

        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);

            if (piece instanceof Point) {
                pieces.set(i, new Point(piece.color, reverse(piece.col, 'c'), reverse(piece.row, 'r'), piece.isReversed,
                        pieceFlipped));
                piece = pieces.get(i);
                piece.flipped = pieceFlipped;
            } else if (piece instanceof Plus) {
                pieces.set(i, new Plus(piece.color, reverse(piece.col, 'c'), reverse(piece.row, 'r')));
            } else if (piece instanceof HourGlass) {
                pieces.set(i, new HourGlass(piece.color, reverse(piece.col, 'c'), reverse(piece.row, 'r')));
            } else if (piece instanceof Time) {
                pieces.set(i, new Time(piece.color, reverse(piece.col, 'c'), reverse(piece.row, 'r')));
            } else if (piece instanceof Sun) {
                pieces.set(i, new Sun(piece.color, reverse(piece.col, 'c'), reverse(piece.row, 'r')));
            }
        }
        copyPieces(pieces, otherpieces);
    }

    public int reverse(int num, char type) {
        if (type == 'r') {
            return 5 - num;
        } else {
            return 6 - num;
        }
    }

    // setter and getter for load game mechanism

    public boolean getLoadGame() {
        return loadGame;
    }

    public void setLoadGame(boolean loadGame) {
        this.loadGame = loadGame;
    }

    public void setFlipBoard(boolean flipBoardSaved) {
        flipBoard = flipBoardSaved;
    }

    public void setTurnCounter(int turnCounterSaved) {
        turnCounter = turnCounterSaved;
    }

    public void setCurrentColor(int currentColorSaved) {
        currentColor = currentColorSaved;
    }

    public void setPieceFlipped(boolean pieceFlippedSaved) {
        pieceFlipped = pieceFlippedSaved;

        // if the pieces is points, set their flipped to true
        for (Piece piece : pieces) {
            if (piece instanceof Point) {
                piece.flipped = pieceFlipped;
            }
        }
    }
}
