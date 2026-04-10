package com.echoes.core;

import com.echoes.battle.*;
import com.echoes.battle.actions.BattleAction;
import com.echoes.dialogue.DialogueBox;
import com.echoes.entities.Enemy;
import com.echoes.entities.Player;
import com.echoes.exceptions.GameException;
import com.echoes.input.InputHandler;
import com.echoes.ui.BattleUI;
import com.echoes.utils.GameLogger;

import java.awt.*;
import java.util.List;

/**
 * BattleState drives the complete Undertale-style battle loop.
 *
 * Phase machine:
 *
 *   ENCOUNTER_DIALOGUE
 *       ↓ Z pressed (dialogue done)
 *   PLAYER_TURN          ← player selects action
 *       ↓ action chosen
 *   PLAYER_ANIM_WAIT     ← wait for player attack animation (if fight)
 *       ↓ animation done
 *   PLAYER_RESULT        ← show result dialogue
 *       ↓ Z pressed
 *   ENEMY_TURN_DELAY     ← 1.2s pause before enemy acts
 *       ↓ timer expires
 *   ENEMY_ANIM_WAIT      ← wait for enemy hurt animation
 *       ↓ animation done
 *   ENEMY_RESULT         ← show enemy action result dialogue
 *       ↓ Z pressed
 *   PLAYER_TURN          ← loop repeats
 *
 *   Any terminal BattleResult → TERMINAL phase
 *       ↓ Z pressed
 *   Battle ends → caller switches game state
 *
 * This class is self-contained for the standalone engine.
 * When integrating with the full game, replace the direct
 * BattleTest.endBattle() call with ctx.setState(new OverworldState()).
 */
public class BattleState {

    // ---------------------------------------------------------------
    // Phase enum
    // ---------------------------------------------------------------

    private enum Phase {
        ENCOUNTER_DIALOGUE,
        PLAYER_TURN,
        PLAYER_ANIM_WAIT,
        PLAYER_RESULT,
        ENEMY_TURN_DELAY,
        ENEMY_ANIM_WAIT,
        ENEMY_RESULT,
        TERMINAL
    }

    // ---------------------------------------------------------------
    // Dependencies
    // ---------------------------------------------------------------

    private final Enemy        enemy;
    private final Player       player;
    private final BattleEngine engine;
    private final BattleUI     ui;
    private final DialogueBox  dialogueBox;
    private       BattleContext ctx;

    // ---------------------------------------------------------------
    // State
    // ---------------------------------------------------------------

    private Phase phase           = Phase.ENCOUNTER_DIALOGUE;
    private int   selectedAction  = 0;
    private float enemyTurnTimer  = 0f;
    private BattleResult lastResult = null;

    private static final float ENEMY_TURN_DELAY = 1.2f; // seconds

    // Callback for when battle ends — used by standalone test runner
    // In the full game this becomes ctx.setState(new OverworldState())
    private Runnable onBattleEnd = () -> {};

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    public BattleState(Player player, Enemy enemy) {
        this.player      = player;
        this.enemy       = enemy;
        this.engine      = new BattleEngine();
        this.ui          = new BattleUI();
        this.dialogueBox = new DialogueBox();
        this.ctx         = new BattleContext(player, enemy);
    }

    // ---------------------------------------------------------------
    // Lifecycle
    // ---------------------------------------------------------------

    /** Called once when entering this battle. */
    public void onEnter() {
        GameLogger.get().info("Battle started: " + enemy.getName());
        dialogueBox.setText(enemy.getEncounterDialogue());
        phase = Phase.ENCOUNTER_DIALOGUE;
    }

    /** Called once when leaving this battle. */
    public void onExit() {
        GameLogger.get().info("Battle ended: " + enemy.getName());
    }

    // ---------------------------------------------------------------
    // Update — called every tick
    // ---------------------------------------------------------------

    public void update(float dt) {
        // Always update animations
        player.update(dt);
        enemy.update(dt);
        dialogueBox.update(dt);

        switch (phase) {

            case PLAYER_ANIM_WAIT -> {
                // Wait for the player's attack animation to finish
                if (player.attackAnimationFinished()) {
                    phase = Phase.PLAYER_RESULT;
                    dialogueBox.setText(lastResult.getMessage());
                }
            }

            case ENEMY_TURN_DELAY -> {
                enemyTurnTimer += dt;
                if (enemyTurnTimer >= ENEMY_TURN_DELAY) {
                    enemyTurnTimer = 0f;
                    executeEnemyTurn();
                }
            }

            case ENEMY_ANIM_WAIT -> {
                // Wait for enemy's reaction animation (hurt / attack)
                if (enemy.getAnimator().currentFinished()
                        || !enemy.getAnimator().has("hurt")) {
                    phase = Phase.ENEMY_RESULT;
                    dialogueBox.setText(lastResult.getMessage());
                }
            }

            default -> { /* other phases driven by input */ }
        }
    }

    // ---------------------------------------------------------------
    // Render — called every tick
    // ---------------------------------------------------------------

    public void render(Graphics2D g) {
        boolean showMenu = (phase == Phase.PLAYER_TURN);
        List<BattleAction> actions = engine.getPlayerActions();
        ui.render(g, ctx, selectedAction, actions, showMenu, dialogueBox);
    }

    // ---------------------------------------------------------------
    // Input — called every tick before update
    // ---------------------------------------------------------------

    public void handleInput(InputHandler input) {
        switch (phase) {

            case ENCOUNTER_DIALOGUE -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) dialogueBox.skipToEnd();
                    else phase = Phase.PLAYER_TURN;
                }
            }

            case PLAYER_TURN -> {
                int actionCount = engine.getActionCount();

                if (input.isJustPressed(InputHandler.UP)) {
                    selectedAction = Math.floorMod(selectedAction - 2, actionCount);
                }
                if (input.isJustPressed(InputHandler.DOWN)) {
                    selectedAction = (selectedAction + 2) % actionCount;
                }
                if (input.isJustPressed(InputHandler.LEFT)) {
                    selectedAction = Math.floorMod(selectedAction - 1, actionCount);
                }
                if (input.isJustPressed(InputHandler.RIGHT)) {
                    selectedAction = (selectedAction + 1) % actionCount;
                }

                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    executePlayerAction(selectedAction);
                }
            }

            case PLAYER_RESULT -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) {
                        dialogueBox.skipToEnd();
                    } else {
                        if (lastResult.isTerminal()) {
                            phase = Phase.TERMINAL;
                            dialogueBox.setText(getTerminalMessage(lastResult));
                        } else {
                            // Move to enemy turn
                            phase = Phase.ENEMY_TURN_DELAY;
                            enemyTurnTimer = 0f;
                        }
                    }
                }
            }

            case ENEMY_RESULT -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) {
                        dialogueBox.skipToEnd();
                    } else {
                        if (lastResult.isTerminal()) {
                            phase = Phase.TERMINAL;
                            dialogueBox.setText(getTerminalMessage(lastResult));
                        } else {
                            phase = Phase.PLAYER_TURN;
                        }
                    }
                }
            }

            case TERMINAL -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) {
                        dialogueBox.skipToEnd();
                    } else {
                        onExit();
                        onBattleEnd.run();
                    }
                }
            }

            default -> { /* PLAYER_ANIM_WAIT, ENEMY_TURN_DELAY, ENEMY_ANIM_WAIT
                            are time-driven — no input processed */ }
        }
    }

    // ---------------------------------------------------------------
    // Action execution
    // ---------------------------------------------------------------

    private void executePlayerAction(int actionIndex) {
        try {
            lastResult = engine.executePlayerAction(actionIndex, ctx);
            ctx.setLastResult(lastResult);

            if (lastResult.isTerminal()) {
                // Terminal results skip animation wait
                phase = Phase.PLAYER_RESULT;
                dialogueBox.setText(lastResult.getMessage());
                return;
            }

            // For Fight — wait for attack animation before showing result
            if (actionIndex == 0 /* FightAction */) {
                phase = Phase.PLAYER_ANIM_WAIT;
                // dialogue will be set when animation finishes
            } else {
                // Talk, Spare, Flee — no animation wait
                phase = Phase.PLAYER_RESULT;
                dialogueBox.setText(lastResult.getMessage());
            }

        } catch (GameException e) {
            GameLogger.get().error("Player action error", e);
            dialogueBox.setText("* Something went wrong...");
            phase = Phase.PLAYER_TURN;
        }
    }

    private void executeEnemyTurn() {
        try {
            lastResult = engine.executeEnemyTurn(ctx);
            ctx.setLastResult(lastResult);

            // Trigger enemy attack animation
            if (enemy.getAnimator().has("attack")) {
                enemy.getAnimator().forcePlay("attack");
                phase = Phase.ENEMY_ANIM_WAIT;
            } else {
                // No animation — go straight to result
                phase = Phase.ENEMY_RESULT;
                dialogueBox.setText(lastResult.getMessage());
            }

        } catch (GameException e) {
            GameLogger.get().error("Enemy turn error", e);
            phase = Phase.PLAYER_TURN;
        }
    }

    // ---------------------------------------------------------------
    // Terminal message
    // ---------------------------------------------------------------

    private String getTerminalMessage(BattleResult result) {
        return switch (result.getType()) {
            case ENEMY_DEFEATED  -> result.getMessage()
                                  + "\n* The path is clear.";
            case MERCY_GRANTED   -> result.getMessage()
                                  + "\n* The slime waves goodbye.";
            case PLAYER_FLED     -> result.getMessage();
            case PLAYER_DEFEATED -> result.getMessage()
                                  + "\n\n* Press Z to try again.";
            default              -> result.getMessage();
        };
    }

    // ---------------------------------------------------------------
    // Configuration
    // ---------------------------------------------------------------

    /**
     * Set a callback to invoke when the battle finishes.
     * In the standalone runner this exits the program or restarts.
     * In the full game this calls ctx.setState(new OverworldState()).
     */
    public void setOnBattleEnd(Runnable callback) {
        this.onBattleEnd = callback;
    }

    public boolean isOver() {
        return phase == Phase.TERMINAL
            && dialogueBox.isFinished();
    }

    public BattleResult getLastResult() { return lastResult; }
}
