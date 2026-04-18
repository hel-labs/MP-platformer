package com.platformer.battle.strategies;

import java.util.Random;

public class HighVarianceDice implements DamageStrategy{
    private final Random rand = new Random();

    @Override
    public int roll(int attackStat){
        return rand.nextInt(Math.max(1, attackStat *2)+1);
    }
    @Override
    public String describe(){
        return "Your unleashed power storms the battlefield.";
    }
}