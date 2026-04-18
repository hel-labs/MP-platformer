package com.platformer.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

public class GameWindow {

    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);

        jframe.add(gamePanel);

        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {}
        });
    }

    public void setFullscreen(boolean fullscreen) {

    jframe.dispose();

    if (fullscreen) {
        jframe.setUndecorated(true);
        jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
    } else {
        jframe.setUndecorated(false);
        jframe.setSize(Game.GAME_WIDTH, Game.GAME_HEIGHT);
        jframe.setLocationRelativeTo(null);
    }

    jframe.setVisible(true);
}
}