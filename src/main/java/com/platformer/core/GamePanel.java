package com.platformer.core;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import static com.platformer.core.Game.GAME_HEIGHT;
import static com.platformer.core.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

    private Game game;

    public GamePanel(Game game) {
        this.game = game;
        setPanelSize();
    }

    public void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        float scale = Math.min(
                (float) getWidth() / Game.GAME_WIDTH,
                (float) getHeight() / Game.GAME_HEIGHT
        );

        int xOffset = (int) ((getWidth() - Game.GAME_WIDTH * scale) / 2);
        int yOffset = (int) ((getHeight() - Game.GAME_HEIGHT * scale) / 2);

        g2d.translate(xOffset, yOffset);
        g2d.scale(scale, scale);

        game.render(g2d);
    }

    public Game getGame() {
        return game;
    }
}
