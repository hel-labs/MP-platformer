package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;

/**
 * Action that attempts to spare the enemy.
 */
public class SpareAction extends BattleAction {

    /**
     * Executes the mercy check and returns the outcome.
     */
    @Override
    public BattleResult execute(BattleContext ctx) {
        if (ctx.getEnemy().isMercyReady(ctx)) {
            ctx.getEnemy().onSpared();
            return BattleResult.mercyGranted(ctx.getEnemy().getName());
        }
        return BattleResult.hint(ctx.getEnemy().getMercyHint(ctx));
    }

    /** @return action label */
    @Override
    public String getLabel() {
        return "SPARE";
    }

    /** @return action description */
    @Override
    public String getDescription() {
        return "Show mercy if time is right.";
    }
}
