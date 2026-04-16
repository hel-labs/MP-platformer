package com.echoes.battle.strategies;

/**
 * Strategy interface for damage calculation.
 * Each enemy (and the player) carries one DamageStrategy.
 * The battle engine asks the attacker for its strategy, rolls, and applies.
 *
 * Adding a new damage behavior = adding one new class here.
 * The engine never changes.
 */
public interface DamageStrategy {

    /**
     * Roll and return a damage value.
     * @param attackStat the base attack stat of the attacker
     * @return damage to deal (always >= 0)
     */
    int roll(int attackStat);

    /**
     * Short human-readable description shown in battle log.
     * e.g. "rolled 1d6", "struck steadily"
     */
    String describe();
}
