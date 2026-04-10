package com.echoes.battle.strategies;

import java.util.Random;

/**
 * High variance damage: 0 to 2× attackStat randomly.
 * Could deal nothing or double — unpredictable, high tension.
 * Good for enemy bosses or glass-cannon enemies.
 */
public class HighVarianceDice implements DamageStrategy {

    private final Random rand = new Random();

    @Override
    public int roll(int attackStat) {
        return rand.nextInt(Math.max(1, attackStat * 2) + 1);
    }

    @Override
    public String describe() {
        return "rolled wildly";
    }
}
