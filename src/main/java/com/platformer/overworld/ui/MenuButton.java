package com.platformer.overworld.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.platformer.gamestate.Gamestate;
import com.platformer.overworld.utils.LoadSave;
import static com.platformer.overworld.utils.Constants.UI.Buttons.*;

/**
 * Menu button with hover/press states and target game state.
 */
public class MenuButton {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    /**
     * @param xPos center x
     * @param yPos top y
     * @param rowIndex sprite row index
     * @param state target state when clicked
     */
    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    /**
     * Draws the button.
     *
     * @param g graphics context
     */
    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    /**
     * Updates hover/press animation state.
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
     * @return true if mouse is over the button
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * @param mouseOver new hover state
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * @return true if the button is pressed
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * @param mousePressed new pressed state
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * @return button bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Applies the button's target game state.
     */
    public void applyGamestate() {
        Gamestate.state = state;
    }

    /**
     * Resets hover and pressed flags.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * @return target game state
     */
    public Gamestate getState() {
        return state;
    }

}
