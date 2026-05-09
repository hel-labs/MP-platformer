package com.platformer.battle.entities;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.strategies.*;
import com.platformer.battle.talk.TalkOption;

import java.util.List;
import java.util.Random;

public class BossEnemy extends BattleEnemy {

    private static final String[] TAUNTS = {
        "Is that all you've got? Pathetic.",
        "You call that fighting? My grandmother hits harder.",
        "I've beaten better heroes before breakfast.",
        "You're making this too easy for me.",
        "Give up now and I'll make it quick."
    };

    private boolean isAttackTurn = true;
    private final Random rng = new Random();

    public BossEnemy() {
        this.hp = 120;
        this.maxHp = 120;
        this.attack = 18;
        this.fleeAllowed = false;
        initBattleAnimation(
            "npc_sprite.png",
            32, 32,
            0, 5, 0.15f
        );
    }

    @Override
    public String getName() {
        return "Gate Keeper";
    }

    @Override
    public String getEncounterDialogue() {
        return "You answered wrong. Now face the consequences.";
    }

    @Override
    public int getBaseHostility() {
        return 10;
    }

    @Override
    public List<TalkOption> getTalkOptions(int talkCount) {
        return List.of();
    }

    @Override
    public DamageStrategy getDamageStrategy() {
        return new HighVarianceDice();
    }

    @Override
    public boolean isMercyReady(BattleContext ctx) {
        return false;
    }

    @Override
    public int getPointValue() {
        return 0;
    }

    public String getNextTaunt() {
        return TAUNTS[rng.nextInt(TAUNTS.length)];
    }

    public boolean isAttackTurn() {
        return isAttackTurn;
    }

    public void advanceTurn() {
        isAttackTurn = !isAttackTurn;
    }
}