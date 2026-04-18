package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;

public class SpareAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        if (ctx.getEnemy().isMercyReady(ctx)) {
            ctx.getEnemy().onSpared();
            return BattleResult.mercyGranted(ctx.getEnemy().getName());
        }
        return BattleResult.hint(ctx.getEnemy().getMercyHint(ctx));
    }

    @Override public String getLabel()       { return "SPARE";                       }
    @Override public String getDescription() { return "Show mercy if time is right."; }
}