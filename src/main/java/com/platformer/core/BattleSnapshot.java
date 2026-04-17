package com.platformer.core;

public class BattleSnapshot {
    public final int hp;
    public final int maxHp;
    public final int attack;

    public BattleSnapshot(int hp, int maxHp, int attack) {
        this.hp     = hp;
        this.maxHp  = maxHp;
        this.attack = attack;
    }
}