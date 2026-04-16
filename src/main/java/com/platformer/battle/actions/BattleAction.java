package com.echoes.battle.actions;

import com.echoes.battle.BattleContext;
import com.echoes.battle.BattleResult;

/**
 * Abstract base for every action the player can take in battle.
 *
 * Each concrete action is a self-contained class with a single execute()
 * method. The BattleEngine calls execute() and receives a BattleResult.
 * The engine never knows which action it is calling — only the interface.
 *
 * Adding a new action = adding one new subclass. Nothing else changes.
 */
public abstract class BattleAction {

    /**
     * Execute this action against the current battle state.
     *
     * @param ctx the shared battle context (player, enemy, counters)
     * @return    a BattleResult describing what happened
     */
    public abstract BattleResult execute(BattleContext ctx);

    /**
     * Short label shown in the action menu.
     * e.g. "FIGHT", "TALK", "SPARE", "FLEE"
     */
    public abstract String getLabel();

    /**
     * One-line description shown at the bottom of the menu when
     * this action is highlighted. Optional — defaults to empty.
     */
    public String getDescription() {
        return "";
    }
}
