package com.platformer.battle.strategies;

import java.util.Random;

public class EscalatingDice implements DamageStrategy{
    private final Random rand = new Random();

    @Override
    public int roll(int attackStat){
        return Math.max(1, attackState + rand.nextInt(3));
    }
    public int rollWithTurn(int attackStat, int turnCount){
        return Math.max(1, attackStat+rand.nextInt(3)+turnCount);
    }
    @Override
    public String describe(){
        return "Your power grows with each turn!";
    }
}