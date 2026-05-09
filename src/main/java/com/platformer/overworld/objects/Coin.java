package com.platformer.overworld.objects;

import com.platformer.core.Game;
import static com.platformer.utils.Constants.ObjectConstants.*;

public class Coin extends GameObject {

    private static final int COIN_VALUE = 5;

    public Coin(int x, int y) {
        super(x, y, COIN);
        initHitbox(12, 12);
        xDrawOffset = (int)(2 * Game.SCALE);
        yDrawOffset = (int)(2 * Game.SCALE);
        hitbox.x += xDrawOffset;
        hitbox.y += yDrawOffset;
    }

    public void update() {
        updateAnimationTick();
    }

    public int getValue() {
        return COIN_VALUE;
    }
}