//  Made and Edited By :
//  Mohd Azriy Akmalhazim Bin Mohd Nazariee

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadGame {

    private static ArrayList<Piece> loadedPiecesYellow;
    private static ArrayList<Piece> loadedPiecesBlue;
    private static ArrayList<Piece> pieces;
    private static int turnCounter;
    private static boolean flipBoard;
    private static boolean loadGame;
    private static int currentColor;
    private static boolean pieceFlipped;

    public LoadGame(GamePanel gamePanel, String filePath) {
        loadedPiecesYellow = new ArrayList<>();
        loadedPiecesBlue = new ArrayList<>();
        pieces = new ArrayList<>(); // Initialize pieces
        loadGame(filePath);

        // Add the pieces to the pieces list
        for (Piece piece : loadedPiecesYellow) {
            pieces.add(piece);
        }
        for (Piece piece : loadedPiecesBlue) {
            pieces.add(piece);
        }

        // Setting the gamePanel variables and also the Pieces flipped variable
        gamePanel.setLoadGame(loadGame);
        gamePanel.setTurnCounter(turnCounter);
        gamePanel.setFlipBoard(flipBoard);
        gamePanel.setCurrentColor(currentColor);
        gamePanel.setPieceFlipped(pieceFlipped);
    }

    public static void loadGame(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read turn counter and flip board status
            String line = br.readLine();
            String[] turnInfo = line.split(": ");
            turnCounter = Integer.parseInt(turnInfo[1].trim());

            line = br.readLine();
            String[] flipInfo = line.split(": ");
            flipBoard = Boolean.parseBoolean(flipInfo[1].trim());

            // for load game
            line = br.readLine();
            String[] loadInfo = line.split(": ");
            loadGame = Boolean.parseBoolean(loadInfo[1].trim());

            // for current color
            line = br.readLine();
            String[] colorInfo = line.split(": ");
            currentColor = Integer.parseInt(colorInfo[1].trim());

            // for piece flipped
            line = br.readLine();
            String[] pieceState = line.split(": ");
            pieceFlipped = Boolean.parseBoolean(pieceState[1].trim());

            // Read and discard the header line for the pieces data
            br.readLine();

            // Read and parse each piece line
            while ((line = br.readLine()) != null) {
                String[] pieceInfo = line.split(",");
                int color = Integer.parseInt(pieceInfo[1]);
                int col = Integer.parseInt(pieceInfo[2]);
                int row = Integer.parseInt(pieceInfo[3]);
                // Add the piece to the appropriate list
                Piece piece = null; // Initialize piece variable

                if ("Point".equals(pieceInfo[0])) {
                    boolean isReversed = Boolean.parseBoolean(pieceInfo[4]);
                    piece = new Point(color, col, row, isReversed, flipBoard);
                } else if ("Time".equals(pieceInfo[0])) {
                    piece = new Time(color, col, row);
                } else if ("HourGlass".equals(pieceInfo[0])) {
                    piece = new HourGlass(color, col, row);
                } else if ("Plus".equals(pieceInfo[0])) {
                    piece = new Plus(color, col, row);
                } else if ("Sun".equals(pieceInfo[0])) {
                    piece = new Sun(color, col, row);
                }

                piece.setFlipped(pieceFlipped);

                // Add the piece to the appropriate list
                if (color == GamePanel.YELLOW) {
                    loadedPiecesYellow.add(piece);
                } else if (color == GamePanel.BLUE) {
                    loadedPiecesBlue.add(piece);
                }
            }

            System.out.println("Game loaded successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public ArrayList<Piece> getLoadedPieces() {
        return pieces;
    }
}
