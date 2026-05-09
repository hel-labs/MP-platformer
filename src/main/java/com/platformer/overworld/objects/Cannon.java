package com.platformer.overworld.objects;

import com.platformer.core.Game;

/**
 * Cannon object that can animate and fire projectiles.
 */
public class Cannon extends GameObject {

    private int tileY;

    /**
     * @param x world x
     * @param y world y
     * @param objType cannon type id
     */
    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
//		hitbox.x -= (int) (1 * Game.SCALE);
        hitbox.y += (int) (6 * Game.SCALE);
    }

    /**
     * Updates animation if firing.
     */
    public void update() {
        if (doAnimation) {
            updateAnimationTick();
        }
    }

    /**
     * @return tile row for the cannon
     */
    public int getTileY() {
        return tileY;
    }

}
