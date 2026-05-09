package com.platformer.gamestate;

/**
 * Enumerates all high-level game screens and modes.
 */
public enum Gamestate {

    TITLE, NAME_ENTRY, MENU, PLAYING, BATTLE, GAME_OVER, LEADERBOARD, CREDITS, OPTIONS, QUIT;

    /** Currently active game state. */
    public static Gamestate state = TITLE;

}
