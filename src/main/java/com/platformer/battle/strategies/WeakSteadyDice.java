package com.echoes.battle.strategies;

/**
 * Weak but guaranteed: always deals exactly half attackStat (minimum 1).
 * No randomness. Good for weak enemies that are annoying but not dangerous.
 */
public class WeakSteadyDice implements DamageStrategy {

    @Override
    public int roll(int attackStat) {
        return Math.max(1, attackStat / 2);
    }

    @Override
    public String describe() {
        return "struck steadily";
    }
}
