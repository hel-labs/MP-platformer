package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.entities.BattleEnemy;

import java.util.Random;

public class FleeAction extends BattleAction{
    private static final float BASE_CHANCE = 0.30f;
    private static final float TALK_BONUS = 0.10f;
    private static final float MAX_CHANCE = 0.75f;

    private final Random rand = new Random();

    @Override
    public BattleResult execute(BattleContext ctx){
        BattleEnemy = ctx.getEnemy();

        if(!enemy.isFleeAllowed()){
            return BattleResult.fleeFailed();
        }

        float chance = Math.min(MAX_CHANCE, BASE_CHANCE + ctx.getTalkCount() * TALK_BONUS);

        if(rand.nextFloat()<chance){
            return BattleResult.fled();
        }
        else{
            return BattleResult.fleeFailed();
        }
    }

    @Override
    public String getLabel(){
        return "Flee!";
    }

    @Override
    public String getDescription(){
        return "Try your luck to see if you can escape.";
    }
}