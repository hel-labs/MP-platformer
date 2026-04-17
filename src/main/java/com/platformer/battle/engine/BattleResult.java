package com.platformer.battle.engine;

public class BattleResult{
    public enum Type{
        PLAYER_ATTACKED,
        ENEMY_ATTACKED,
        PLAYER_FLED,
        FLEE_FAILED,
        ENEMY_DEFEATED,
        PLAYER_DEFEATED,
        MERCY_GRANTED,
        TALK_INITIATED,
        TALKED,
        HINT
    }

    private final Type type;
    private final String message;
    private final int damageDealt;

    private BattleResult(Type type, String message, int damageDealt){
        this.type=type;
        this.message=message;
        this.damageDealt=damageDealt;
    }

    public static BattleResult playerAttacked(int damage, String message){
        return new BattleResult(Type.PLAYER_ATTACKED, message, damage);
    }
    public static BattleResult enemyAttacked(int damage, String message){
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
    public static BattleResult talkInitiated() {
        return new BattleResult(Type.TALK_INITIATED, "", 0);
    }
    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public int getDamageDealt() {
        return damageDealt;
    }
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