//Requirements
// 1. After 2 turns (counting one yellow move and one blue move as one turn), all Time pieces will turn into Plus pieces, and 
//   all Plus pieces will turn into Time pieces
// 2. program user friendly
// 3. with suitable menus,
// 4. save game (the game should be saved into a text file so that it’s human-readable.)
// 5. resizable windows
// 6. flipping screens when its other's players turn
//
//


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Talabia Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Main Menu
        JPanel mainMenu = new JPanel(new BorderLayout());
        mainMenu.setPreferredSize(new Dimension(400, 300)); // Set preferred size

        // Create a label
        JLabel titleLabel = new JLabel("Talabia Chess", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font and size
        mainMenu.add(titleLabel, BorderLayout.NORTH);

        // Create a sub-panel for the button with padding
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

        // ActionListener for Start Game button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and add GamePanel to the window
                GamePanel gp = new GamePanel();
                window.getContentPane().removeAll();
                window.add(gp);
                window.pack();
                window.repaint();
                gp.launchGame();
            }
        });

        // ActionListener for Quit Game button
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
}

