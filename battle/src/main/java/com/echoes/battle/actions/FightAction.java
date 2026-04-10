package com.echoes.battle.actions;

import com.echoes.battle.BattleContext;
import com.echoes.battle.BattleResult;
import com.echoes.battle.strategies.DamageStrategy;
import com.echoes.entities.Enemy;
import com.echoes.entities.Player;

/**
 * Player attacks the enemy using their DamageStrategy.
 *
 * Flow:
 *   1. Roll damage via player's DamageStrategy
 *   2. Apply damage to enemy
 *   3. Trigger player attack animation
 *   4. Return ENEMY_DEFEATED if HP reaches zero, else PLAYER_ATTACKED
 */
public class FightAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        Player player = ctx.getPlayer();
        Enemy  enemy  = ctx.getEnemy();

        DamageStrategy strategy = player.getDamageStrategy();
        int damage = strategy.roll(player.getAttack());

        enemy.takeDamage(damage);

        // Trigger attack animation — BattleState will wait for it to finish
        player.playAttackAnimation();
        ctx.setWaitingForPlayerAnim(true);

        String msg = "* You attack! ("
                   + strategy.describe()
                   + ") — dealt " + damage + " damage!";

        if (enemy.isDefeated()) {
            return BattleResult.enemyDefeated(enemy.getName());
        }

        return BattleResult.playerAttacked(damage, msg);
    }

    @Override
    public String getLabel() { return "FIGHT"; }

    @Override
    public String getDescription() { return "Attack the enemy."; }
}
