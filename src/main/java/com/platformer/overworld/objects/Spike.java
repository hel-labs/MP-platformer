package com.platformer.overworld.objects;

import com.platformer.core.Game;

/**
 * Static spike trap that damages entities on contact.
 */
public class Spike extends GameObject {

    /**
     * @param x world x
     * @param y world y
     * @param objType spike type id
     */
    public Spike(int x, int y, int objType) {
        super(x, y, objType);
        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int) (Game.SCALE * 16);
        hitbox.y += yDrawOffset;
    }
}
