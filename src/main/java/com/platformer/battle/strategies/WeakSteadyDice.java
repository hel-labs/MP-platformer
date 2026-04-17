package com.platformer.battle.strategies;

import java.util.Random;

public class WeakSteadyDice implements DamageStrategy{
    private final Random rand = new Random();

    @Override
    public int roll(int attackStat){
        return Math.max(1, attackStat/2);
    }
    @Override
    public String describe(){
        return "Your power lies dormant as you strike steadily.";
    }
}