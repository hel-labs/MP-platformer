package com.platformer.battle.entities.enemies;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.strategies.WeakSteadyDice;
import com.platformer.battle.talk.TalkOption;

import java.util.List;

public class PlaceholderEnemy extends BattleEnemy {

    public PlaceholderEnemy() {
        this.hp     = 20;
        this.maxHp  = 20;
        this.attack = 4;
    }

    @Override public String getName()             { return "Test Slime";              }
    @Override public String getEncounterDialogue(){ return "* A slime blocks the way!"; }
    @Override public int    getBaseHostility()    { return 2;                          }
    @Override public DamageStrategy getDamageStrategy() { return new WeakSteadyDice(); }

    @Override
    public List<TalkOption> getTalkOptions(int talkCount) {
        return List.of(
            new TalkOption("You seem harmless.", "* It wobbles happily.",  -1),
            new TalkOption("Get out of my way!","* It tenses up angrily.", +1)
        );
    }
}