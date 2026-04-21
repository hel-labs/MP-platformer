package com.platformer.gamestate;

public enum Gamestate {

    TITLE, MENU, PLAYING, BATTLE, GAME_OVER, LEADERBOARD, CREDITS, OPTIONS, QUIT;

    public static Gamestate state = TITLE;

}
