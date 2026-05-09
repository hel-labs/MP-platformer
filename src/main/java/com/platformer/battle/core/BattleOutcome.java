package com.platformer.battle.core;

public class BattleOutcome {

    // Enum for all possible outcomes
    public enum Result {
        WIN, LOSE, FLEE
    }

    public final Result result;
    public final int hpRemaining;

    // Creates the result of the battle; used by Game.java
    public BattleOutcome(Result result, int hpRemaining) {
        this.result = result;
        this.hpRemaining = hpRemaining;
    }

    // Accessors
    public boolean isWin() {
        return result == Result.WIN;
    }

    public boolean isLose() {
        return result == Result.LOSE;
    }

    public boolean isFlee() {
        return result == Result.FLEE;
    }
}
