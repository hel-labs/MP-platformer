package com.echoes.battle;

/**
 * Immutable result returned by every BattleAction and by the enemy turn.
 *
 * Instead of throwing exceptions for expected outcomes (flee fails, mercy
 * not ready), actions return a BattleResult describing what happened.
 * Exceptions are reserved for genuine bugs.
 *
 * Use the static factory methods to construct results — never new BattleResult().
 */
public class BattleResult {

    public enum Type {
        PLAYER_ATTACKED,    // player successfully hit the enemy
        ENEMY_ATTACKED,     // enemy successfully hit the player
        PLAYER_FLED,        // player escaped successfully
        FLEE_FAILED,        // flee attempt failed
        ENEMY_DEFEATED,     // enemy HP reached zero from fight
        PLAYER_DEFEATED,    // player HP reached zero
        MERCY_GRANTED,      // player spared the enemy after mercy condition met
        TALKED,             // player talked — response returned
        HINT                // spare attempted too early — hint returned
    }

    private final Type   type;
    private final String message;
    private final int    damageDealt;

    private BattleResult(Type type, String message, int damageDealt) {
        this.type        = type;
        this.message     = message;
        this.damageDealt = damageDealt;
    }

    // ---------------------------------------------------------------
    // Static factory methods — one per outcome type
    // ---------------------------------------------------------------

    public static BattleResult playerAttacked(int damage, String message) {
        return new BattleResult(Type.PLAYER_ATTACKED, message, damage);
    }

    public static BattleResult enemyAttacked(int damage, String message) {
        return new BattleResult(Type.ENEMY_ATTACKED, message, damage);
    }

    public static BattleResult fled() {
        return new BattleResult(Type.PLAYER_FLED,
                "* You fled to safety!", 0);
    }

    public static BattleResult fleeFailed() {
        return new BattleResult(Type.FLEE_FAILED,
                "* You couldn't escape!", 0);
    }

    public static BattleResult enemyDefeated(String enemyName) {
        return new BattleResult(Type.ENEMY_DEFEATED,
                "* " + enemyName + " was defeated.", 0);
    }

    public static BattleResult playerDefeated() {
        return new BattleResult(Type.PLAYER_DEFEATED,
                "* You were defeated...", 0);
    }

    public static BattleResult mercyGranted(String enemyName) {
        return new BattleResult(Type.MERCY_GRANTED,
                "* You spared " + enemyName + ". They seem grateful.", 0);
    }

    public static BattleResult talked(String enemyResponse) {
        return new BattleResult(Type.TALKED, enemyResponse, 0);
    }

    public static BattleResult hint(String hintMessage) {
        return new BattleResult(Type.HINT, hintMessage, 0);
    }

    // ---------------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------------

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    /**
     * True when the battle is over after this result.
     * The battle state should stop accepting input and transition out.
     */
    public boolean isTerminal() {
        return type == Type.ENEMY_DEFEATED
            || type == Type.PLAYER_DEFEATED
            || type == Type.PLAYER_FLED
            || type == Type.MERCY_GRANTED;
    }

    @Override
    public String toString() {
        return "BattleResult{type=" + type
             + ", damage=" + damageDealt
             + ", msg='" + message + "'}";
    }
}
