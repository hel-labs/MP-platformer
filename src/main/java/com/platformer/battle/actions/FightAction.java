package com.platformer.battle.actions;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.entities.BattlePlayer;
import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.strategies.DamageStrategy;

public class FightAction extends BattleAction {

    @Override
    public BattleResult execute(BattleContext ctx) {
        BattlePlayer player   = ctx.getPlayer();
        BattleEnemy  enemy    = ctx.getEnemy();
        player.playAttackAnimation();
        player.spendStamina(8);
        DamageStrategy strategy = player.getDamageStrategy();
        int damage = strategy.roll(player.getAttack());

        enemy.takeDamage(damage);

        String msg = "* You attack! (" + strategy.describe()
                   + ") — dealt " + damage + " damage!";

        if (enemy.isDefeated())
            return BattleResult.enemyDefeated(enemy.getName());

        return BattleResult.playerAttacked(damage, msg);
    }

    @Override public String getLabel()       { return "FIGHT";              }
    @Override public String getDescription() { return "Attack the enemy.";  }
}