package com.platformer.gamestate;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Standard lifecycle callbacks for game states.
 */
public interface Statemethods {

    /** Updates the state logic. */
    void update();

    /**
     * Draws the state.
     *
     * @param g target graphics context
     */
    void draw(Graphics g);

    /**
     * Handles a mouse click event.
     *
     * @param e mouse event
     */
    void mouseClicked(MouseEvent e);

    /**
     * Handles a mouse press event.
     *
     * @param e mouse event
     */
    void mousePressed(MouseEvent e);

    /**
     * Handles a mouse release event.
     *
     * @param e mouse event
     */
    void mouseReleased(MouseEvent e);

    /**
     * Handles a mouse move event.
     *
     * @param e mouse event
     */
    void mouseMoved(MouseEvent e);

}
