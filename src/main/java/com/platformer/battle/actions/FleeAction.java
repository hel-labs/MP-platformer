package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;

import java.util.Random;

public class FleeAction extends BattleAction {

    private static final float BASE_FLEE_CHANCE = 0.50f;
    private static final float TALK_FLEE_BONUS  = 0.10f;
    private static final float MAX_FLEE_CHANCE  = 0.95f;
    private final Random rand = new Random();

    @Override
    public BattleResult execute(BattleContext ctx) {
        if (!ctx.getEnemy().isFleeAllowed())
            return BattleResult.fleeFailed();

        float chance = Math.min(MAX_FLEE_CHANCE,
            BASE_FLEE_CHANCE + ctx.getTalkCount() * TALK_FLEE_BONUS);

        return rand.nextFloat() < chance
            ? BattleResult.fled()
            : BattleResult.fleeFailed();
    }

    @Override public String getLabel()       { return "FLEE";                              }
    @Override public String getDescription() { return "Talking improves escape odds."; }
}