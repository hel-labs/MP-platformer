package com.platformer.overworld.levels;

public class RiddleData {

    public final String npcDialogue;
    public final String riddleStatement;
    public final boolean answer;

    public RiddleData(String npcDialogue, String riddleStatement, boolean answer) {
        this.npcDialogue = npcDialogue;
        this.riddleStatement = riddleStatement;
        this.answer = answer;
    }

    private static final RiddleData[] RIDDLES = {
        new RiddleData(
            "Halt, traveler. You shall not pass without answering my riddle.",
            "The sun rises in the west.",
            false
        ),
        new RiddleData(
            "You've come far. But wisdom is the final key.",
            "All prime numbers are odd.",
            false
        ),
        new RiddleData(
            "The final gate. Only truth will open it.",
            "A square is always a rectangle.",
            true
        )
    };

    public static RiddleData forLevel(int levelIndex) {
        if (levelIndex < 0 || levelIndex >= RIDDLES.length) {
            return RIDDLES[0];
        }
        return RIDDLES[levelIndex];
    }
}