//needs refactoring
package com.platformer.battle.core;

import com.platformer.battle.*;
import com.platformer.battle.actions.BattleAction;
import com.platformer.battle.actions.TalkAction;
import com.platformer.dialogue.DialogueBox;
import com.platformer.entities.Enemy;
import com.platformer.entities.Player;
import com.platformer.exceptions.GameException;
import com.platformer.input.InputHandler;
import com.platformer.states.GameState;
import com.platformer.ui.BattleUI;
import com.platformer.utils.GameLogger;
import com.platformer.*;

import java.awt.*;
import java.util.List;


public class BattleState extends GameState {

    // ── Phases ───────────────────────────────────────────────────────
    private enum Phase {
        ENCOUNTER_DIALOGUE,
        PLAYER_TURN,
        TALK_SELECTION,
        PLAYER_ANIM_WAIT,
        PLAYER_RESULT,
        ENEMY_TURN_DELAY,
        ENEMY_ANIM_WAIT,
        ENEMY_RESULT,
        TERMINAL
    }

    // ── Dependencies ─────────────────────────────────────────────────
    private final BattleEnemy          enemy;
    private final BattlePlayer         player;
    private final BattleEngine   engine;
    private final BattleUI       ui;
    private final DialogueBox    dialogueBox;
    private final BattleContext  ctx;
    private final InputHandler input;

    // ── Mutable state ────────────────────────────────────────────────
    private Phase            phase           = Phase.ENCOUNTER_DIALOGUE;
    private int              selectedAction  = 0;
    private float            enemyTurnTimer  = 0f;
    private List<TalkOption> talkOptions     = List.of();
    private int              selectedTalkOpt = 0;
    private BattleResult     lastResult      = null;

    private static final float ENEMY_TURN_DELAY_SECS = 1.2f;

    // Fallback callback — used by BattleTest standalone runner.
    // Replaced automatically when gameCtx is provided.
    private Runnable onWin  = () -> {};
    private Runnable onLose = () -> {};
    private Runnable onFlee = () -> {};

    // ---------------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------------

    /**
     * Full game constructor — transitions back to overworld automatically.
     *
     * @param enemy   the enemy being fought
     * @param gameCtx shared game context
     */
    public BattleState(BattleContext ctx, InputHandler input) {
        this.enemy      = ctx.getEnemy();
        this.player     = ctx.getPlayer();
        this.ctx = ctx;
        this.engine     = new BattleEngine();
        this.ui         = new BattleUI();
        this.dialogueBox = new DialogueBox();
        this.battleCtx  = new BattleContext(player, enemy);
        this.input = input;

        // Wire exit callbacks to game state transitions
        // OverworldState and GameOverState are imported at runtime —
        // referenced by class name string to avoid circular imports
        // during incremental development. Replace with direct
        // constructors once those classes exist:
        //
        //   this.onWin  = () -> gameCtx.setState(new OverworldState(gameCtx));
        //   this.onLose = () -> gameCtx.setState(new GameOverState(gameCtx));
        //   this.onFlee = () -> gameCtx.setState(new OverworldState(gameCtx));
        //
        // For now they fall through to the standalone callbacks below.
        this.onWin  = () -> GameLogger.get().info("Battle won — wire OverworldState here");
        this.onLose = () -> GameLogger.get().info("Battle lost — wire GameOverState here");
        this.onFlee = () -> GameLogger.get().info("Player fled — wire OverworldState here");
    }

    /**
     * Standalone constructor — used by BattleTest with no overworld.
     * Use setOnWin/setOnLose/setOnFlee to register callbacks.
     */
    public BattleState(Player player, Enemy enemy) {
        this.enemy       = enemy;
        this.player      = player;
        this.gameCtx     = null;
        this.engine      = new BattleEngine();
        this.ui          = new BattleUI();
        this.dialogueBox = new DialogueBox();
        this.battleCtx   = new BattleContext(player, enemy);
    }

    // ---------------------------------------------------------------
    // GameState lifecycle
    // ---------------------------------------------------------------

    @Override
    public void onEnter() {
        GameLogger.get().info("Battle started: " + enemy.getName()
            + "  hostility=" + battleCtx.getHostility());
        player.setFrozen(true);
        dialogueBox.setText(enemy.getEncounterDialogue());
        phase = Phase.ENCOUNTER_DIALOGUE;
        // AudioSystem.get().playMusic(enemy.getBattleMusic()); ← uncomment when wired
    }

    @Override
    public void onExit() {
        GameLogger.get().info("Battle ended: " + enemy.getName());
        player.setFrozen(false);
        // AudioSystem.get().stopMusic(); ← uncomment when wired
        // AudioSystem.get().playMusic(gameCtx.getCurrentRoom().getMusicPath());
    }

    // ---------------------------------------------------------------
    // Update
    // ---------------------------------------------------------------

    @Override
    public void update(float dt) {
        player.update(dt);
        enemy.update(dt);
        dialogueBox.update(dt);

        switch (phase) {
            case PLAYER_ANIM_WAIT -> {
                if (player.attackAnimationFinished()) {
                    phase = Phase.PLAYER_RESULT;
                    dialogueBox.setText(lastResult.getMessage());
                }
            }
            case ENEMY_TURN_DELAY -> {
                enemyTurnTimer += dt;
                if (enemyTurnTimer >= ENEMY_TURN_DELAY_SECS) {
                    enemyTurnTimer = 0f;
                    executeEnemyTurn();
                }
            }
            case ENEMY_ANIM_WAIT -> {
                if (enemy.getAnimator().currentFinished()
                        || !enemy.getAnimator().has("hurt")) {
                    phase = Phase.ENEMY_RESULT;
                    dialogueBox.setText(lastResult.getMessage());
                }
            }
            default -> {}
        }
    }

    // ---------------------------------------------------------------
    // Render
    // ---------------------------------------------------------------

    @Override
    public void render(Graphics2D g) {
        boolean showAction = (phase == Phase.PLAYER_TURN);
        boolean showTalk   = (phase == Phase.TALK_SELECTION);

        ui.render(g, battleCtx,
                  selectedAction, engine.getPlayerActions(), showAction,
                  talkOptions,    selectedTalkOpt,           showTalk,
                  dialogueBox);
    }

    // ---------------------------------------------------------------
    // Input
    // ---------------------------------------------------------------

    @Override
    public void handleInput(InputHandler input) {
        switch (phase) {

            case ENCOUNTER_DIALOGUE -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) dialogueBox.skipToEnd();
                    else                           phase = Phase.PLAYER_TURN;
                }
            }

            case PLAYER_TURN -> {
                int n = engine.getActionCount();
                if (input.isJustPressed(InputHandler.UP))
                    selectedAction = Math.floorMod(selectedAction - 2, n);
                if (input.isJustPressed(InputHandler.DOWN))
                    selectedAction = (selectedAction + 2) % n;
                if (input.isJustPressed(InputHandler.LEFT))
                    selectedAction = Math.floorMod(selectedAction - 1, n);
                if (input.isJustPressed(InputHandler.RIGHT))
                    selectedAction = (selectedAction + 1) % n;
                if (input.isJustPressed(InputHandler.CONFIRM))
                    executePlayerAction(selectedAction);
            }

            case TALK_SELECTION -> {
                if (talkOptions.isEmpty()) { phase = Phase.PLAYER_TURN; return; }
                int n = talkOptions.size();
                if (input.isJustPressed(InputHandler.UP))
                    selectedTalkOpt = Math.floorMod(selectedTalkOpt - 1, n);
                if (input.isJustPressed(InputHandler.DOWN))
                    selectedTalkOpt = (selectedTalkOpt + 1) % n;
                if (input.isJustPressed(InputHandler.CONFIRM))
                    resolveTalkOption(talkOptions.get(selectedTalkOpt));
                if (input.isJustPressed(InputHandler.CANCEL))
                    phase = Phase.PLAYER_TURN;
            }

            case PLAYER_RESULT -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) {
                        dialogueBox.skipToEnd();
                    } else if (lastResult != null && lastResult.isTerminal()) {
                        phase = Phase.TERMINAL;
                        dialogueBox.setText(buildTerminalMessage(lastResult));
                    } else {
                        phase = Phase.ENEMY_TURN_DELAY;
                        enemyTurnTimer = 0f;
                    }
                }
            }

            case ENEMY_RESULT -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) {
                        dialogueBox.skipToEnd();
                    } else if (lastResult != null && lastResult.isTerminal()) {
                        phase = Phase.TERMINAL;
                        dialogueBox.setText(buildTerminalMessage(lastResult));
                    } else {
                        phase = Phase.PLAYER_TURN;
                    }
                }
            }

            case TERMINAL -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished()) {
                        dialogueBox.skipToEnd();
                    } else {
                        exitBattle();
                    }
                }
            }

            default -> {}
        }
    }

    // ---------------------------------------------------------------
    // Action resolution
    // ---------------------------------------------------------------

    private void executePlayerAction(int actionIndex) {
        try {
            lastResult = engine.executePlayerAction(actionIndex, battleCtx);
            battleCtx.setLastResult(lastResult);

            if (lastResult.isTerminal()) {
                phase = Phase.PLAYER_RESULT;
                dialogueBox.setText(lastResult.getMessage());
                return;
            }

            switch (lastResult.getType()) {
                case TALK_INITIATED -> {
                    talkOptions     = battleCtx.getEnemy().getTalkOptions(battleCtx.getTalkCount());
                    selectedTalkOpt = 0;
                    phase           = Phase.TALK_SELECTION;
                }
                case PLAYER_ATTACKED -> {
                    phase = Phase.PLAYER_ANIM_WAIT;
                }
                default -> {
                    phase = Phase.PLAYER_RESULT;
                    dialogueBox.setText(lastResult.getMessage());
                }
            }

        } catch (GameException e) {
            GameLogger.get().error("Player action error", e);
            dialogueBox.setText("* Something went wrong...");
            phase = Phase.PLAYER_TURN;
        }
    }

    private void resolveTalkOption(TalkOption option) {
        TalkAction talkAction = (TalkAction) engine.getPlayerActions().get(1);
        lastResult = talkAction.resolveOption(option, battleCtx);
        battleCtx.setLastResult(lastResult);

        GameLogger.get().info("Talk: \"" + option.getText()
            + "\" delta=" + option.getHostilityDelta()
            + " hostility=" + battleCtx.getHostility());

        phase = Phase.PLAYER_RESULT;
        dialogueBox.setText(lastResult.getMessage());
    }

    private void executeEnemyTurn() {
        try {
            lastResult = engine.executeEnemyTurn(battleCtx);
            battleCtx.setLastResult(lastResult);

            if (enemy.getAnimator().has("attack")) {
                enemy.getAnimator().forcePlay("attack");
                phase = Phase.ENEMY_ANIM_WAIT;
            } else {
                phase = Phase.ENEMY_RESULT;
                dialogueBox.setText(lastResult.getMessage());
            }

        } catch (GameException e) {
            GameLogger.get().error("Enemy turn error", e);
            phase = Phase.PLAYER_TURN;
        }
    }

    // ---------------------------------------------------------------
    // Exit routing
    // ---------------------------------------------------------------

    private void exitBattle() {
        onExit();
        if (lastResult == null) { onWin.run(); return; }
        switch (lastResult.getType()) {
            case PLAYER_DEFEATED -> onLose.run();
            case PLAYER_FLED     -> onFlee.run();
            default              -> onWin.run();
        }
    }

    private String buildTerminalMessage(BattleResult result) {
        return switch (result.getType()) {
            case ENEMY_DEFEATED  -> result.getMessage() + "\n* The path is clear.";
            case MERCY_GRANTED   -> result.getMessage() + "\n* They step aside peacefully.";
            case PLAYER_FLED     -> result.getMessage();
            case PLAYER_DEFEATED -> result.getMessage() + "\n\n* Press Z to try again.";
            default              -> result.getMessage();
        };
    }

    // ---------------------------------------------------------------
    // Standalone callbacks (used by BattleTest)
    // ---------------------------------------------------------------

    public void setOnWin(Runnable r)  { this.onWin  = r; }
    public void setOnLose(Runnable r) { this.onLose = r; }
    public void setOnFlee(Runnable r) { this.onFlee = r; }

    /** Legacy — kept for BattleTest compatibility */
    public void setOnBattleEnd(Runnable r) { this.onWin = r; this.onFlee = r; }

    public boolean isOver() {
        return phase == Phase.TERMINAL && dialogueBox.isFinished();
    }

    public BattleResult getLastResult() { return lastResult; }

    private void resolveTalkOption(TalkOption option) {
    TalkAction talkAction = (TalkAction) engine.getPlayerActions().get(1);
    lastResult = talkAction.resolveOption(option, ctx);
    ctx.setLastResult(lastResult);
    phase = Phase.PLAYER_RESULT;
    dialogueBox.setText(lastResult.getMessage());
}
}
