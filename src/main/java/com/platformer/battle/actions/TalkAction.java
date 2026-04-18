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
        ctx.applyHostilityDelta(option.getHostilityDelta());
        ctx.incrementTalkCount();
        return BattleResult.talked(option.getResponse());
    }

    @Override public String getLabel()       { return "TALK";                                    }
    @Override public String getDescription() { return "Some words calm, others provoke."; }
}