package com.platformer.core;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import come.platformer.input.InputHandler;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;
    private final InputHandler input;

	public GamePanel(Game game) {
		this.game = game;
		setPanelSize();
        input.install(this);
		mouseInputs = new MouseInputs(this);
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}

	public void updateGame() {
        input.tick();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
    public InputHandler getInput(){
        return input;
    }
}