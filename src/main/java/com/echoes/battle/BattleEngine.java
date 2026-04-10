package com.echoes.battle;

import com.echoes.battle.actions.*;
import com.echoes.battle.strategies.DamageStrategy;
import com.echoes.entities.Enemy;
import com.echoes.entities.Player;
import com.echoes.exceptions.BattleException;
import com.echoes.exceptions.EnemyAlreadyDeadException;

import java.util.List;

/**
 * The battle engine.
 *
 * Owns the list of player actions and knows how to execute a player turn
 * and an enemy turn. It does not own the game loop — BattleState drives
 * the loop and calls into the engine at the right moments.
 *
 * The engine is stateless between calls. All mutable state lives in
 * BattleContext, which is passed in on every call.
 *
 * Turn flow (managed externally by BattleState):
 *   1. Player selects action → executePlayerAction()
 *   2. BattleState waits for animation
 *   3. BattleState shows result dialogue
 *   4. Player presses Z → executeEnemyTurn()
 *   5. BattleState waits for animation
 *   6. BattleState shows result dialogue
 *   7. Player presses Z → back to step 1
 */
public class BattleEngine {

    private final List<BattleAction> playerActions;

    public BattleEngine() {
        // Order determines menu position — do not rearrange without
        // updating BattleUI's grid layout
        playerActions = List.of(
            new FightAction(),
            new TalkAction(),
            new SpareAction(),
            new FleeAction()
        );
    }

    // ---------------------------------------------------------------
    // Player turn
    // ---------------------------------------------------------------

    /**
     * Execute the player's chosen action.
     *
     * @param actionIndex 0=Fight, 1=Talk, 2=Spare, 3=Flee
     * @param ctx         current battle context
     * @return            result describing what happened
     * @throws BattleException if the action index is invalid
     */
    public BattleResult executePlayerAction(int actionIndex, BattleContext ctx) {
        if (actionIndex < 0 || actionIndex >= playerActions.size()) {
            throw new BattleException(
                "Invalid action index: " + actionIndex
                + " (valid: 0-" + (playerActions.size() - 1) + ")");
        }

        BattleAction action = playerActions.get(actionIndex);
        BattleResult result = action.execute(ctx);

        ctx.setPlayerTurn(false);
        ctx.setLastResult(result);

        return result;
    }

    // ---------------------------------------------------------------
    // Enemy turn
    // ---------------------------------------------------------------

    /**
     * Execute the enemy's turn. The enemy rolls its DamageStrategy against
     * the player and applies the result.
     *
     * @param ctx current battle context
     * @return    result describing what the enemy did
     * @throws EnemyAlreadyDeadException if the enemy is already defeated
     */
    public BattleResult executeEnemyTurn(BattleContext ctx) {
        Enemy enemy = ctx.getEnemy();

        if (enemy.isDefeated()) {
            throw new EnemyAlreadyDeadException(enemy.getName());
        }

        Player player = ctx.getPlayer();

        DamageStrategy strategy = enemy.getDamageStrategy();
        int damage = strategy.roll(enemy.getAttack());

        player.takeDamage(damage);

        String msg = "* " + enemy.getName() + " attacks! ("
                   + strategy.describe()
                   + ") — dealt " + damage + " damage!";

        ctx.incrementTurnCount();
        ctx.setPlayerTurn(true);
        ctx.setLastResult(null);

        if (player.isDefeated()) {
            return BattleResult.playerDefeated();
        }

        return BattleResult.enemyAttacked(damage, msg);
    }

    // ---------------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------------

    public List<BattleAction> getPlayerActions() {
        return playerActions;
    }

    public int getActionCount() {
        return playerActions.size();
    }
}
