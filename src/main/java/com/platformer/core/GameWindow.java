package com.platformer.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

/**
 * Swing window wrapper that hosts the game panel.
 */
public class GameWindow {

    private final JFrame jframe;

    /**
     * Creates and displays the main game window.
     *
     * @param gamePanel panel to render inside the window
     */
    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);

        jframe.setContentPane(gamePanel);
        applyWindowedSize();
        jframe.setVisible(true);

        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
            }
        });
    }

    /**
     * Toggles fullscreen mode.
     *
     * @param fullscreen true to enter fullscreen
     */
    public void setFullscreen(boolean fullscreen) {

        jframe.dispose();

        if (fullscreen) {
            jframe.setUndecorated(true);
            jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            jframe.setUndecorated(false);
            applyWindowedSize();
        }

        jframe.setVisible(true);
    }

    private void applyWindowedSize() {
        jframe.setExtendedState(JFrame.NORMAL);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
    }
}
