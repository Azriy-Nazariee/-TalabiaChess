import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 512, 512);
        JPanel pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                int width = getWidth() / 7;
                int height = getHeight() / 6;

                for (int x = 0; x < 7; x++) {
                    for (int y = 0; y < 6; y++) {
                        if ((x + y) % 2 == 0) {
                            g.setColor(Color.white);
                        } else {
                            g.setColor(Color.gray);
                        }
                        g.fillRect(x * width, y * height, width, height);
                    }
                }
            }
        };
        frame.add(pn);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}


