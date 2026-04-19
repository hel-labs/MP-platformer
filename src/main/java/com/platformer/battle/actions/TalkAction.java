package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.talk.TalkOption;

public class TalkAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        return BattleResult.talkInitiated();
    }

    public BattleResult resolveOption(TalkOption option, BattleContext ctx) {
        int delta = option.getHostilityDelta();
        if (delta < 0) {
            int calmMultiplier = Math.min(3, 1 + ctx.getTalkCount());
            delta *= calmMultiplier;
        }
        ctx.applyHostilityDelta(delta);
        ctx.incrementTalkCount();
        return BattleResult.talked(option.getResponse());
    }

    @Override public String getLabel()       { return "TALK";                                    }
    @Override public String getDescription() { return "Some words calm, others provoke."; }
}