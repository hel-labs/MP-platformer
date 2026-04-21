package com.platformer.gamestate;

import com.platformer.core.Game;
import com.platformer.input.InputHandler;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TitleScreen extends State implements Statemethods {

    private static final String TITLE_TEXT = "MY PLATFORMER";
    private static final String ENTER_TEXT = "Press ENTER to Start";

    public TitleScreen(Game game) {
        super(game);
    }

    @Override
    public void update() {
        InputHandler input = game.getInputHandler();
        if (input.isJustPressed(InputHandler.ENTER)) {
            Gamestate.state = Gamestate.MENU;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        Font titleFont = new Font("Arial", Font.BOLD, (int) (40 * Game.SCALE));
        Font enterFont = new Font("Arial", Font.PLAIN, (int) (20 * Game.SCALE));

        g.setFont(titleFont);
        drawCenteredString(g, TITLE_TEXT, Game.GAME_HEIGHT / 3);

        g.setFont(enterFont);
        drawCenteredString(g, ENTER_TEXT, (int) (Game.GAME_HEIGHT / 1.6));
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (Game.GAME_WIDTH - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
    }
}
