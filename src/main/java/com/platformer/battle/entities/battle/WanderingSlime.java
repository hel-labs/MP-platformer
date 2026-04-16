package com.echoes.entities.enemies;

import com.echoes.animation.Animation;
import com.echoes.animation.SpriteSheet;
import com.echoes.battle.BattleContext;
import com.echoes.battle.strategies.DamageStrategy;
import com.echoes.battle.strategies.WeakSteadyDice;
import com.echoes.entities.Enemy;
import com.echoes.entities.Player;
import com.echoes.riddle.Riddle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * WanderingSlime — the tutorial battle enemy.
 *
 * Intended to teach the player Talk → Spare without being dangerous.
 * Uses WeakSteadyDice (always half attack, never random) so damage is
 * predictable and new players are not punished for learning.
 *
 * Mercy condition: talk to it twice.
 *
 * PLACEHOLDER SPRITES:
 *   Drop your spritesheet at src/main/resources/sprites/slime_battle.png
 *   Frame size: 48 × 48 pixels
 *   Row 0 — idle     (3 frames, 0.25s, looping)
 *   Row 1 — attack   (4 frames, 0.1s,  one-shot)
 *   Row 2 — hurt     (3 frames, 0.08s, one-shot)
 *   Row 3 — defeated (4 frames, 0.12s, one-shot)
 *
 *   Battle display sprite (large, single frame):
 *   src/main/resources/sprites/slime_display.png  (128 × 128 recommended)
 */
public class WanderingSlime extends Enemy {

    private static final int MERCY_TALK_THRESHOLD = 2;
    private final DamageStrategy damageStrategy = new WeakSteadyDice();

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public WanderingSlime(float x, float y) {
        this.x      = x;
        this.y      = y;
        this.hp     = 20;
        this.maxHp  = 20;
        this.attack = 4;
        this.width  = 32;
        this.height = 28;

        // Wandering enemies do not block path — they roam
        this.blocking    = false;
        this.fleeAllowed = true;

        loadAnimations();
    }

    // ---------------------------------------------------------------
    // Routing
    // ---------------------------------------------------------------

    @Override
    public boolean isBattleEnemy() { return true; }

    // ---------------------------------------------------------------
    // Identity
    // ---------------------------------------------------------------

    @Override
    public String getName() { return "Wandering Slime"; }

    // ---------------------------------------------------------------
    // Battle contract
    // ---------------------------------------------------------------

    @Override
    public String getEncounterDialogue() {
        return "* A Wandering Slime blocks your path!\n"
             + "* It wobbles menacingly.";
    }

    @Override
    public String getTalkResponse(int talkCount) {
        return switch (talkCount) {
            case 0 -> "* The slime jiggles nervously.\n"
                    + "  It doesn't seem hostile.";
            case 1 -> "* The slime makes a soft bubbling sound.\n"
                    + "  It looks like it just wants a friend.";
            default -> "* The slime seems completely at ease now.\n"
                     + "  It wouldn't hurt anyone.";
        };
    }

    @Override
    public boolean isMercyReady(BattleContext ctx) {
        return ctx.getTalkCount() >= MERCY_TALK_THRESHOLD;
    }

    @Override
    public String getMercyHint(BattleContext ctx) {
        int remaining = MERCY_TALK_THRESHOLD - ctx.getTalkCount();
        return "* The slime is still wary of you.\n"
             + "  (Try talking "
             + remaining + " more time"
             + (remaining == 1 ? "" : "s") + "...)";
    }

    @Override
    public void onSpared() {
        setDefeated(true);
    }

    @Override
    public DamageStrategy getDamageStrategy() { return damageStrategy; }

    @Override
    public String getBattleMusic() {
        return "/audio/music/battle_normal.wav";
    }

    // ---------------------------------------------------------------
    // Riddle contract — not used for battle enemies, safe no-ops
    // ---------------------------------------------------------------

    @Override
    public String getBlockingDialogue() { return ""; }

    @Override
    public Riddle getRiddle() { return null; }

    @Override
    public String getSuccessDialogue() { return ""; }

    @Override
    public String getFailureDialogue() { return ""; }

    @Override
    public void onCorrect() {}

    @Override
    public void onIncorrect(Player player) {}

    // ---------------------------------------------------------------
    // Animation
    // ---------------------------------------------------------------

    private void loadAnimations() {
        // PLACEHOLDER: SpriteSheet returns a colored placeholder grid
        // if the file does not exist yet. Replace path when ready.
        SpriteSheet sheet = new SpriteSheet(
                "/sprites/slime_battle.png", 48, 48);

        animator.addAnimation("idle",
            new Animation(sheet.getRow(0, 3), 0.25f, true));
        animator.addAnimation("attack",
            new Animation(sheet.getRow(1, 4), 0.10f, false));
        animator.addAnimation("hurt",
            new Animation(sheet.getRow(2, 3), 0.08f, false));
        animator.addAnimation("defeated",
            new Animation(sheet.getRow(3, 4), 0.12f, false));

        animator.play("idle");
    }

    // ---------------------------------------------------------------
    // Update and render
    // ---------------------------------------------------------------

    @Override
    public void update(float dt) {
        if (defeated) {
            animator.play("defeated");
        } else {
            if (animator.currentFinished()) {
                animator.play("idle");
            }
        }
        animator.update(dt);
    }

    @Override
    public void render(Graphics2D g) {
        drawAnimatedFrame(g, new Color(100, 200, 100), true);
    }

    /**
     * Large battle display sprite shown on the left of the battle screen.
     * Returns null here — BattleUI will draw a green oval placeholder.
     * Replace by loading /sprites/slime_display.png in this method.
     */
    @Override
    public BufferedImage getBattleSprite() {
        return null; // PLACEHOLDER — replace with real sprite
    }
}
