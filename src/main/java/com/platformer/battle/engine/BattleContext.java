package com.platformer.battle.engine;

import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.entities.BattlePlayer;

public class BattleContext {

    private final BattlePlayer player;
    private final BattleEnemy enemy;

    private int talkCount = 0;
    private int turnCount = 0;
    private boolean playerTurn = true;
    private int hostility;

    public static final int MAX_HOSTILITY = 5;
    public static final int MIN_HOSTILITY = 0;

    private BattleResult lastResult = null;

    private boolean waitingForPlayerAnim = false;
    private boolean waitingForEnemyAnim = false;

    public BattleContext(BattlePlayer player, BattleEnemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.hostility = enemy.getBaseHostility();
    }

    public void applyHostilityDelta(int delta) {
        hostility = Math.max(MIN_HOSTILITY,
                Math.min(MAX_HOSTILITY, hostility + delta));
    }

    public void setWaitingForPlayerAnim(boolean waiting) {
        this.waitingForPlayerAnim = waiting;
    }

    public void setWaitingForEnemyAnim(boolean waiting) {
        this.waitingForEnemyAnim = waiting;
    }

    public boolean isMercyAvailable() {
        return hostility <= MIN_HOSTILITY;
    }

    public int getHostilityDamageBonus() {
        return hostility;
    }

    public int getHostility() {
        return hostility;
    }

    public void incrementTalkCount() {
        talkCount++;
    }

    public void incrementTurnCount() {
        turnCount++;
    }

    public void setPlayerTurn(boolean b) {
        playerTurn = b;
    }

    public void setLastResult(BattleResult r) {
        lastResult = r;
    }

    public BattlePlayer getPlayer() {
        return player;
    }

    public BattleEnemy getEnemy() {
        return enemy;
    }

    public int getTalkCount() {
        return talkCount;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public BattleResult getLastResult() {
        return lastResult;
    }
}
