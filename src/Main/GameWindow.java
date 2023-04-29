package Main;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class GameWindow {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel){
        jframe = new JFrame();

        jframe.add(gamePanel);
        jframe.pack();
        jframe.setLocationRelativeTo(null);

        //resize window
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                //gamePanel.getGame().windowFocusGained();
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}
