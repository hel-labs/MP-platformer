package com.echoes.riddle;

/**
 * A single riddle challenge.
 * Holds a question, an array of answer choices, and the index of the
 * correct answer. Completely dumb data object — no game logic here.
 */
public class Riddle {

    private final String   question;
    private final String[] choices;
    private final int      correctIndex;

    /**
     * @param question     the riddle text shown to the player
     * @param choices      array of answer options (2-4 recommended)
     * @param correctIndex 0-based index of the correct choice
     */
    public Riddle(String question, String[] choices, int correctIndex) {
        if (choices == null || choices.length < 2)
            throw new IllegalArgumentException(
                "Riddle must have at least 2 choices");
        if (correctIndex < 0 || correctIndex >= choices.length)
            throw new IllegalArgumentException(
                "correctIndex out of bounds: " + correctIndex);

        this.question     = question;
        this.choices      = choices;
        this.correctIndex = correctIndex;
    }

    /**
     * Returns true if the selected index is the correct answer.
     * @param selectedIndex the index the player chose
     */
    public boolean attempt(int selectedIndex) {
        return selectedIndex == correctIndex;
    }

    public String   getQuestion()    { return question; }
    public String[] getChoices()     { return choices;  }
    public int      getCorrectIndex(){ return correctIndex; }
}
