package com.platformer.battle.entities;

import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.strategies.StandardDice;
import com.platformer.battle.talk.TalkOption;

import java.util.List;

public class CrabbyBattleEnemy extends BattleEnemy {

    public CrabbyBattleEnemy() {
        this.hp     = 22;
        this.maxHp  = 22;
        this.attack = 4;
        initBattleAnimation("/res/crabby_sprite.png", 72, 32, 0, 9, 0.12f, 2, 7, 0.08f);
    }

    @Override public String getName()              { return "Crabby";                      }
    @Override public String getEncounterDialogue() { return "* A Crabby snaps its claws!"; }
    @Override public int    getBaseHostility()     { return 3;                              }
    @Override public DamageStrategy getDamageStrategy() { return new StandardDice();       }

    @Override
    public List<TalkOption> getTalkOptions(int talkCount) {
        return switch (talkCount) {
            case 0 -> List.of(
                new TalkOption("You look tired.",      "* It slows down slightly.",    -1),
                new TalkOption("I'll crush you!",      "* It snaps faster, enraged.",  +1),
                new TalkOption("What do you want?",    "* It clicks its claws slowly.", 0)
            );
            case 1 -> List.of(
                new TalkOption("Let's not fight.",     "* It lowers its claws a bit.", -1),
                new TalkOption("You're just a crab.",  "* It attacks furiously!",      +1)
            );
            default -> List.of(
                new TalkOption("I mean no harm.",      "* It seems calmer now.",       -1),
                new TalkOption("Move aside!",          "* It raises its claws again.", +1)
            );
        };
    }
}