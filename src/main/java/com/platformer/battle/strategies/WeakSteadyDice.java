package com.platformer.battle.strategies;

public class WeakSteadyDice implements DamageStrategy {

    @Override
    public int roll(int attackStat) {
        return Math.max(1, attackStat / 2);
    }

    @Override
    public String describe() {
        return "The power lies dormant as the steady strike lands.";
    }
}
