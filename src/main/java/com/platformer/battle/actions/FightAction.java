package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.entities.BattlePlayer;
import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.strategies.DamageStrategy;

public class FightAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        // Get the player and emeny status
        // create battle entity objects based on the overworld entities.
        BattlePlayer player = ctx.getPlayer();
        BattleEnemy enemy = ctx.getEnemy();

        player.playAttackAnimation(); // Player animation player
        player.spendStamina(8); // Defined stamina cost for fight action

        // Get dice based damage strategy
        // Calculate the damage, apply it to enemy
        DamageStrategy strategy = player.getDamageStrategy();
        int damage = strategy.roll(player.getAttack());
        enemy.takeDamage(damage);

        // Display damage strategy message
        String msg = "* You attack! (" + strategy.describe()
                + ") — dealt " + damage + " damage!";

        // End battle if enemy is defeated, and return result
        if (enemy.isDefeated()) {
            return BattleResult.enemyDefeated(enemy.getName());
        }

        // Return result of action if enemy not defeated
        return BattleResult.playerAttacked(damage, msg);
    }

    @Override
    public String getLabel() {
        return "FIGHT";
    }

    @Override
    public String getDescription() {
        return "Attack the enemy.";
    }
}
