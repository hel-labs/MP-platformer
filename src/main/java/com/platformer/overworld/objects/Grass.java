package com.platformer.overworld.objects;

/**
 * Decorative grass tile used for foreground details.
 */
public class Grass {

    private int x, y, type;

    /**
     * @param x world x
     * @param y world y
     * @param type grass variant id
     */
    public Grass(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
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
     * @return grass type id
     */
    public int getType() {
        return type;
    }
}
