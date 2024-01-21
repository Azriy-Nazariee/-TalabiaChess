import javax.swing.JFrame;

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


public class Main {

    public static void main(String[] args){

        JFrame window = new JFrame("Talabia Chess");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        //add game panel to the window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }
}