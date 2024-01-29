import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {

    private static JFrame window;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        window = new JFrame("Talabia Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            Image icon = ImageIO.read(new File("res/icon.png"));
            window.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Main Menu
        JPanel mainMenu = new JPanel(new BorderLayout());
        mainMenu.setPreferredSize(new Dimension(400, 300)); // Set preferred size

        JLabel titleLabel = new JLabel("Talabia Chess", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainMenu.add(titleLabel, BorderLayout.NORTH);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 50)); // Add padding

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Add padding

        // Add Buttons
        JButton startButton = new JButton("Start Game");
        JButton loadButton = new JButton("Load Game");
        JButton quitButton = new JButton("Quit Game");

        buttonPanel.add(startButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(quitButton);

        mainMenu.add(buttonPanel, BorderLayout.CENTER);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(window);
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame(window);
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        window.add(mainMenu);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static void startGame(JFrame window) {
        GamePanel gp = new GamePanel();
        window.getContentPane().removeAll();
        window.add(gp);
        window.pack();
        window.repaint();
        gp.launchGame();
    }

    private static void loadGame(JFrame window) {
        GamePanel load = new GamePanel();
        LoadGame lg = new LoadGame(load, "save.txt");
        GamePanel.pieces = lg.getLoadedPieces(); // Load the pieces immediately after creating the LoadGame object
        load.setLoadGame(true); // Set loadGame to true

        window.getContentPane().removeAll();
        window.add(load);

        window.pack();
        window.repaint();
        load.launchGame();

        ImageIcon oriLoad = new ImageIcon("res/load.png");
        Image originalLoad = oriLoad.getImage();
        Image scaledLoad = originalLoad.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledLoad);
        JOptionPane.showMessageDialog(null, "Your saved game progress is loaded successfully!" + "\n" + "This current turn is: "
                + (load.currentColor == 0 ? "Yellow" : "Blue"), "Load Game", JOptionPane.INFORMATION_MESSAGE, scaledIcon);
    }

    // switch the window to the main menu
    public static void switchToMainMenu() {
        window.dispose();
        createAndShowGUI();
    }

    // get the current window
    public static JFrame getWindow() {
        return window;
    }
}
