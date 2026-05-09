package com.platformer.battle.core;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.entities.BattlePlayer;
import com.platformer.core.BattleSnapshot;
import com.platformer.input.InputHandler;
import com.platformer.battle.animation.SpriteSheet;
import com.platformer.utils.AudioPlayer;

import java.awt.Graphics;
import java.util.function.Consumer;

public class BattleManager {

    private BattleState battleState;
    private Consumer<BattleOutcome> onDone;

    public void init(BattleSnapshot snapshot,
            BattleEnemy enemy,
            InputHandler input,
            AudioPlayer audioPlayer,
            Consumer<BattleOutcome> onDone) {
        // Battle initialization, loads all necessary objects
        this.onDone = onDone;

        // Creates battle player object from current overworld player
        BattlePlayer battlePlayer = new BattlePlayer(snapshot);
        SpriteSheet playerSheet = new SpriteSheet("/res/player_sprites.png", 64, 40);
        battlePlayer.initAnimations(playerSheet);

        // Creates the battlecontext object for the battle with combatans
        BattleContext ctx = new BattleContext(battlePlayer, enemy);

        // Creates and enters battle state
        battleState = new BattleState(ctx, input, audioPlayer, this::onBattleEnd);
        battleState.onEnter();
    }

    private void onBattleEnd(BattleOutcome outcome) {
        // forwards battle outcome to game.java
        battleState.onExit();
        onDone.accept(outcome);
    }

    // Forward tick to battlestate
    public void update(float dt) {
        if (battleState != null) {
            battleState.update(dt);
        }
    }

    // draws the battlestate 
    public void draw(Graphics g) {
        if (battleState != null) {
            battleState.draw(g);
        }
    }

    // FOrward input handling to battlestate
    public void handleInput() {
        if (battleState != null) {
            battleState.handleInput();
        }
    }
}