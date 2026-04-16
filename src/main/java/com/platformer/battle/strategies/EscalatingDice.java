package com.echoes.battle.strategies;

import java.util.Random;

/**
 * Escalating damage: base + small roll + turnCount bonus.
 * Gets more dangerous the longer the battle drags on.
 * Good for boss enemies that punish stalling.
 *
 * This strategy needs the turn count from BattleContext.
 * Use rollWithTurn() instead of roll() for correct behavior.
 */
public class EscalatingDice implements DamageStrategy {

    private final Random rand = new Random();

    @Override
    public int roll(int attackStat) {
        // Fallback — no turn bonus if called without context
        return Math.max(1, attackStat + rand.nextInt(3));
    }

    /**
     * Preferred call — includes turn-based escalation.
     * @param attackStat base attack value
     * @param turnCount  current turn number from BattleContext
     */
    public int rollWithTurn(int attackStat, int turnCount) {
        return Math.max(1, attackStat + rand.nextInt(3) + turnCount);
    }

    @Override
    public String describe() {
        return "power is growing";
    }
}
