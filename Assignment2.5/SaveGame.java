import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveGame {
    private int turnCounter;
    private boolean flipBoard;
    private ArrayList<Piece> savedPieces;
    private boolean loadGame;

    public SaveGame(int turnCounter, boolean flipBoard, int currentColor, boolean loadGame, ArrayList<Piece> savedPieces) {
        this.turnCounter = turnCounter;
        this.flipBoard = flipBoard;
        this.savedPieces = savedPieces;
        this.loadGame = loadGame;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public boolean isFlipBoard() {
        return flipBoard;
    }

    public ArrayList<Piece> getSavedPieces() {
        return savedPieces;
    }

    // Save data to a text file
    public void saveToTxtFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Turn Counter: " + turnCounter);
            writer.newLine();
            writer.write("Flip Board: " + flipBoard);
            writer.newLine();
            writer.write("Load Game: " + loadGame);
            writer.newLine();

            // Write each piece to a line using toString()
            for (Piece piece : savedPieces) {
                writer.write(piece.toString());
                writer.newLine();
            }

            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
