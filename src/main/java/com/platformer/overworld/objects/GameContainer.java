package com.platformer.overworld.objects;

import static com.platformer.overworld.utils.Constants.ObjectConstants.*;

import com.platformer.core.Game;

/**
 * Breakable container (box or barrel) that can drop items.
 */
public class GameContainer extends GameObject {

    /**
     * @param x world x
     * @param y world y
     * @param objType container type id
     */
    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    private void createHitbox() {
        if (objType == BOX) {
            initHitbox(25, 18);

            xDrawOffset = (int) (7 * Game.SCALE);
            yDrawOffset = (int) (12 * Game.SCALE);

        } else {
            initHitbox(23, 25);
            xDrawOffset = (int) (8 * Game.SCALE);
            yDrawOffset = (int) (5 * Game.SCALE);
        }

        hitbox.y += yDrawOffset + (int) (Game.SCALE * 2);
        hitbox.x += xDrawOffset / 2;
    }

    /**
     * Updates animation while the container is breaking.
     */
    public void update() {
        if (doAnimation) {
            updateAnimationTick();
        }
    }
}
