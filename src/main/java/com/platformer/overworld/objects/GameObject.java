package com.platformer.overworld.objects;

import static com.platformer.overworld.utils.Constants.ANI_SPEED;
import static com.platformer.overworld.utils.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.platformer.core.Game;

/**
 * Base class for overworld objects with animation and hitbox data.
 */
public class GameObject {

    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    /**
     * @param x world x
     * @param y world y
     * @param objType object type id
     */
    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objType)) {
                aniIndex = 0;
                if (objType == BARREL || objType == BOX) {
                    doAnimation = false;
                    active = false;
                } else if (objType == CANNON_LEFT || objType == CANNON_RIGHT) {
                    doAnimation = false;
                }
            }
        }
    }

    /**
     * Resets animation state and activation flags.
     */
    public void reset() {
        aniIndex = 0;
        aniTick = 0;
        active = true;

        if (objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    /**
     * Draws the object's hitbox for debugging.
     *
     * @param g graphics context
     * @param xLvlOffset current camera offset
     */
    public void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    /**
     * @return object type id
     */
    public int getObjType() {
        return objType;
    }

    /**
     * @return object hitbox
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * @return true if the object is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active new active state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @param doAnimation whether to play animation
     */
    public void setAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }

    /**
     * @return x draw offset
     */
    public int getxDrawOffset() {
        return xDrawOffset;
    }

    /**
     * @return y draw offset
     */
    public int getyDrawOffset() {
        return yDrawOffset;
    }

    /**
     * @return animation frame index
     */
    public int getAniIndex() {
        return aniIndex;
    }

    /**
     * @return animation tick counter
     */
    public int getAniTick() {
        return aniTick;
    }

}
