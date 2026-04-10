package com.echoes.battle;

import com.echoes.entities.Enemy;
import com.echoes.entities.Player;

/**
 * Shared state object passed to every component during a battle.
 * Nobody owns BattleContext — everyone reads from and writes to it.
 *
 * Created fresh at the start of each battle and discarded when it ends.
 * The BattleEngine and all BattleActions receive this as a parameter.
 */
public class BattleContext {

    private final Player player;
    private final Enemy  enemy;

    private int     talkCount  = 0;   // how many times player used Talk
    private int     turnCount  = 0;   // how many full rounds have passed
    private boolean playerTurn = true; // whose turn it currently is

    // Animation phase tracking — used by BattleState to sequence animations
    private boolean waitingForPlayerAnim = false;
    private boolean waitingForEnemyAnim  = false;

    // Last result — stored so BattleState can read it after the fact
    private BattleResult lastResult = null;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public BattleContext(Player player, Enemy enemy) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        if (enemy  == null) throw new IllegalArgumentException("Enemy cannot be null");
        this.player = player;
        this.enemy  = enemy;
    }

    // ---------------------------------------------------------------
    // Turn management
    // ---------------------------------------------------------------

    public void incrementTalkCount() { talkCount++; }
    public void incrementTurnCount() { turnCount++; }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    // ---------------------------------------------------------------
    // Animation gating
    // ---------------------------------------------------------------

    public void setWaitingForPlayerAnim(boolean waiting) {
        this.waitingForPlayerAnim = waiting;
    }

    public void setWaitingForEnemyAnim(boolean waiting) {
        this.waitingForEnemyAnim = waiting;
    }

    public boolean isWaitingForPlayerAnim() { return waitingForPlayerAnim; }
    public boolean isWaitingForEnemyAnim()  { return waitingForEnemyAnim;  }

    // ---------------------------------------------------------------
    // Last result
    // ---------------------------------------------------------------

    public void setLastResult(BattleResult result) {
        this.lastResult = result;
    }

    public BattleResult getLastResult() { return lastResult; }

    // ---------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------

    public Player  getPlayer()    { return player;     }
    public Enemy   getEnemy()     { return enemy;      }
    public int     getTalkCount() { return talkCount;  }
    public int     getTurnCount() { return turnCount;  }
    public boolean isPlayerTurn() { return playerTurn; }
}
