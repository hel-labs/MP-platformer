package com.echoes.entities;

import com.echoes.animation.Animation;
import com.echoes.animation.SpriteSheet;
import com.echoes.battle.strategies.DamageStrategy;
import com.echoes.battle.strategies.StandardDice;

import java.awt.*;

/**
 * The player entity.
 *
 * For the standalone battle engine this class only needs:
 *   - HP / maxHp / attack stats
 *   - DamageStrategy for fight action rolls
 *   - Battle animations (idle, attack, hurt, death)
 *   - A render method for the battle screen
 *
 * Overworld physics (velocity, gravity, collision) will be added
 * when integrating with the platformer. Those fields are stubbed here.
 *
 * PLACEHOLDER SPRITES:
 *   Drop your real spritesheet at src/main/resources/sprites/player_battle.png
 *   Row 0 — idle   (2 frames, 0.3s each, looping)
 *   Row 1 — attack (5 frames, 0.08s each, one-shot)
 *   Row 2 — hurt   (3 frames, 0.08s each, one-shot)
 *   Row 3 — death  (4 frames, 0.12s each, one-shot)
 */
public class Player extends Entity {

    // --- Battle config ---
    private final DamageStrategy damageStrategy;

    // --- Hurt flash state ---
    private float hurtTimer     = 0f;
    private float invincTimer   = 0f;
    private static final float HURT_DURATION   = 0.15f;
    private static final float INVINC_DURATION = 1.0f;

    // --- Battle animation state ---
    private boolean facingRight = true;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public Player() {
        // Starting stats
        this.hp        = 80;
        this.maxHp     = 80;
        this.attack    = 10;
        this.width     = 48;
        this.height    = 64;
        this.x         = 100f;
        this.y         = 200f;

        this.damageStrategy = new StandardDice();

        loadAnimations();
    }

    // ---------------------------------------------------------------
    // Animation setup
    // ---------------------------------------------------------------

    private void loadAnimations() {
        // PLACEHOLDER: path points to a sprite that may not exist yet.
        // SpriteSheet will return a placeholder colored grid if the file
        // is missing — the game will not crash.
        SpriteSheet sheet = new SpriteSheet(
                "/sprites/player_battle.png", 48, 64);

        animator.addAnimation("idle",
            new Animation(sheet.getRow(0, 2), 0.3f, true));
        animator.addAnimation("attack",
            new Animation(sheet.getRow(1, 5), 0.08f, false));
        animator.addAnimation("hurt",
            new Animation(sheet.getRow(2, 3), 0.08f, false));
        animator.addAnimation("death",
            new Animation(sheet.getRow(3, 4), 0.12f, false));

        animator.play("idle");
    }

    // ---------------------------------------------------------------
    // Update
    // ---------------------------------------------------------------

    @Override
    public void update(float dt) {
        // Tick hurt and invincibility timers
        if (hurtTimer   > 0) hurtTimer   -= dt;
        if (invincTimer > 0) invincTimer  -= dt;

        // Pick animation based on state
        if (defeated) {
            animator.play("death");
        } else if (hurtTimer > 0) {
            animator.play("hurt");
        } else {
            // Return to idle after attack or hurt finishes
            if (animator.currentFinished()) {
                animator.play("idle");
            }
        }

        animator.update(dt);
    }

    // ---------------------------------------------------------------
    // Render
    // ---------------------------------------------------------------

    @Override
    public void render(Graphics2D g) {
        // Hurt flash — alternate visibility every 0.05s during iframes
        if (invincTimer > 0 && ((int)(invincTimer / 0.05f) % 2 == 0)) {
            return; // blink invisible
        }

        drawAnimatedFrame(g, new Color(70, 130, 180), facingRight);
    }

    // ---------------------------------------------------------------
    // Combat overrides
    // ---------------------------------------------------------------

    @Override
    public void takeDamage(int amount) {
        if (invincTimer > 0) return; // iframes active — ignore hit
        super.takeDamage(amount);
        hurtTimer   = HURT_DURATION;
        invincTimer = INVINC_DURATION;
        if (!defeated) {
            animator.forcePlay("hurt");
        }
    }

    // ---------------------------------------------------------------
    // Battle-specific
    // ---------------------------------------------------------------

    /** Trigger the attack animation. Called by FightAction. */
    public void playAttackAnimation() {
        animator.forcePlay("attack");
    }

    /** True when the attack animation has finished playing. */
    public boolean attackAnimationFinished() {
        return animator.getCurrentKey().equals("attack")
            && animator.currentFinished();
    }

    public DamageStrategy getDamageStrategy() {
        return damageStrategy;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
}
