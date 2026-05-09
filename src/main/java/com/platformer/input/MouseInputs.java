package com.platformer.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.platformer.gamestate.Gamestate;
import com.platformer.core.GamePanel;

/**
 * Routes mouse input to the active game state.
 */
public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    /**
     * Creates a mouse input router for the given game panel.
     *
     * @param gamePanel panel hosting the game
     */
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @SuppressWarnings("incomplete-switch")
    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING ->
                gamePanel.getGame().getPlaying().mouseDragged(e);
            case OPTIONS ->
                gamePanel.getGame().getGameOptions().mouseDragged(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    /** {@inheritDoc} */
    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU ->
                gamePanel.getGame().getMenu().mouseMoved(e);
            case PLAYING ->
                gamePanel.getGame().getPlaying().mouseMoved(e);
            case OPTIONS ->
                gamePanel.getGame().getGameOptions().mouseMoved(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU ->
                gamePanel.getGame().getMenu().mousePressed(e);
            case PLAYING ->
                gamePanel.getGame().getPlaying().mousePressed(e);
            case OPTIONS ->
                gamePanel.getGame().getGameOptions().mousePressed(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU ->
                gamePanel.getGame().getMenu().mouseReleased(e);
            case PLAYING ->
                gamePanel.getGame().getPlaying().mouseReleased(e);
            case OPTIONS ->
                gamePanel.getGame().getGameOptions().mouseReleased(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseEntered(MouseEvent e) {
        // Not In use
    }

    /** {@inheritDoc} */
    @Override
    public void mouseExited(MouseEvent e) {
        // Not In use
    }

    /** {@inheritDoc} */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.state) {
            case PLAYING ->
                gamePanel.getGame().getPlaying().mouseClicked(e);
            default -> {
            }
        }
    }

}
