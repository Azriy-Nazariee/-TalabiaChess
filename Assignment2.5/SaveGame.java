import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveGame {
    private int turnCounter;
    private boolean flipBoard;
    private ArrayList<Piece> savedPieces;

    public SaveGame(int turnCounter, boolean flipBoard, int currentColor,ArrayList<Piece> savedPieces, boolean loadGame) {
        this.turnCounter = turnCounter;
        this.flipBoard = flipBoard;
        this.savedPieces = savedPieces;
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
            writer.write("Load Game: " + flipBoard);
            writer.newLine();

            // Save piece information
            for (Piece piece : savedPieces) {
                writer.write(piece.toString()); // You need to implement the toString() method in Piece
                writer.newLine();
            }

            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
