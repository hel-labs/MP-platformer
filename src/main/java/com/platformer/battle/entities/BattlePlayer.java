package com.platformer.battle.entities;

import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.strategies.StandardDice;
import com.platformer.core.BattleSnapshot;


public class BattlePlayer {

    private int hp;
    private int maxHp;
    private int attack;
    private final DamageStrategy damageStrategy = new StandardDice();

    public BattlePlayer(BattleSnapshot snapshot) {
        this.hp     = snapshot.hp;
        this.maxHp  = snapshot.maxHp;
        this.attack = snapshot.attack;
    }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public boolean isDefeated()          { return hp <= 0;         }
    public int     getHp()               { return hp;              }
    public int     getMaxHp()            { return maxHp;           }
    public int     getAttack()           { return attack;          }
    public DamageStrategy getDamageStrategy() { return damageStrategy; }
}