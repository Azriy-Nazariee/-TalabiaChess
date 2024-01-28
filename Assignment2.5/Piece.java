import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Piece {
    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public Piece GotAPiece;
    public boolean flipped = false;
    public boolean isReversed = false;

    public Piece(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;

        x = getX(col);
        y = getY(row);

        preCol = col;
        preRow = row;
    }

    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public int getX(int col) {
        return col * Board.SQUARE_SIZE;
    }

    public int getY(int row) {
        return row * Board.SQUARE_SIZE;
    }

    // the getCol and getRow is used as indicator to its col and row based on its
    // center point
    public int getCol(int x) {
        return (x + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public int getRow(int y) {
        return (y + Board.HALF_SQUARE_SIZE) / Board.SQUARE_SIZE;
    }

    public int getIndex() {
        for (int index = 0; index < GamePanel.otherpieces.size(); index++) {
            if (GamePanel.otherpieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }

    public void updatePosition() {
        x = getX(col);
        y = getY(row);
        preCol = getCol(x);
        preRow = getRow(y);
    }

    public void resetPosition() {
        col = preCol;
        row = preRow;
        x = getX(col);
        y = getY(row);
    }

    public boolean canMove(int targetCol, int targetRow) {
        return false;
    }

    public boolean isWithinBoard(int targetCol, int targetRow) {
        if (targetCol >= 0 && targetCol < 7 && targetRow >= 0 && targetRow < 6) {
            return true;
        }
        return false;
    }

    public boolean isSameSquare(int targetCol, int targetRow) {
        if (targetCol == preCol && targetRow == preRow) {
            return true;
        }
        return false;
    }

    public Piece gotAllyPiece(int targetCol, int targetRow) {
        for (Piece piece : GamePanel.otherpieces) {
            if (piece.col == targetCol && piece.row == targetRow && piece != this) {
                return piece;
            }
        }
        return null;
    }

    public boolean isValidSquare(int targetCol, int targetRow) {

        GotAPiece = gotAllyPiece(targetCol, targetRow);

        if (GotAPiece == null) {// vacant
            return true;
        } else {
            if (GotAPiece.color != this.color) {// same color
                return true;
            } else {// different color
                GotAPiece = null;
            }
        }
        return false;
    }

    public boolean pieceOnStraightLine(int targetCol, int targetRow) {

        // left movement
        for (int c = preCol - 1; c > targetCol; c--) {
            for (Piece piece : GamePanel.otherpieces) {
                if (piece.col == c && piece.row == targetRow) {
                    GotAPiece = piece;
                    return true;
                }
            }
        }
        // right movement
        for (int c = preCol + 1; c < targetCol; c++) {
            for (Piece piece : GamePanel.otherpieces) {
                if (piece.col == c && piece.row == targetRow) {
                    GotAPiece = piece;
                    return true;
                }
            }
        }
        // up movement
        for (int r = preRow - 1; r > targetRow; r--) {
            for (Piece piece : GamePanel.otherpieces) {
                if (piece.col == targetCol && piece.row == r) {
                    GotAPiece = piece;
                    return true;
                }
            }
        }

        // down movement
        for (int r = preRow + 1; r < targetRow; r++) {
            for (Piece piece : GamePanel.otherpieces) {
                if (piece.col == targetCol && piece.row == r) {
                    GotAPiece = piece;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pieceOnDiagnolLine(int targetCol, int targetRow) {

        if (targetRow < preRow) {
            // up left movement
            for (int c = preCol - 1; c > targetCol; c--) {
                int dif = Math.abs(c - preCol);
                // check if there's a piece blocking the way
                for (Piece piece : GamePanel.otherpieces) {
                    // -diff because the row is decreasing hence going up(row starts from top to
                    // bot)
                    if (piece.col == c && piece.row == preRow - dif) {
                        GotAPiece = piece;
                        return true;
                    }
                }
            }

            // up right movement
            for (int c = preCol + 1; c < targetCol; c++) {
                int dif = Math.abs(c - preCol);
                // check if there's a piece blocking the way
                for (Piece piece : GamePanel.otherpieces) {
                    if (piece.col == c && piece.row == preRow - dif) {
                        GotAPiece = piece;
                        return true;
                    }
                }
            }

        }

        else {

            // down left movement
            for (int c = preCol - 1; c > targetCol; c--) {
                int dif = Math.abs(c - preCol);
                // check if there's a piece blocking the way
                for (Piece piece : GamePanel.otherpieces) {
                    // +diff because the row is increasing hence going down
                    if (piece.col == c && piece.row == preRow + dif) {
                        GotAPiece = piece;
                        return true;
                    }
                }
            }

            // down right movement
            for (int c = preCol + 1; c < targetCol; c++) {
                int dif = Math.abs(c - preCol);
                // check if there's a piece blocking the way
                for (Piece piece : GamePanel.otherpieces) {
                    // +diff because the row is increasing hence going down
                    if (piece.col == c && piece.row == preRow + dif) {
                        GotAPiece = piece;
                        return true;
                    }
                }
            }

        }
        return false;

    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }

    @Override
    public String toString() {
        return String.format("%s,%d,%d,%d,%b",
                this.getClass().getSimpleName(),
                this.color,
                this.col,
                this.row,
                // if piece is point, the isreversed info needs to be stored
                this.isReversed);
    }

    public void setFlipped(boolean flipped2) {
        flipped = flipped2;
    }

    public void setReversed(boolean isReversed2) {
        isReversed = isReversed2;
    }

}
