package com.platformer.overworld.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.platformer.input.InputHandler;
import com.platformer.core.Game;
import com.platformer.gamestate.GameState;

public class Menu {

    private final Game game;
    private final InputHandler input;

    public Menu(Game game, InputHandler input) {
        this.game = game;
        this.input = input;
    }

    public void update() {
        if (input.isJustPressed(InputHandler.CONFIRM)) {
            GameState.state = GameState.PLAYING;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawString("OVERWORLD MENU - PRESS Z",
                Game.GAME_WIDTH / 2 - 95,
                Game.GAME_HEIGHT / 2);
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public Game getGame() {
        return game;
    }
}