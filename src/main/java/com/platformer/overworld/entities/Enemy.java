package com.platformer.overworld.entities;

import static com.platformer.overworld.utils.Constants.EnemyConstants.*;
import static com.platformer.overworld.utils.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import com.platformer.overworld.states.Playing;

import static com.platformer.overworld.utils.Constants.Directions.*;
import static com.platformer.overworld.utils.Constants.*;

import com.platformer.core.Game;

/**
 * Base class for overworld enemies with shared AI helpers.
 */
public abstract class Enemy extends Entity {

    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    protected int attackBoxOffsetX;

    /**
     * @param x world x
     * @param y world y
     * @param width sprite width
     * @param height sprite height
     * @param enemyType constant enemy type id
     */
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    /**
     * Positions the attack box relative to the hitbox.
     */
    protected void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    /**
     * Positions the attack box based on facing direction.
     */
    protected void updateAttackBoxFlip() {
        if (walkDir == RIGHT) {
            attackBox.x = hitbox.x + hitbox.width;
        } else {
            attackBox.x = hitbox.x - attackBoxOffsetX;
        }

        attackBox.y = hitbox.y;
    }

    /**
     * Initializes the attack box dimensions and offset.
     *
     * @param w width
     * @param h height
     * @param attackBoxOffsetX offset in pixels
     */
    protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
        attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
        this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
    }

    /**
     * Performs first update checks for air state.
     *
     * @param lvlData level collision data
     */
    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    /**
     * Handles in-air physics and environment hazards.
     *
     * @param lvlData level collision data
     * @param playing playing state
     */
    protected void inAirChecks(int[][] lvlData, Playing playing) {
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouched(this);
            if (IsEntityInWater(hitbox, lvlData)) {
                hurt(maxHealth);
            }
        }
    }

    /**
     * Updates vertical movement while in the air.
     *
     * @param lvlData level collision data
     */
    protected void updateInAir(int[][] lvlData) {
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    /**
     * Moves horizontally and flips direction on collision.
     *
     * @param lvlData level collision data
     */
    protected void move(int[][] lvlData) {
        float xSpeed = 0;

        if (walkDir == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    /**
     * Turns to face the player.
     *
     * @param player player instance
     */
    protected void turnTowardsPlayer(Player player) {
        if (player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    /**
     * @param lvlData level collision data
     * @param player player instance
     * @return true if the player is visible in line of sight
     */
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param player player instance
     * @return true if player is within sight range
     */
    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    /**
     * @param player player instance
     * @return true if player is close enough to attack
     */
    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        switch (enemyType) {
            case CRABBY -> {
                return absValue <= attackDistance;
            }
            case SHARK -> {
                return absValue <= attackDistance * 2;
            }
        }
        return false;
    }

    /**
     * Applies damage and updates state.
     *
     * @param amount damage amount
     */
    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            newState(DEAD);
        } else {
            newState(HIT);
            if (walkDir == LEFT) {
                pushBackDir = RIGHT;
            } else {
                pushBackDir = LEFT;
            }
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    /**
     * Checks if attack box hits the player.
     *
     * @param attackBox attack bounds
     * @param player player instance
     */
    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitbox)) {
            player.changeHealth(-GetEnemyDmg(enemyType), this);
        } else {
            if (enemyType == SHARK) {
                return;
            }
        }
        attackChecked = true;
    }

    /**
     * Advances animation frames based on state.
     */
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                if (enemyType == CRABBY || enemyType == SHARK) {
                    aniIndex = 0;

                    switch (state) {
                        case ATTACK, HIT ->
                            state = IDLE;
                        case DEAD ->
                            active = false;
                    }
                } else if (enemyType == PINKSTAR) {
                    if (state == ATTACK) {
                        aniIndex = 3;
                    } else {
                        aniIndex = 0;
                        if (state == HIT) {
                            state = IDLE;

                        } else if (state == DEAD) {
                            active = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Flips walking direction.
     */
    protected void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    /**
     * Resets enemy to its initial spawn state.
     */
    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;

        pushDrawOffset = 0;

    }

    /** @return x flip offset for drawing */
    public int flipX() {
        if (walkDir == RIGHT) {
            return width;
        } else {
            return 0;
        }
    }

    /** @return width multiplier for drawing */
    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        } else {
            return 1;
        }
    }

    /** @return true if enemy is active */
    public boolean isActive() {
        return active;
    }

    /** @param b new active flag */
    public void setActive(boolean b) {
        this.active = b;
    }

    /** @return current knockback draw offset */
    public float getPushDrawOffset() {
        return pushDrawOffset;
    }

}
