package com.platformer.battle.animation;

import com.platformer.exceptions.DataException;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class AnimationController {

    private final Map<String, Animation> animations = new HashMap<>();
    private String currentKey = "";
    private Animation current = null;

    public void addAnimation(String key, Animation animation) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Animation Key can't be blank!");
        }
        animations.put(key, animation);
    }

    public void play(String key) {
        if (key.equals(currentKey)) {
            return;
        }
        if (!animations.containsKey(key)) {
            throw new DataException("Animation key not found");
        }
        currentKey = key;
        current = animations.get(key);
        current.reset();
    }

    public void forceReplay(String key) {
        if (!animations.containsKey(key)) {
            throw new DataException("Animation key not found");
        }
        currentKey = key;
        current = animations.get(key);
        current.reset();
    }

    public void update(float delta) {
        if (current != null) {
            current.update(delta);
        }
    }

    public BufferedImage getCurrentFrame() {
        return current != null ? current.getCurrentFrame() : null;
    }

    public boolean currentFinished() {
        return current != null && current.isFinished();
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public boolean has(String key) {
        return animations.containsKey(key);
    }
}
