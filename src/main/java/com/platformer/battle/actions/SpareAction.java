package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;

public class SpareAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        // Spare enemy if hostility is at stable stage
        if (ctx.getEnemy().isMercyReady(ctx)) {
            ctx.getEnemy().onSpared();
            return BattleResult.mercyGranted(ctx.getEnemy().getName());
        }
        // Return hint if hostility still not low enough
        return BattleResult.hint(ctx.getEnemy().getMercyHint(ctx));
    }

    @Override
    public String getLabel() {
        return "SPARE";
    }

    @Override
    public String getDescription() {
        return "Show mercy if time is right.";
    }
}
