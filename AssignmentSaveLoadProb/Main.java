import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main {

    private static JFrame window;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        window = new JFrame("Talabia Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Main Menu
        JPanel mainMenu = new JPanel(new BorderLayout());
        mainMenu.setPreferredSize(new Dimension(400, 300)); // Set preferred size

        JLabel titleLabel = new JLabel("Talabia Chess", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainMenu.add(titleLabel, BorderLayout.NORTH);

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

    // private static void loadGame(JFrame window) {
    //     GamePanel load = new GamePanel();        
    //     LoadGame lg = new LoadGame(load, "save.txt");
    //     GamePanel.pieces = lg.getLoadedPieces(); // Load the pieces immediately after creating the LoadGame object
    //     load.setLoadGame(true); // Set loadGame to true
    
    //     window.getContentPane().removeAll();
    //     window.add(load);

    //     window.pack();
    //     window.repaint();
    //     load.launchGame();

    //     JOptionPane.showMessageDialog(null, "Game loaded successfully!"+"\n"+"This current turn is: " + (load.currentColor == 0 ? "Yellow" : "Blue"));
    // }

    private static void loadGame(JFrame window) {
    // Create a JFileChooser to let the user choose the file to load
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File("SavedTalabia")); // Set the default directory
    
    int result = fileChooser.showOpenDialog(window);
    
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        
        // Get the absolute path of the selected file
        String filePath = selectedFile.getAbsolutePath();
        
        GamePanel load = new GamePanel();        
        LoadGame lg = new LoadGame(load, filePath);
        GamePanel.pieces = lg.getLoadedPieces(); // Load the pieces immediately after creating the LoadGame object
        load.setLoadGame(true); // Set loadGame to true

        window.getContentPane().removeAll();
        window.add(load);

        window.pack();
        window.repaint();
        load.launchGame();

        JOptionPane.showMessageDialog(null, "Game loaded successfully!" + "\n" +
                "This current turn is: " + (load.currentColor == 0 ? "Yellow" : "Blue"));
    }
}


    // switch the window to the main menu
    public static void switchToMainMenu() {
        window.dispose();
        createAndShowGUI();
    }

    //get the current window
    public static JFrame getWindow() {
        return window;
    }
}
