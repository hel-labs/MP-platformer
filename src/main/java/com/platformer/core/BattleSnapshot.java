package com.platformer.core;

/**
 * Immutable snapshot of player stats when entering battle.
 */
public class BattleSnapshot {

    /** Current HP at the time of snapshot. */
    public final int hp;
    /** Max HP at the time of snapshot. */
    public final int maxHp;
    /** Player attack power at the time of snapshot. */
    public final int attack;
    /** Current stamina at the time of snapshot. */
    public final int stamina;
    /** Max stamina at the time of snapshot. */
    public final int maxStamina;

    /**
     * Creates a snapshot with the given stats.
     *
     * @param hp current HP
     * @param maxHp max HP
     * @param attack attack power
     * @param stamina current stamina
     * @param maxStamina max stamina
     */
    public BattleSnapshot(int hp, int maxHp, int attack, int stamina, int maxStamina) {
        this.hp = hp;
        this.maxHp = maxHp;
        this.attack = attack;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
    }
}
