package com.platformer.core;

import java.awt.Graphics;

import com.platformer.overworld.audio.AudioPlayer;
import com.platformer.gamestate.GameState;
import com.platformer.overworld.states.Menu;
import com.platformer.overworld.states.Playing;
import com.platformer.overworld.ui.AudioOptions;

import com.platformer.battle.core.BattleManager;
import com.platformer.core.BattleSnapshot;
import com.platformer.battle.core.BattleOutcome;
import com.platformer.battle.entities.BattleEnemy;

import com.platformer.gamestate.Credits;
import com.platformer.gamestate.GameOptions;

public class Game implements Runnable {

    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    private Credits credits;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    private BattleManager battleManager;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    private final boolean SHOW_FPS_UPS = true;

    public Game() {
        System.out.println("size: " + GAME_WIDTH + " : " + GAME_HEIGHT);

        gamePanel = new GamePanel(this);

        initClasses();

        new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();
        startGameLoop();
    }

    private void initClasses() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();

        menu = new Menu(this, gamePanel.getInput());
        playing = new Playing(this, gamePanel.getInput());

        credits = new Credits(this);
        gameOptions = new GameOptions(this);

        battleManager = new BattleManager(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (GameState.state) {
            case MENU -> menu.update();

            case PLAYING -> playing.update();

            case OPTIONS -> gameOptions.update();

            case BATTLE -> {
                battleManager.handleInput();
                battleManager.update(1f / UPS_SET);
            }

            case CREDITS -> credits.update();

            case GAME_OVER -> {
                // TODO: handle game over screen
            }

            case QUIT -> System.exit(0);
        }
    }

    @SuppressWarnings("incomplete-switch")
    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU -> menu.draw(g);

            case PLAYING -> playing.draw(g);

            case OPTIONS -> gameOptions.draw(g);

            case BATTLE -> battleManager.draw(g);

            case CREDITS -> credits.draw(g);

            case GAME_OVER -> {
                // optionally draw game over screen
            }
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (SHOW_FPS_UPS) {
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;
                }
            }
        }
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirBooleans();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Credits getCredits() {
        return credits;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void startBattle(BattleSnapshot snapshot, BattleEnemy enemy) {
        battleManager.init(snapshot, enemy, gamePanel.getInput(), this::onBattleEnd);
        GameState.state = GameState.BATTLE;
    }

    private void onBattleEnd(BattleOutcome outcome) {
        playing.applyBattleOutcome(outcome);

        if (outcome.isWin() || outcome.isFlee()) {
            GameState.state = GameState.PLAYING;
        } else {
            GameState.state = GameState.GAME_OVER;
        }
    }
}