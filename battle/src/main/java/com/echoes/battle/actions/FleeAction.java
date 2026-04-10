package com.echoes.battle.actions;

import com.echoes.battle.BattleContext;
import com.echoes.battle.BattleResult;
import com.echoes.entities.Enemy;

import java.util.Random;

/**
 * Player attempts to flee the battle.
 *
 * Flow:
 *   1. Check if the enemy allows fleeing — if not, always fail
 *   2. Roll flee probability (base 50% + 10% per talk)
 *   3. Return PLAYER_FLED on success, FLEE_FAILED on failure
 *
 * Flee probability curve:
 *   0 talks → 50%
 *   1 talk  → 60%
 *   2 talks → 70%
 *   3 talks → 80%
 *   ...capped at 95%
 *
 * This incentivises talking even when the player wants to flee.
 */
public class FleeAction extends BattleAction {

    private static final float BASE_FLEE_CHANCE  = 0.50f;
    private static final float TALK_FLEE_BONUS   = 0.10f;
    private static final float MAX_FLEE_CHANCE   = 0.95f;

    private final Random rand = new Random();

    @Override
    public BattleResult execute(BattleContext ctx) {
        Enemy enemy = ctx.getEnemy();

        // Some enemies (bosses) cannot be fled from
        if (!enemy.isFleeAllowed()) {
            return BattleResult.fleeFailed();
        }

        float chance = Math.min(
            MAX_FLEE_CHANCE,
            BASE_FLEE_CHANCE + ctx.getTalkCount() * TALK_FLEE_BONUS
        );

        if (rand.nextFloat() < chance) {
            return BattleResult.fled();
        } else {
            return BattleResult.fleeFailed();
        }
    }

    @Override
    public String getLabel() { return "FLEE"; }

    @Override
    public String getDescription() {
        return "Try to escape. Talking improves your odds.";
    }
}
