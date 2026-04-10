package com.echoes.entities;

import com.echoes.battle.BattleContext;
import com.echoes.battle.strategies.DamageStrategy;
import com.echoes.riddle.Riddle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Abstract base for all enemies.
 *
 * Every enemy must implement both the riddle contract (for gatekeeper bosses)
 * and the battle contract (for wandering enemies). Subclasses only need to
 * fill in the methods relevant to their type — the other methods return safe
 * defaults so the engine never crashes.
 *
 * Battle enemies override isBattleEnemy() to return true.
 * Riddle bosses leave it as false (default).
 *
 * PLACEHOLDER SPRITES:
 *   Each enemy subclass loads its own spritesheet.
 *   Battle sheet layout:
 *     Row 0 — idle     (2-3 frames, looping)
 *     Row 1 — attack   (4-5 frames, one-shot)
 *     Row 2 — hurt     (3 frames, one-shot)
 *     Row 3 — defeated (4-5 frames, one-shot)
 */
public abstract class Enemy extends Entity {

    // Overworld flags
    protected boolean blocking = false;

    // Battle flags
    protected boolean fleeAllowed = true;

    // ---------------------------------------------------------------
    // Identity
    // ---------------------------------------------------------------

    public abstract String getName();

    // ---------------------------------------------------------------
    // Routing — which state this enemy triggers
    // ---------------------------------------------------------------

    /** Override to true for enemies that trigger BattleState. */
    public boolean isBattleEnemy() { return false; }

    // ---------------------------------------------------------------
    // Riddle contract — implemented by gatekeeper bosses
    // ---------------------------------------------------------------

    public abstract String getBlockingDialogue();
    public abstract Riddle getRiddle();
    public abstract String getSuccessDialogue();
    public abstract String getFailureDialogue();
    public abstract void onCorrect();
    public abstract void onIncorrect(Player player);

    // ---------------------------------------------------------------
    // Battle contract — implemented by battle enemies
    // ---------------------------------------------------------------

    /** Opening line when battle begins. */
    public String getEncounterDialogue() {
        return "* " + getName() + " appeared!";
    }

    /**
     * Return a talk response based on how many times the player has talked.
     * Each call should return progressively warmer or more descriptive text.
     */
    public String getTalkResponse(int talkCount) {
        return "* " + getName() + " doesn't seem to want to talk.";
    }

    /**
     * True when the enemy is ready to be spared.
     * Check talk count, turn count, or any custom condition here.
     */
    public boolean isMercyReady(BattleContext ctx) {
        return false;
    }

    /**
     * Hint shown when Spare is attempted before mercy is ready.
     * Should guide the player toward the mercy condition without being obvious.
     */
    public String getMercyHint(BattleContext ctx) {
        return "* It doesn't seem like the right moment...";
    }

    /** Called when the player successfully spares this enemy. */
    public void onSpared() {
        setDefeated(true);
    }

    public DamageStrategy getDamageStrategy() {
        // Subclasses override with their own strategy
        return new DamageStrategy() {
    @Override
    public int roll(int attackStat) {
        return 0;
    }

    @Override
    public String describe() {
        return "does nothing";
    }
};
    }

    /**
     * Large battle sprite displayed on the left side of the battle screen.
     * Return null if not yet assigned — BattleUI will draw a placeholder.
     */
    public BufferedImage getBattleSprite() {
        return null;
    }

    /** Path to battle music. Override per enemy for unique BGM. */
    public String getBattleMusic() {
        return "/audio/music/battle_normal.wav";
    }

    // ---------------------------------------------------------------
    // Overworld flags
    // ---------------------------------------------------------------

    public boolean isBlocking()   { return blocking; }
    public boolean isFleeAllowed(){ return fleeAllowed; }

    public void setBlocking(boolean blocking)       { this.blocking = blocking; }
    public void setFleeAllowed(boolean fleeAllowed) { this.fleeAllowed = fleeAllowed; }
}
