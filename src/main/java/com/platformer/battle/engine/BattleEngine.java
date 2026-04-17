package com.platformer.battle.engine;

import com.platformer.battle.actions.*;
import com.platformer.battle.strategies.DamageSTrategy;
import com.platformer.battle.entities.*;
import com.platformer.exceptions.*;

import java.util.List;

public class BattleEngine{
    private final List<BattleAction> playerActions;

    public BattleEngine(){
        playerActions = List.of(
            new FightAction(),
            new TalkAction(),
            new SpareAction(),
            new FleeAction()
        );
    }

    public BattleResult executePlayerAction(int actionIndex, BattleContext ctx){
        if(actionIndex <0 || actionIndex>=playerActions.size()){
            throw new BattleException("Invalid action index. Valid: 0-3. Provided: " + actionIndex);
        }
        BattleAction action = playerActions.get(actionIndex);
        BattleResult result = action.execute(ctx);

        ctx.setPlayerTurn(false);
        ctx.setLastResult(result);

        return result;
    }

    public BattleResult executeEnemyAction(BattleContext ctx){
        BattleEnemy enemy = ctx.getEnemy();

        if(enemy.isDefeated()){
            throw new EnemyAlreadyDeadException(enemy.getName());
        }

        BattlePlayer player = ctx.getPlayer();

        DamageStrategy strategy = enemy.getDamageStrategy();
        int baseDamage = strategy.roll(enemy.getAttack());
        int bonusDamage = ctx.getHostilityDamageBonus();
        int damage = baseDamage + bonusDamage;

        player.takeDamage(damage);

        String msg = enemy.getName() + " attacks. The attack deals " + damage + " damage!";

        ctx.incrementTurnCount();
        ctx.setPlayerTurn(true);
        ctx.setLastResult(null);

        if(player.isDefeated()){
            return BattleResult.playerDefeated();
        }

        return BattleResult.enemyAttacked(damage, msg);
    }

    public List<BattleAction> getPlayerActions(){
        return playerActions;
    }

    public int getActionCount(){
        return playerActions.size();
    }
}