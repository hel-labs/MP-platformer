package com.platformer.battle.core;

public class BattleOutcome {

    public enum Result { WIN, LOSE, FLEE }

    public final Result result;
    public final int    hpRemaining;

    public BattleOutcome(Result result, int hpRemaining) {
        this.result      = result;
        this.hpRemaining = hpRemaining;
    }

    public boolean isWin()  { return result == Result.WIN;  }
    public boolean isLose() { return result == Result.LOSE; }
    public boolean isFlee() { return result == Result.FLEE; }
}