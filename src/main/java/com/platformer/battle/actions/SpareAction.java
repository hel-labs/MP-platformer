package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.entities.*;

public class SpareAction extends BattleAction{
    @Override
    public BattleResult execute(BattleContext ctx){
    
        BattleEnemy enemy = ctx.getEnemy();

        if(enemy.isMercyReady(ctx)){
            enemy.onSpared();
            return BattleResult.mercyGranted(enemy.getName());
        }

        String hint = enemy.getMercyHint(ctx);
        return BattleResult.hint(hint);
    }

    @Override
    public String getLabel(){
        return "Spare";
    }

    @Override
    public String getDescription(){
        return "Show mercy and spare the enemy if you feel the time is right."
    }
}