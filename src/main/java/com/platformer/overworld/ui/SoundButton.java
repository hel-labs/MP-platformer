package com.platformer.overworld.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.platformer.overworld.utils.LoadSave;
import static com.platformer.overworld.utils.Constants.UI.PauseButtons.*;

/**
 * Toggle button for music or SFX mute states.
 */
public class SoundButton extends PauseButton {

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    /**
     * @param x left x
     * @param y top y
     * @param width button width
     * @param height button height
     */
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3];
        for (int j = 0; j < soundImgs.length; j++) {
            for (int i = 0; i < soundImgs[j].length; i++) {
                soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }

    /**
     * Updates sprite indices for current hover/press state.
     */
    public void update() {
        if (muted) {
            rowIndex = 1;
        } else {
            rowIndex = 0;
        }

        colIndex = 0;
        if (mouseOver) {
            colIndex = 1;
        }
        if (mousePressed) {
            colIndex = 2;
        }

    }

    /**
     * Resets hover and pressed flags.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Draws the button.
     *
     * @param g graphics context
     */
    public void draw(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
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

    /** @return true if muted */
    public boolean isMuted() {
        return muted;
    }

    /** @param muted new muted state */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

}
