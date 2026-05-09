package com.platformer.overworld.objects;

import java.awt.geom.Rectangle2D;

import com.platformer.core.Game;

import static com.platformer.overworld.utils.Constants.Projectiles.*;

/**
 * Cannon projectile that moves horizontally until it hits something.
 */
public class Projectile {

    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;

    /**
     * @param x spawn x
     * @param y spawn y
     * @param dir direction (1 for right, -1 for left)
     */
    public Projectile(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (5 * Game.SCALE);

        if (dir == 1) {
            xOffset = (int) (29 * Game.SCALE);
        }

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    /**
     * Advances projectile position by its speed.
     */
    public void updatePos() {
        hitbox.x += dir * SPEED;
    }

    /**
     * Sets the projectile position.
     *
     * @param x new x
     * @param y new y
     */
    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    /**
     * @return projectile hitbox
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * @param active new active state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return true if the projectile is active
     */
    public boolean isActive() {
        return active;
    }

}
