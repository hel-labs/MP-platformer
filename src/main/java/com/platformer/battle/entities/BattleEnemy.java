package com.platformer.battle.entities;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.talk.TalkOption;

import java.util.List;

public abstract class BattleEnemy {

    protected int     hp;
    protected int     maxHp;
    protected int     attack;
    protected boolean fleeAllowed = true;

    public abstract String getName();
    public abstract String getEncounterDialogue();
    public abstract int    getBaseHostility();
    public abstract List<TalkOption> getTalkOptions(int talkCount);
    public abstract DamageStrategy  getDamageStrategy();

    public boolean isMercyReady(BattleContext ctx) {
        return ctx.isMercyAvailable();
    }

    public String getMercyHint(BattleContext ctx) {
        return "* " + getName() + " is still hostile. ("
             + ctx.getHostility() + " remaining)";
    }

    public void onSpared() { hp = 0; }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public boolean isDefeated()    { return hp <= 0;     }
    public boolean isFleeAllowed() { return fleeAllowed; }
    public int     getHp()         { return hp;          }
    public int     getMaxHp()      { return maxHp;       }
    public int     getAttack()     { return attack;      }
}