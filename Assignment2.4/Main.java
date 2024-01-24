import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame window = new JFrame("Talabia Chess");
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
}
