package com.platformer.exceptions;

public class EnemyAlreadyDeadException extends BattleException {
    public EnemyAlreadyDeadException(String enemyName) {
        super("Cannot act on defeated enemy: " + enemyName);
    }
}