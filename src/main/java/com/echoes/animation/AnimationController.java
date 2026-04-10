package com.echoes.animation;

import com.echoes.exceptions.DataException;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages a named map of animations for one entity.
 *
 * Usage:
 *   controller.addAnimation("idle",   new Animation(...));
 *   controller.addAnimation("attack", new Animation(...));
 *
 *   // Each tick:
 *   controller.play("idle");      // switches if not already active
 *   controller.update(dt);        // advances current animation
 *   controller.getCurrentFrame(); // draw this
 */
public class AnimationController {

    private final Map<String, Animation> animations = new HashMap<>();
    private String    currentKey  = "";
    private Animation current     = null;

    /** Register a named animation. Call once during entity construction. */
    public void addAnimation(String key, Animation animation) {
        if (key == null || key.isBlank())
            throw new IllegalArgumentException("Animation key must not be blank");
        animations.put(key, animation);
    }

    /**
     * Switch to the named animation.
     * If this animation is already playing it is NOT restarted —
     * this prevents the animation resetting to frame 0 every tick.
     */
    public void play(String key) {
        if (key.equals(currentKey)) return; // already playing — do nothing
        if (!animations.containsKey(key))
            throw new DataException("Animation key not found: " + key);

        currentKey = key;
        current    = animations.get(key);
        current.reset();
    }

    /**
     * Force-play an animation even if it is already active.
     * Use this to restart a one-shot animation (e.g. replaying hurt).
     */
    public void forcePlay(String key) {
        if (!animations.containsKey(key))
            throw new DataException("Animation key not found: " + key);
        currentKey = key;
        current    = animations.get(key);
        current.reset();
    }

    /**
     * Advance the currently active animation.
     * Call once per game tick before rendering.
     */
    public void update(float dt) {
        if (current != null) current.update(dt);
    }

    /**
     * Returns the frame to draw this tick.
     * Returns null if no animation has been set yet.
     */
    public BufferedImage getCurrentFrame() {
        return current != null ? current.getCurrentFrame() : null;
    }

    /**
     * True when the current animation is a one-shot and has finished.
     * Use this to know when to advance battle phases after an attack animation.
     */
    public boolean currentFinished() {
        return current != null && current.isFinished();
    }

    /** Returns the key of the currently playing animation. */
    public String getCurrentKey() {
        return currentKey;
    }

    public boolean has(String key) {
        return animations.containsKey(key);
    }
}
