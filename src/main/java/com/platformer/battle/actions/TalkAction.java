package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.entities.*;
import com.platformer.battle.talk.TalkOption;

import java.util.List;

public class TalkAction extends BattleAction {
    @Override
    public BattleResult execute(BattleContext ctx){
        return BattleResult.talkInitiated();
    }

    @Override
    public BattleResult resolveOption(TalkOption option, BattleContext ctx){
        ctx.applyHostilityDelta(option.getHostilityDelta());
        ctx.incrementTalkCount();

        String prefix;
        if(option.isCalming()){
            prefix = "The enemy seems calmer.";
        }
        else if(option.isProvoking()){
            prefix = "The enemy seems agitated.";
        }
        else{
            prefix = "The enemy doesn't react!";
        }

        return BattleResult.talked(prefix + option.getResponse());
    }

    @Override
    public String getLabel(){
        return "Talk...";
    }

    @Override
    public String getDescription(){
        return "Try to talk to the enemy. Some words might calm it, others will provoke it further!";
    }
}