package com.platformer.battle.entities;

import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.strategies.HighVarianceDice;
import com.platformer.battle.talk.TalkOption;

import java.util.List;

public class PinkstarBattleEnemy extends BattleEnemy {

    public PinkstarBattleEnemy() {
        this.hp     = 20;
        this.maxHp  = 20;
        this.attack = 8;
    }

    @Override public String getName()              { return "Pinkstar";                        }
    @Override public String getEncounterDialogue() { return "* A Pinkstar spins toward you!";  }
    @Override public int    getBaseHostility()     { return 4;                                  }
    @Override public DamageStrategy getDamageStrategy() { return new HighVarianceDice();       }

    @Override
    public List<TalkOption> getTalkOptions(int talkCount) {
        return switch (talkCount) {
            case 0 -> List.of(
                new TalkOption("You're beautiful.",    "* It slows its spinning.",      -1),
                new TalkOption("Get out of my way!",   "* It spins faster, enraged.",   +1),
                new TalkOption("What are you?",        "* It wobbles curiously.",         0)
            );
            case 1 -> List.of(
                new TalkOption("I admire your grace.", "* It seems flattered.",          -1),
                new TalkOption("You're in my way.",    "* It spins with more force.",    +1)
            );
            default -> List.of(
                new TalkOption("Let's be friends.",    "* It nearly stops spinning.",    -1),
                new TalkOption("Move!",                "* It spins aggressively again.", +1)
            );
        };
    }
}