package com.platformer.battle.engine;

import com.platformer.battle.actions.*;
import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.entities.BattlePlayer;
import com.platformer.battle.strategies.DamageStrategy;
import com.platformer.exceptions.BattleException;
import com.platformer.exceptions.EnemyAlreadyDeadException;

import java.util.List;

public class BattleEngine {

    private final List<BattleAction> playerActions;

    public BattleEngine() {
        playerActions = List.of(
                new FightAction(),
                new TalkAction(),
                new SpareAction(),
                new FleeAction()
        );
    }

    public BattleResult executePlayerAction(int index, BattleContext ctx) {
        if (index < 0 || index >= playerActions.size()) {
            throw new BattleException("Invalid action index: " + index);
        }

        BattleAction action = playerActions.get(index);
        BattleResult result = action.execute(ctx);
        ctx.setPlayerTurn(false);
        ctx.setLastResult(result);
        return result;
    }

    public BattleResult executeEnemyTurn(BattleContext ctx) {
        BattleEnemy enemy = ctx.getEnemy();
        if (enemy.isDefeated()) {
            throw new EnemyAlreadyDeadException(enemy.getName());
        }

        BattlePlayer player = ctx.getPlayer();
        enemy.playAttackAnimation();

        DamageStrategy strategy = enemy.getDamageStrategy();
        int base = strategy.roll(enemy.getAttack());
        int bonus = ctx.getHostilityDamageBonus();
        int damage = base + bonus;

        player.takeDamage(damage);
        player.recoverStamina(5);

        String bonusNote = bonus > 0 ? " (+" + bonus + " hostility)" : "";
        String msg = "* " + enemy.getName() + " attacks! ("
                + strategy.describe() + ") — dealt " + damage
                + " damage!" + bonusNote;

        ctx.incrementTurnCount();
        ctx.setPlayerTurn(true);
        ctx.setLastResult(null);

        if (player.isDefeated()) {
            return BattleResult.playerDefeated();
        }

        return BattleResult.enemyAttacked(damage, msg);
    }

    public List<BattleAction> getPlayerActions() {
        return playerActions;
    }

    public int getActionCount() {
        return playerActions.size();
    }
}
