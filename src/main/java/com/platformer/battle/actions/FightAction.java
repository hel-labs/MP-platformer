package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.entities.BattlePlayer;
import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.strategies.DamageStrategy;

/**
 * Action that performs a player attack.
 */
public class FightAction extends BattleAction {

    /**
     * Executes a player attack and returns the outcome.
     */
    @Override
    public BattleResult execute(BattleContext ctx) {
        BattlePlayer player = ctx.getPlayer();
        BattleEnemy enemy = ctx.getEnemy();
        player.playAttackAnimation();
        player.spendStamina(8);
        DamageStrategy strategy = player.getDamageStrategy();
        int damage = strategy.roll(player.getAttack());

        enemy.takeDamage(damage);

        String msg = "* You attack! (" + strategy.describe()
                + ") — dealt " + damage + " damage!";

        if (enemy.isDefeated()) {
            return BattleResult.enemyDefeated(enemy.getName());
        }

        return BattleResult.playerAttacked(damage, msg);
    }

    /** @return action label */
    @Override
    public String getLabel() {
        return "FIGHT";
    }

    /** @return action description */
    @Override
    public String getDescription() {
        return "Attack the enemy.";
    }
}
