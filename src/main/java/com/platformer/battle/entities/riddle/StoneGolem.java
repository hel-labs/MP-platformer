package com.echoes.entities.enemies;

import com.echoes.animation.Animation;
import com.echoes.animation.SpriteSheet;
import com.echoes.battle.BattleContext;
import com.echoes.battle.strategies.DamageStrategy;
import com.echoes.battle.strategies.StandardDice;
import com.echoes.entities.Enemy;
import com.echoes.entities.Player;
import com.echoes.riddle.Riddle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * StoneGolem — riddle gatekeeper boss, Zone 1.
 *
 * Blocks the path. Player must answer its riddle to pass.
 * Wrong answer pushes the player back and deals damage.
 * Correct answer moves the golem aside permanently.
 *
 * Not a battle enemy (isBattleEnemy returns false).
 *
 * PLACEHOLDER SPRITES:
 *   src/main/resources/sprites/stone_golem.png
 *   Frame size: 40 × 60 pixels
 *   Row 0 — idle / block stance (2 frames, 0.4s, looping)
 *   Row 1 — step aside         (4 frames, 0.1s, one-shot)
 *   Row 2 — react wrong answer (3 frames, 0.1s, one-shot)
 */
public class StoneGolem extends Enemy {

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public StoneGolem(float x, float y) {
        this.x      = x;
        this.y      = y;
        this.hp     = 999; // effectively unkillable in riddle context
        this.maxHp  = 999;
        this.attack = 8;
        this.width  = 40;
        this.height = 60;
        this.blocking    = true;
        this.fleeAllowed = false;

        loadAnimations();
    }

    // ---------------------------------------------------------------
    // Routing — riddle boss, not a battle enemy
    // ---------------------------------------------------------------

    @Override
    public boolean isBattleEnemy() { return false; }

    // ---------------------------------------------------------------
    // Identity
    // ---------------------------------------------------------------

    @Override
    public String getName() { return "Stone Golem"; }

    // ---------------------------------------------------------------
    // Riddle contract
    // ---------------------------------------------------------------

    @Override
    public String getBlockingDialogue() {
        return "* The golem shifts, filling the passage.\n"
             + "  \"Answer correctly... or go no further.\"";
    }

    @Override
    public Riddle getRiddle() {
        return new Riddle(
            "I have cities, but no houses live there.\n"
          + "I have mountains, but no trees grow.\n"
          + "I have water, but no fish swim.\n"
          + "What am I?",
            new String[]{
                "A dream",
                "A map",
                "A painting",
                "A mirror"
            },
            1 // correct: "A map"
        );
    }

    @Override
    public String getSuccessDialogue() {
        return "* The golem exhales slowly, stone grinding on stone.\n"
             + "  \"...Correct. You may pass.\"";
    }

    @Override
    public String getFailureDialogue() {
        return "* The golem shakes its great head.\n"
             + "  \"Incorrect. The way remains closed.\"";
    }

    @Override
    public void onCorrect() {
        this.blocking  = false;
        this.defeated  = false; // still alive, just moved
        this.active    = true;
        this.x        += 96;   // step aside — adjust per room layout
        animator.forcePlay("step_aside");
    }

    @Override
    public void onIncorrect(Player player) {
        // Push player back and deal damage
        player.takeDamage(new StandardDice().roll(attack));
        animator.forcePlay("react_wrong");
    }

    // ---------------------------------------------------------------
    // Battle contract — safe stubs (golem is not a battle enemy)
    // ---------------------------------------------------------------

    @Override
    public String getEncounterDialogue() { return ""; }

    @Override
    public String getTalkResponse(int talkCount) { return ""; }

    @Override
    public boolean isMercyReady(BattleContext ctx) { return false; }

    @Override
    public String getMercyHint(BattleContext ctx) { return ""; }

    @Override
    public DamageStrategy getDamageStrategy() {
        return new StandardDice();
    }

    @Override
    public BufferedImage getBattleSprite() { return null; }

    @Override
    public String getBattleMusic() { return ""; }

    // ---------------------------------------------------------------
    // Animation
    // ---------------------------------------------------------------

    private void loadAnimations() {
        SpriteSheet sheet = new SpriteSheet(
                "/sprites/stone_golem.png", 40, 60);

        animator.addAnimation("idle",
            new Animation(sheet.getRow(0, 2), 0.4f, true));
        animator.addAnimation("step_aside",
            new Animation(sheet.getRow(1, 4), 0.10f, false));
        animator.addAnimation("react_wrong",
            new Animation(sheet.getRow(2, 3), 0.10f, false));

        animator.play("idle");
    }

    // ---------------------------------------------------------------
    // Update and render
    // ---------------------------------------------------------------

    @Override
    public void update(float dt) {
        if (animator.currentFinished()) {
            animator.play("idle");
        }
        animator.update(dt);
    }

    @Override
    public void render(Graphics2D g) {
        drawAnimatedFrame(g, new Color(140, 130, 120), true);
    }
}
