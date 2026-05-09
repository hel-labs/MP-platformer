package com.platformer.overworld.ui;

import java.awt.Rectangle;

/**
 * Base class for pause/menu buttons with bounds helpers.
 */
public class PauseButton {

    protected int x, y, width, height;
    protected Rectangle bounds;

    /**
     * @param x left x
     * @param y top y
     * @param width button width
     * @param height button height
     */
    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    /** @return x position */
    public int getX() {
        return x;
    }

    /** @param x new x position */
    public void setX(int x) {
        this.x = x;
    }

    /** @return y position */
    public int getY() {
        return y;
    }

    /** @param y new y position */
    public void setY(int y) {
        this.y = y;
    }

    /** @return width */
    public int getWidth() {
        return width;
    }

    /** @param width new width */
    public void setWidth(int width) {
        this.width = width;
    }

    /** @return height */
    public int getHeight() {
        return height;
    }

    /** @param height new height */
    public void setHeight(int height) {
        this.height = height;
    }

    /** @return button bounds */
    public Rectangle getBounds() {
        return bounds;
    }

    /** @param bounds new bounds */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

}
