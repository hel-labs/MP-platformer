package com.echoes.animation;

import java.awt.image.BufferedImage;

/**
 * Holds one array of frames and advances through them over time.
 *
 * frameDuration  — seconds each frame is visible (e.g. 0.1f = 10fps)
 * looping        — true: loops back to frame 0; false: freezes on last frame
 */
public class Animation {

    private final BufferedImage[] frames;
    private final float frameDuration;
    private final boolean looping;

    private float   timer        = 0f;
    private int     currentFrame = 0;
    private boolean finished     = false;

    /**
     * @param frames        array of frames sliced from a SpriteSheet
     * @param frameDuration seconds per frame  (0.08f = fast, 0.25f = slow)
     * @param looping       true for continuous animations (idle, run),
     *                      false for one-shot animations (attack, hurt, death)
     */
    public Animation(BufferedImage[] frames,
                     float frameDuration,
                     boolean looping) {
        if (frames == null || frames.length == 0)
            throw new IllegalArgumentException("Animation requires at least one frame");
        this.frames        = frames;
        this.frameDuration = frameDuration;
        this.looping       = looping;
    }

    /**
     * Advance the animation by delta time (seconds since last frame).
     * Call once per game tick.
     */
    public void update(float dt) {
        if (finished) return;

        timer += dt;
        if (timer >= frameDuration) {
            timer -= frameDuration;
            currentFrame++;

            if (currentFrame >= frames.length) {
                if (looping) {
                    currentFrame = 0;           // loop
                } else {
                    currentFrame = frames.length - 1; // freeze on last
                    finished = true;
                }
            }
        }
    }

    /** Returns the frame that should be drawn this tick. */
    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    /** True when a one-shot animation has played its last frame. */
    public boolean isFinished() {
        return finished;
    }

    /** Resets to frame 0. Call this when switching back to this animation. */
    public void reset() {
        currentFrame = 0;
        timer        = 0f;
        finished     = false;
    }

    /** Jumps immediately to the last frame and marks as finished. */
    public void skipToEnd() {
        currentFrame = frames.length - 1;
        finished     = !looping;
    }

    public int getFrameCount() {
        return frames.length;
    }

    public boolean isLooping() {
        return looping;
    }
}
