import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame(){

        ImageIcon logo = new ImageIcon("src/logo.png");
        this.add(new GamePanel());
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(logo.getImage());
    }

}
