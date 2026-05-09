package com.platformer.overworld.effects;

import static com.platformer.overworld.utils.Constants.ANI_SPEED;
import static com.platformer.overworld.utils.Constants.Dialogue.*;

/**
 * Animated popup for exclamation or question markers.
 */
public class DialogueEffect {

    private int x, y, type;
    private int aniIndex, aniTick;
    private boolean active = true;

    /**
     * @param x world x
     * @param y world y
     * @param type marker type
     */
    public DialogueEffect(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Advances the effect animation.
     */
    public void update() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(type)) {
                active = false;
                aniIndex = 0;
            }
        }
    }

    /**
     * Deactivates the effect.
     */
    public void deactive() {
        active = false;
    }

    /**
     * Resets the effect position and reactivates it.
     *
     * @param x world x
     * @param y world y
     */
    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        active = true;
    }

    /**
     * @return animation frame index
     */
    public int getAniIndex() {
        return aniIndex;
    }

    /**
     * @return world x
     */
    public int getX() {
        return x;
    }

    /**
     * @return world y
     */
    public int getY() {
        return y;
    }

    /**
     * @return marker type
     */
    public int getType() {
        return type;
    }

    /**
     * @return true if the effect is active
     */
    public boolean isActive() {
        return active;
    }
}
