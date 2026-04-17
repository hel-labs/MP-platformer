package com.platformer.battle.action;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.battle.entities.*;

public class FightAction extends BattleAction {
    @Override
    public Battle execute(BattleContext ctx){
        BattlePlayer player = ctx.getPlayer();
        BattleEnemy enemy = ctx.getEnemy();

        DamageStrategy strategy = player.getDamageStrategy();
        int damage = strategy.roll(player.getAttack());

        enemy.takeDamage(damage);

        player.playAttackAnimation();
        ctx.setWaitingForPlayerAnimation(true);

        String msg = "You attack!\n" + strategy.describe()+ "Dealt " +damage+" damage!";
         if(enemy.isDefeated()){
            return BattleResult.enemyDefeated(enemy.getName());
         }

         return BattleResult.playerAttacked(damage, msg);
    }

    @Override
    public String getLabel(){
        return "Fight!";
    }

    @Override
    public String getDescription(){
        return "Attack the enemy to deal damage and defeat it!";
    }
}