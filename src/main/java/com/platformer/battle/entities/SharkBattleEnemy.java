package com.platformer.battle.entities;

import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.strategies.EscalatingDice;
import com.platformer.battle.talk.TalkOption;

import java.util.List;

public class SharkBattleEnemy extends BattleEnemy {

    private final EscalatingDice strategy = new EscalatingDice();

    public SharkBattleEnemy() {
        this.hp     = 40;
        this.maxHp  = 40;
        this.attack = 10;
        this.fleeAllowed = false;
    }

    @Override public String getName()              { return "Shark";                           }
    @Override public String getEncounterDialogue() { return "* A Shark circles menacingly!";   }
    @Override public int    getBaseHostility()     { return 5;                                  }
    @Override public DamageStrategy getDamageStrategy() { return strategy;                     }

    @Override
    public List<TalkOption> getTalkOptions(int talkCount) {
        return switch (talkCount) {
            case 0 -> List.of(
                new TalkOption("Calm down.",            "* It slows its circling.",        -1),
                new TalkOption("I'm not afraid!",       "* It surges forward, enraged.",   +1),
                new TalkOption("Why are you here?",     "* It tilts its head slightly.",    0)
            );
            case 1 -> List.of(
                new TalkOption("You don't need to fight.", "* It backs off slightly.",     -1),
                new TalkOption("Come get me!",          "* It lunges aggressively!",       +1)
            );
            default -> List.of(
                new TalkOption("Let's coexist.",        "* It circles more slowly.",       -1),
                new TalkOption("Back off!",             "* It bares its teeth.",           +1)
            );
        };
    }
}