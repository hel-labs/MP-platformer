package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;

/**
 * Base class for player-selectable battle actions.
 */
public abstract class BattleAction {

    /**
     * Executes the action effect.
     *
     * @param ctx battle context
     * @return result of the action
     */
    public abstract BattleResult execute(BattleContext ctx);

    /** @return display label for the action */
    public abstract String getLabel();

    /** @return description of the action */
    public abstract String getDescription();
}
