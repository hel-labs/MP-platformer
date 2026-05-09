package com.platformer.overworld.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.platformer.overworld.utils.LoadSave;
import static com.platformer.overworld.utils.Constants.UI.URMButtons.*;

/**
 * Pause/menu button using URM sprite atlas.
 */
public class UrmButton extends PauseButton {

    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    /**
     * @param x left x
     * @param y top y
     * @param width button width
     * @param height button height
     * @param rowIndex sprite row index
     */
    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }

    }

    /**
     * Updates hover/press frame index.
     */
    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }

    }

    /**
     * Draws the button.
     *
     * @param g graphics context
     */
    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    /**
     * Resets hover and pressed flags.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /** @return true if mouse is over the button */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /** @param mouseOver new hover state */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /** @return true if button is pressed */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /** @param mousePressed new pressed state */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

}
