package com.echoes.battle.strategies;

import java.util.Random;

/**
 * Standard damage: attackStat + 1d6
 * Predictable range, scales with attacker strength.
 */
public class StandardDice implements DamageStrategy {

    private final Random rand = new Random();

    @Override
    public int roll(int attackStat) {
        return Math.max(1, attackStat + rand.nextInt(6) + 1);
    }

    @Override
    public String describe() {
        return "rolled the die";
    }
}
