package com.platformer.overworld.states;

import static com.platformer.gamestate.GameState.MENU;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.platformer.input.InputHandler;
import com.platformer.core.Game;
import com.platformer.gamestate.GameState;
import com.platformer.overworld.effects.DialogueEffect;
import com.platformer.overworld.effects.Rain;
import com.platformer.overworld.entities.EnemyManager;
import com.platformer.overworld.entities.Player;
import com.platformer.overworld.levels.LevelManager;
import com.platformer.overworld.objects.ObjectManager;
import com.platformer.overworld.ui.GameCompletedOverlay;
import com.platformer.overworld.ui.GameOverOverlay;
import com.platformer.overworld.ui.LevelCompletedOverlay;
import com.platformer.overworld.ui.PauseOverlay;
import com.platformer.overworld.utils.LoadSave;

public class Playing {

	private final Game game;
	private final InputHandler input;
	private final Player player;
	private final LevelManager levelManager;
	private final EnemyManager enemyManager;
	private final ObjectManager objectManager;

	private final PauseOverlay pauseOverlay;
	private final GameOverOverlay gameOverOverlay;
	private final LevelCompletedOverlay levelCompletedOverlay;
	private final GameCompletedOverlay gameCompletedOverlay;

	private final ArrayList<DialogueEffect> dialogues = new ArrayList<>();

	private int xLvlOffset;
	private int leftBorder = (int) (0.2f * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8f * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private boolean paused;
	private boolean gameOver;
	private boolean levelCompleted;
	private boolean gameCompleted;

	private final Rain rain;
	private final BufferedImage dialogueBubble;

	public Playing(Game game, InputHandler input) {
		this.game = game;
		this.input=input;

		levelManager = new LevelManager(game);
		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());

		enemyManager = new EnemyManager(this);
		enemyManager.loadEnemies(levelManager.getCurrentLevel());

		objectManager = new ObjectManager(this);
		objectManager.loadObjects(levelManager.getCurrentLevel());

		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

		rain = new Rain();
		dialogueBubble = LoadSave.GetSpriteAtlas(LoadSave.DIALOGUE_BUBBLE_ATLAS);

		calcLvlOffset();
	}

	private void calcLvlOffset() {
		int levelTilesWide = levelManager.getCurrentLevel().getLevelData()[0].length;
		int maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	public void update() {
		handleInput();

		if (paused) {
			pauseOverlay.update();
			return;
		}
		if (gameOver) {
			gameOverOverlay.update();
			return;
		}
		if (levelCompleted) {
			if (levelManager.getLevelIndex() >= levelManager.getAmountOfLevels() - 1) {
				gameCompleted = true;
				gameCompletedOverlay.update();
			} else {
				levelCompletedOverlay.update();
			}
			return;
		}

		levelManager.update();
		player.update();
		checkCloseToBorder();

		enemyManager.update(levelManager.getCurrentLevel().getLevelData());
		objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
		objectManager.checkObjectTouched(player.getHitbox());

		rain.update(xLvlOffset);
		updateDialogues();

		if (player.getCurrentHealth() <= 0) {
			gameOver = true;
		}
	}
	private void handleInput(){

		if (input.isJustPressed(InputHandler.ESCAPE)) {
            paused = !paused;
        }
		if(gameOver || levelCompleted||paused){
			return;
		}


		player.setLeft(input.isHeld(InputHandler.LEFT));
        player.setRight(input.isHeld(InputHandler.RIGHT));
        player.setJump(input.isHeld(InputHandler.UP));

		if (input.isJustPressed(InputHandler.CANCEL)) {
            setGamestate(MENU);
        }
	}

	private void updateDialogues() {
		for (int i = dialogues.size() - 1; i >= 0; i--) {
			DialogueEffect d = dialogues.get(i);
			d.update();
			if (!d.isActive()) {
				dialogues.remove(i);
			}
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder) {
			xLvlOffset += diff - rightBorder;
		} else if (diff < leftBorder) {
			xLvlOffset += diff - leftBorder;
		}

		if (xLvlOffset < 0) {
			xLvlOffset = 0;
		} else if (xLvlOffset > maxLvlOffsetX) {
			xLvlOffset = maxLvlOffsetX;
		}
	}

	public void draw(Graphics g) {
		g.setColor(new Color(150, 200, 255));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		rain.draw(g, xLvlOffset);
		levelManager.draw(g, xLvlOffset);
		objectManager.drawBackgroundTrees(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		drawDialogues(g);

		if (paused) {
			pauseOverlay.draw(g);
		} else if (gameOver) {
			gameOverOverlay.draw(g);
		} else if (levelCompleted) {
			if (gameCompleted) {
				gameCompletedOverlay.draw(g);
			} else {
				levelCompletedOverlay.draw(g);
			}
		}
	}

	private void drawDialogues(Graphics g) {
		if (dialogueBubble == null) {
			return;
		}

		int w = 14;
		int h = 12;

		for (DialogueEffect d : dialogues) {
			int x = d.getAniIndex() * w;
			int y = d.getType() * h;
			g.drawImage(dialogueBubble.getSubimage(x, y, w, h), d.getX() - xLvlOffset, d.getY() - (int) (20 * Game.SCALE), (int) (w * Game.SCALE), (int) (h * Game.SCALE), null);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseDragged(e);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (paused) {
			pauseOverlay.mousePressed(e);
			return;
		}

		if (gameOver) {
			gameOverOverlay.mousePressed(e);
			return;
		}

		if (levelCompleted) {
			if (gameCompleted) {
				gameCompletedOverlay.mousePressed(e);
			} else {
				levelCompletedOverlay.mousePressed(e);
			}
			return;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseReleased(e);
			return;
		}

		if (gameOver) {
			gameOverOverlay.mouseReleased(e);
			return;
		}

		if (levelCompleted) {
			if (gameCompleted) {
				gameCompletedOverlay.mouseReleased(e);
			} else {
				levelCompletedOverlay.mouseReleased(e);
			}
			return;
		}

		if (e.getButton() == MouseEvent.BUTTON1) {
			player.setAttacking(true);
			enemyManager.checkEnemyHit(player.getAttackBox());
			objectManager.checkObjectHit(player.getAttackBox());
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (paused) {
			pauseOverlay.mouseMoved(e);
		} else if (gameOver) {
			gameOverOverlay.mouseMoved(e);
		} else if (levelCompleted) {
			if (gameCompleted) {
				gameCompletedOverlay.mouseMoved(e);
			} else {
				levelCompletedOverlay.mouseMoved(e);
			}
		}
	}	

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public void resetAll() {
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();

		xLvlOffset = 0;
		gameOver = false;
		levelCompleted = false;
		paused = false;
	}

	public void resetGameCompleted() {
		gameCompleted = false;
	}

	public void loadNextLevel() {
		levelManager.loadNextLevel();
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
		calcLvlOffset();
		resetAll();
	}

	public void addDialogue(int x, int y, int type) {
		dialogues.add(new DialogueEffect(x, y, type));
	}

	public void unpauseGame() {
		paused = false;
	}

	public void setGamestate(GameState state) {
		GameState.state = state;
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.levelCompleted = levelCompleted;
	}

	public Player getPlayer() {
		return player;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public Game getGame() {
		return game;
	}

	//Incomplete implementation for battle transition
	private void detectEnemyContact(){
		//Create battle snaps from overowlrd
		BattleSnapshot snapshot = new BattleSnapshot(player);
		setGamestate(BATTLE);
		Game.startBattle(snapshot, enemy);
	}
}
