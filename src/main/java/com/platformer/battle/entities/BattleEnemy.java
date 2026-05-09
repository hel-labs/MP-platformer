package com.platformer.battle.entities;

import com.platformer.battle.animation.Animation;
import com.platformer.battle.animation.AnimationController;
import com.platformer.battle.animation.SpriteSheet;
import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.talk.TalkOption;
import java.awt.image.BufferedImage;

import java.util.List;

public abstract class BattleEnemy {

    protected int hp;
    protected int maxHp;
    protected int attack;
    protected boolean fleeAllowed = true;
    protected AnimationController animator;
    protected BufferedImage battleSprite;

    public abstract String getName();

    public abstract String getEncounterDialogue();

    public abstract int getBaseHostility();

    public abstract List<TalkOption> getTalkOptions(int talkCount);

    public abstract DamageStrategy getDamageStrategy();

    public boolean isMercyReady(BattleContext ctx) {
        return ctx.isMercyAvailable();
    }

    public BufferedImage getBattleSprite() {
        if (animator != null) {
            BufferedImage frame = animator.getCurrentFrame();
            if (frame != null) {
                return frame;
            }
        }
        return battleSprite;
    }

    public void updateAnimation(float dt) {
        if (animator != null) {
            animator.update(dt);
            if ("attack".equals(animator.getCurrentKey()) && animator.currentFinished() && animator.has("idle")) {
                animator.play("idle");
            }
        }
    }

    protected void initBattleAnimation(String resourcePath,
            int frameWidth,
            int frameHeight,
            int row,
            int frameCount,
            float frameDuration) {
        SpriteSheet sheet = new SpriteSheet(resourcePath, frameWidth, frameHeight);
        animator = new AnimationController();
        animator.addAnimation("idle", new Animation(sheet.getRow(row, frameCount), frameDuration, true));
        animator.play("idle");
    }

    protected void initBattleAnimation(String resourcePath,
            int frameWidth,
            int frameHeight,
            int idleRow,
            int idleFrames,
            float idleFrameDuration,
            int attackRow,
            int attackFrames,
            float attackFrameDuration) {
        SpriteSheet sheet = new SpriteSheet(resourcePath, frameWidth, frameHeight);
        animator = new AnimationController();
        animator.addAnimation("idle", new Animation(sheet.getRow(idleRow, idleFrames), idleFrameDuration, true));
        animator.addAnimation("attack", new Animation(sheet.getRow(attackRow, attackFrames), attackFrameDuration, false));
        animator.play("idle");
    }

    public void playAttackAnimation() {
        if (animator != null && animator.has("attack")) {
            animator.forceReplay("attack");
        }
    }

    public String getMercyHint(BattleContext ctx) {
        return "* " + getName() + " is still hostile. ("
                + ctx.getHostility() + " remaining)";
    }

    public void onSpared() {
        hp = 0;
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public boolean isDefeated() {
        return hp <= 0;
    }

    public boolean isFleeAllowed() {
        return fleeAllowed;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }
}
