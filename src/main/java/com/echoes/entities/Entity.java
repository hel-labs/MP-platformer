package com.echoes.entities;

import com.echoes.animation.AnimationController;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abstract base for every living thing in the game.
 * Owns position, dimensions, HP, and an AnimationController.
 *
 * The battle engine only needs HP and attack from entities.
 * Rendering is handled by subclasses using their own spritesheets.
 */
public abstract class Entity {

    // --- Position and size (pixels) ---
    protected float x;
    protected float y;
    protected int   width;
    protected int   height;

    // --- Combat stats ---
    protected int hp;
    protected int maxHp;
    protected int attack;

    // --- State flags ---
    protected boolean active   = true;
    protected boolean defeated = false;

    // --- Animation ---
    protected final AnimationController animator = new AnimationController();

    // ---------------------------------------------------------------
    // Abstract methods — subclasses must implement
    // ---------------------------------------------------------------

    /**
     * Called once per game tick. Subclasses update physics, AI, animation state.
     * @param dt seconds since last tick
     */
    public abstract void update(float dt);

    /**
     * Render this entity using Graphics2D.
     * Subclasses draw their sprite or placeholder shape here.
     */
    public abstract void render(Graphics2D g);

    // ---------------------------------------------------------------
    // Shared combat logic
    // ---------------------------------------------------------------

    /**
     * Apply damage to this entity. HP is clamped to 0.
     * Subclasses can override to add hurt animation triggers.
     */
    public void takeDamage(int amount) {
        if (amount < 0) amount = 0;
        hp = Math.max(0, hp - amount);
        if (hp <= 0) {
            defeated = true;
            active   = false;
        }
    }

    /** Heal HP, clamped to maxHp. */
    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    // ---------------------------------------------------------------
    // Getters and setters
    // ---------------------------------------------------------------

    public float getX()     { return x; }
    public float getY()     { return y; }
    public int   getWidth() { return width; }
    public int   getHeight(){ return height; }

    public void setX(float x)  { this.x = x; }
    public void setY(float y)  { this.y = y; }
    public void setPosition(float x, float y) { this.x = x; this.y = y; }

    public int  getHp()     { return hp; }
    public int  getMaxHp()  { return maxHp; }
    public int  getAttack() { return attack; }

    public boolean isActive()   { return active; }
    public boolean isDefeated() { return defeated; }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
        if (defeated) this.active = false;
    }

    public AnimationController getAnimator() { return animator; }

    /**
     * Returns bounding rectangle in pixel space.
     * Used for collision detection in the overworld.
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /**
     * Helper: draw a colored placeholder rectangle.
     * Used by subclasses when their real sprite is not yet loaded.
     */
    protected void drawPlaceholder(Graphics2D g, Color color) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 9));
        g.drawString(getClass().getSimpleName(), (int) x + 2, (int) y + 12);
    }

    /**
     * Helper: draw a frame from the animator at this entity's position.
     * Handles null frame gracefully by falling back to placeholder.
     */
    protected void drawAnimatedFrame(Graphics2D g, Color fallbackColor,
                                     boolean facingRight) {
        BufferedImage frame = animator.getCurrentFrame();
        if (frame == null) {
            drawPlaceholder(g, fallbackColor);
            return;
        }
        if (facingRight) {
            g.drawImage(frame, (int) x, (int) y, width, height, null);
        } else {
            // Flip horizontally — no mirrored spritesheet needed
            g.drawImage(frame, (int) x + width, (int) y, -width, height, null);
        }
    }
}
