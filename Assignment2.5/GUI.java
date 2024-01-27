import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GUI {
    public GUI() {
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

        // ActionListener for Load Game Button (Not Finished)
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePanel gp = new GamePanel();
                LoadGame lg = new LoadGame(gp, "save.txt");

                lg.getLoadedPieces();
                
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

    // close the window
    public static void close( JFrame window) {

        WindowEvent winClosingEvent = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);

    }
}
