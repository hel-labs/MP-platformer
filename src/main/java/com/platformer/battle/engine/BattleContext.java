package com.platformer.battle.engine;

import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.entities.BattlePlayer;
import com.platformer.overworld.entities.Player;
import com.platformer.overworld.entities.Enemy;

public class BattleContext {
    private final BattlePlayer player;
    private final BattleEnemy enemy;

    private int talkCount =0;
    private int turnCount = 0;
    private boolean playerTurn = true;

    private int hostility;
    public static final int MAX_HOSTILITY = 10;
    public static final int MIN_HOSTILITY = 0;

    private boolean waitingForPlayerAnimation = false;
    private boolean waitingForEnemyAnimation = false;
    private BattleResult lastResult = null;

    public BattleContext(Player player, Enemy enemy){
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        if (enemy  == null) throw new IllegalArgumentException("Enemy cannot be null");
        this.player = new BattlePlayer(player);
        this.enemy=new BattleEnemy(enemy);
        this.hostility=enemy.getBaseHostility;
    }

    public void incrementTalkCount(){
        talkCount++;
    }
    public void incrementTurnCount(){
        turnCount++;
    }
    public void setPlayerTurn(boolean bool){
        this.playerTurn=bool;
    }

    public void applyHostilityDelta(int delta){
        hostility = Math.max(MIN_HOSTILITY, Math.min(MAX_HOSTILITY, hostility+delta));
    }

    public boolean isMercyAvailable(){
        return hostility <= MIN_HOSTILITY;
    }
    public int getHostilityDamageBonus() {
        return hostility * 2;
    }
    public int getHostility(){
        return hostility;
    }

    public void setWaitingForPlayerAnim(boolean w) {
        waitingForPlayerAnim = w;
    }
    public void setWaitingForEnemyAnim(boolean w)  {
        waitingForEnemyAnim  = w;
    }
    public boolean isWaitingForPlayerAnim(){
        return waitingForPlayerAnim;
    }
    public boolean isWaitingForEnemyAnim(){
        return waitingForEnemyAnim;
    }
    public void setLastResult(BattleResult r){
        this.lastResult = r;
    }
    public BattleResult getLastResult(){
        return lastResult;
    }

    public BattlePlayer getPlayer(){
        return player;
    }

    public BattleEnemy getEnemy(){
        return enemy;
    }

    public int getTalkCount(){
        return talkCount;
    }

    public int getTurnCount(){
        return turnCount;
    }

    public boolean isPlayerTurn(){
        return playerTurn
    }

}