package com.echoes.battle.actions;

import com.echoes.battle.BattleContext;
import com.echoes.battle.BattleResult;
import com.echoes.entities.Enemy;

/**
 * Player attempts to spare the enemy (mercy).
 *
 * Flow:
 *   1. Ask enemy if mercy condition is met (isMercyReady)
 *   2. If yes  → call onSpared(), return MERCY_GRANTED
 *   3. If no   → return HINT with the enemy's hint text
 *
 * Mercy readiness is entirely defined by the enemy subclass.
 * Common condition: talk count reached a threshold.
 * The engine and this action never hardcode that threshold.
 */
public class SpareAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        Enemy enemy = ctx.getEnemy();

        if (enemy.isMercyReady(ctx)) {
            enemy.onSpared();
            return BattleResult.mercyGranted(enemy.getName());
        }

        // Not ready — return a hint to guide the player
        String hint = enemy.getMercyHint(ctx);
        return BattleResult.hint(hint);
    }

    @Override
    public String getLabel() { return "SPARE"; }

    @Override
    public String getDescription() {
        return "Show mercy if the time is right.";
    }
}
