package com.platformer.battle.core;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.entities.BattleEnemy;
import com.platformer.battle.entities.BattlePlayer;
import com.platformer.core.BattleSnapshot;
import com.platformer.input.InputHandler;
import com.platformer.battle.animation.SpriteSheet;

import java.awt.Graphics;
import java.util.function.Consumer;

public class BattleManager {

    private BattleState battleState;
    private Consumer<BattleOutcome> onDone;

    public void init(BattleSnapshot snapshot,
            BattleEnemy enemy,
            InputHandler input,
            Consumer<BattleOutcome> onDone) {

        this.onDone = onDone;

        BattlePlayer battlePlayer = new BattlePlayer(snapshot);
        SpriteSheet playerSheet = new SpriteSheet("/res/player_sprites.png", 64, 40);
        battlePlayer.initAnimations(playerSheet);
        BattleContext ctx = new BattleContext(battlePlayer, enemy);

        battleState = new BattleState(ctx, input, this::onBattleEnd);
        battleState.onEnter();
    }

    private void onBattleEnd(BattleOutcome outcome) {
        battleState.onExit();
        onDone.accept(outcome);
    }

    public void update(float dt) {
        if (battleState != null) {
            battleState.update(dt);
        }
    }

    public void draw(Graphics g) {
        if (battleState != null) {
            battleState.draw(g);
        }
    }

    public void handleInput() {
        if (battleState != null) {
            battleState.handleInput();
        }
    }
}
