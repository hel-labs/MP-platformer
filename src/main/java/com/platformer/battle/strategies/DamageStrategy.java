package com.platformer.battle.strategies;

public interface DamageStrategy{
    int roll(int attackStat);
    String describe();
}