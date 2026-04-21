package com.platformer.battle.entities;

import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.strategies.StandardDice;
import com.platformer.core.BattleSnapshot;
import com.platformer.battle.animation.*;

public class BattlePlayer {

    private int hp;
    private int maxHp;
    private int attack;
    private int stamina;
    private int maxStamina;
    private final DamageStrategy damageStrategy = new StandardDice();
    private AnimationController animator;

    public BattlePlayer(BattleSnapshot snapshot) {
        this.hp = snapshot.hp;
        this.maxHp = snapshot.maxHp;
        this.attack = snapshot.attack;
        this.stamina = snapshot.stamina;
        this.maxStamina = snapshot.maxStamina;
    }

    public void initAnimations(SpriteSheet sheet) {
        this.animator = new AnimationController();
        animator.addAnimation("idle", new Animation(sheet.getRow(0, 5), 0.15f, true));
        animator.addAnimation("attack", new Animation(sheet.getRow(4, 3), 0.08f, false));
        animator.play("idle");
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void spendStamina(int amount) {
        stamina = Math.max(0, stamina - amount);
    }

    public void recoverStamina(int amount) {
        stamina = Math.min(maxStamina, stamina + amount);
    }

    public void playAttackAnimation() {
        if (animator != null) {
            animator.forceReplay("attack");
        }
    }

    public AnimationController getAnimator() {
        return animator;
    }

    public boolean isDefeated() {
        return hp <= 0;
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

    public int getStamina() {
        return stamina;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public DamageStrategy getDamageStrategy() {
        return damageStrategy;
    }
}
