package com.platformer.overworld.objects;

import java.util.Random;

/**
 * Decorative background tree with a subtle idle animation.
 */
public class BackgroundTree {

    private int x, y, type, aniIndex, aniTick;

    /**
     * @param x world x
     * @param y world y
     * @param type tree type id
     */
    public BackgroundTree(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        // Sets the aniIndex to a random value, to get some variations for the trees so
        // they all don't move in synch.
        Random r = new Random();
        aniIndex = r.nextInt(4);

    }

    /**
     * Advances the animation frame.
     */
    public void update() {
        aniTick++;
        if (aniTick >= 35) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4) {
                aniIndex = 0;
            }
        }
    }

    /**
     * @return animation frame index
     */
    public int getAniIndex() {
        return aniIndex;
    }

    /**
     * @param aniIndex new animation frame index
     */
    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    /**
     * @return world x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x new world x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return world y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y new world y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return tree type id
     */
    public int getType() {
        return type;
    }

    /**
     * @param type new tree type id
     */
    public void setType(int type) {
        this.type = type;
    }
}
