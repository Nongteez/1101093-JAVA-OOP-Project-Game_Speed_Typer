import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

public class TypePanel {

    private TypeGame game;

    public TypePanel() {
        try {
            JFrame frame = new JFrame();
            frame.setSize(1000, 750); 
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            frame.setLocation(gd.getDisplayMode().getWidth() / 4, gd.getDisplayMode().getHeight() / 4);
            
            game = new TypeGame("res/words.txt");
            
            frame.setContentPane(game);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    public static void main(String[] args) {
        new TypePanel();
    }
}
