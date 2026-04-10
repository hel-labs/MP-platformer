package com.echoes.battle.actions;

import com.echoes.battle.BattleContext;
import com.echoes.battle.BattleResult;
import com.echoes.entities.Enemy;

/**
 * Player talks to the enemy.
 *
 * Flow:
 *   1. Ask enemy for a talk response based on current talk count
 *   2. Increment talk count in context
 *   3. Return TALKED result with the enemy's response text
 *
 * Talk count is used by SpareAction to check mercy readiness.
 * Each successive talk should return progressively warmer dialogue.
 */
public class TalkAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        Enemy enemy     = ctx.getEnemy();
        int   talkCount = ctx.getTalkCount();

        String response = enemy.getTalkResponse(talkCount);
        ctx.incrementTalkCount();

        return BattleResult.talked(response);
    }

    @Override
    public String getLabel() { return "TALK"; }

    @Override
    public String getDescription() {
        return "Try talking to the enemy.";
    }
}
