package com.platformer.battle.strategies;

import java.util.Random;

public class StandardDice implements DamageStrategy{
    private final Random rand = new Random();

    @Override
    public int roll(int attackStat){
        return Math.max(1, attackStat + rand.nextInt(6)+1);
    }
    @Override
    public String describe(){
        return "You rol the standard die.";
    }
}