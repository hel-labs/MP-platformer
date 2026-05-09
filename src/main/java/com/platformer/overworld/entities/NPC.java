package com.platformer.overworld.entities;

import com.platformer.core.Game;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class NPC {

    private float x, y;
    private int width, height;
    private Rectangle2D.Float hitbox;
    private BufferedImage[] frames;
    private int aniIndex, aniTick;
    private static final int ANI_SPEED = 10;
    private static final int FRAME_COUNT = 5;
    private static final int PROXIMITY_RANGE = (int) (Game.TILES_SIZE * 1.5f);
    private boolean active = false;

    public NPC(float x, float y, BufferedImage[] frames) {
        this.x = x;
        this.y = y;
        this.frames = frames;
        this.width = (int) (32 * Game.SCALE);
        this.height = (int) (32 * Game.SCALE);
        this.hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    public void update() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex = (aniIndex + 1) % FRAME_COUNT;
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!active) return;
        g.drawImage(frames[aniIndex],
                (int) (hitbox.x - xLvlOffset),
                (int) hitbox.y,
                width, height, null);
    }

    public boolean isPlayerNear(Rectangle2D.Float playerHitbox) {
        float dx = (playerHitbox.x + playerHitbox.width / 2) - (hitbox.x + hitbox.width / 2);
        float dy = (playerHitbox.y + playerHitbox.height / 2) - (hitbox.y + hitbox.height / 2);
        return Math.abs(dx) <= PROXIMITY_RANGE && Math.abs(dy) <= PROXIMITY_RANGE;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
}