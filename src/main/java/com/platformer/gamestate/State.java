package com.platformer.gamestate;

import java.awt.event.MouseEvent;

import com.platformer.utils.AudioPlayer;
import com.platformer.core.Game;
import com.platformer.overworld.ui.MenuButton;

/**
 * Base class for screen states that share helpers and game access.
 */
public class State {

    protected Game game;

    /**
     * @param game owning game instance
     */
    public State(Game game) {
        this.game = game;
    }

    /**
     * Checks whether a mouse event is inside a menu button.
     *
     * @param e mouse event
     * @param mb target button
     * @return true if the event is within the button bounds
     */
    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * @return owning game instance
     */
    public Game getGame() {
        return game;
    }

    @SuppressWarnings("incomplete-switch")
    /**
     * Switches the active game state and updates background music.
     *
     * @param state new game state
     */
    public void setGamestate(Gamestate state) {
        switch (state) {
            case MENU ->
                game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING ->
                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }

        Gamestate.state = state;
    }

}
