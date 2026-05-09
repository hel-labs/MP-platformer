package com.platformer.overworld.entities;

import static com.platformer.overworld.utils.Constants.Directions.DOWN;
import static com.platformer.overworld.utils.Constants.Directions.LEFT;
import static com.platformer.overworld.utils.Constants.Directions.UP;
import static com.platformer.overworld.utils.HelpMethods.CanMoveHere;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.platformer.core.Game;

/**
 * Base class for overworld entities with shared physics and animation helpers.
 */
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;

    /**
     * @param x world x
     * @param y world y
     * @param width sprite width
     * @param height sprite height
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Updates the knockback draw offset animation.
     */
    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit) {
                pushBackOffsetDir = DOWN;
            }
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0) {
                pushDrawOffset = 0;
            }
        }
    }

    /**
     * Applies horizontal knockback if space is clear.
     *
     * @param pushBackDir direction to push
     * @param lvlData level collision data
     * @param speedMulti speed multiplier
     */
    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if (pushBackDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed * speedMulti;
        }
    }

    /**
     * Debug draw for the attack box.
     *
     * @param g graphics context
     * @param xLvlOffset level x offset
     */
    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    /**
     * Debug draw for the hitbox.
     *
     * @param g graphics context
     * @param xLvlOffset level x offset
     */
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    /**
     * Initializes the entity hitbox.
     *
     * @param width hitbox width
     * @param height hitbox height
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    /** @return entity hitbox */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /** @return current animation/state id */
    public int getState() {
        return state;
    }

    /** @return current animation frame index */
    public int getAniIndex() {
        return aniIndex;
    }

    /**
     * Switches to a new state and resets animation counters.
     *
     * @param state new state id
     */
    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }
}
