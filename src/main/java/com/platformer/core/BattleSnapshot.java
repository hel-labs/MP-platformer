package com.platformer.core;

public class BattleSnapshot {
    public final int hp;
    public final int maxHp;
    public final int attack;
    public final int stamina;
    public final int maxStamina;
 
    public BattleSnapshot(int hp, int maxHp, int attack, int stamina, int maxStamina) {
        this.hp     = hp;
        this.maxHp  = maxHp;
        this.attack = attack;
        this.stamina = stamina;
        this.maxStamina = maxStamina;
    }
}
