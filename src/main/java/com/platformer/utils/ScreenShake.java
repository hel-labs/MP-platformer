package com.platformer.utils;

import java.util.Random;

/**
 * Screen shake effect. Call trigger() when something impactful happens. Call
 * update(dt) each tick. Apply getOffsetX/Y to the Graphics2D transform before
 * rendering.
 */
public class ScreenShake {

    private float intensity = 0f;
    private float duration = 0f;
    private float timer = 0f;
    private final Random rand = new Random();

    /**
     * @param intensity max pixel offset (e.g. 6f for a solid hit)
     * @param duration how long the shake lasts in seconds (e.g. 0.3f)
     */
    public void trigger(float intensity, float duration) {
        this.intensity = intensity;
        this.duration = duration;
        this.timer = 0f;
    }

    public void update(float dt) {
        if (timer < duration) {
            timer += dt;
        }
    }

    public int getOffsetX() {
        return offset();
    }

    public int getOffsetY() {
        return offset();
    }

    private int offset() {
        if (timer >= duration || intensity == 0) {
            return 0;
        }
        float strength = intensity * (1f - timer / duration);
        return (int) (rand.nextGaussian() * strength);
    }

    public boolean isActive() {
        return timer < duration;
    }
}
