package com.platformer.battle.core;

import com.platformer.battle.actions.*;
import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.engine.BattleEngine;
import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.talk.TalkOption;
import com.platformer.battle.ui.BattleUI;
import com.platformer.battle.dialogue.DialogueBox;
import com.platformer.exceptions.GameException;
import com.platformer.input.InputHandler;
import com.platformer.utils.GameLogger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.function.Consumer;

public class BattleState {

    private enum Phase {
        ENCOUNTER_DIALOGUE, PLAYER_TURN, TALK_SELECTION, PLAYER_RESULT, ENEMY_TURN_DELAY, ENEMY_RESULT, TERMINAL
    }

    private final BattleContext ctx;
    private final BattleEngine engine;
    private final BattleUI ui;
    private final DialogueBox dialogueBox;
    private final InputHandler input;
    private final Consumer<BattleOutcome> onDone;

    private Phase phase = Phase.ENCOUNTER_DIALOGUE;
    private int selectedAction = 0;
    private float enemyTurnTimer = 0f;
    private List<TalkOption> talkOptions = List.of();
    private int selectedTalkOpt = 0;
    private BattleResult lastResult = null;

    private static final float ENEMY_TURN_DELAY_SECS = 1.2f;

    public BattleState(BattleContext ctx,
            InputHandler input,
            Consumer<BattleOutcome> onDone) {
        this.ctx = ctx;
        this.input = input;
        this.onDone = onDone;
        this.engine = new BattleEngine();
        this.ui = new BattleUI();
        this.dialogueBox = new DialogueBox();
    }

    public void onEnter() {
        phase = Phase.ENCOUNTER_DIALOGUE;
        selectedAction = 0;
        lastResult = null;
        dialogueBox.setText(ctx.getEnemy().getEncounterDialogue());
        GameLogger.get().info("Battle started: " + ctx.getEnemy().getName()
                + "  hostility=" + ctx.getHostility());
    }

    public void onExit() {
        GameLogger.get().info("Battle ended: " + ctx.getEnemy().getName());
    }

    public void update(float dt) {
        dialogueBox.update(dt);
        if (ctx.getPlayer().getAnimator() != null) {
            ctx.getPlayer().getAnimator().update(dt);
            if ("attack".equals(ctx.getPlayer().getAnimator().getCurrentKey())
                    && ctx.getPlayer().getAnimator().currentFinished()
                    && ctx.getPlayer().getAnimator().has("idle")) {
                ctx.getPlayer().getAnimator().play("idle");
            }
        }
        ctx.getEnemy().updateAnimation(dt);
        if (phase == Phase.ENEMY_TURN_DELAY) {
            enemyTurnTimer += dt;
            if (enemyTurnTimer >= ENEMY_TURN_DELAY_SECS) {
                enemyTurnTimer = 0f;
                executeEnemyTurn();
            }
        }
    }

    public void draw(Graphics g) {
        boolean showAction = (phase == Phase.PLAYER_TURN);
        boolean showTalk = (phase == Phase.TALK_SELECTION);

        ui.render((Graphics2D) g, ctx,
                selectedAction, engine.getPlayerActions(), showAction,
                talkOptions, selectedTalkOpt, showTalk,
                dialogueBox);
    }

    public void handleInput() {
        switch (phase) {

            case ENCOUNTER_DIALOGUE -> {
                if (input.isJustPressed(InputHandler.CONFIRM)) {
                    if (!dialogueBox.isFinished())
                        dialogueBox.skipToEnd();
                    else
                        phase = Phase.PLAYER_TURN;
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
                if (talkOptions.isEmpty()) {
                    phase = Phase.PLAYER_TURN;
                    return;
                }
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
                    if (!dialogueBox.isFinished())
                        dialogueBox.skipToEnd();
                    else
                        exitBattle();
                }
            }

            case ENEMY_TURN_DELAY -> {
            }
        }
    }

    private void executePlayerAction(int index) {
        try {
            lastResult = engine.executePlayerAction(index, ctx);
            ctx.setLastResult(lastResult);

            if (lastResult.isTerminal()) {
                phase = Phase.PLAYER_RESULT;
                dialogueBox.setText(lastResult.getMessage());
                return;
            }

            switch (lastResult.getType()) {
                case TALK_INITIATED -> {
                    talkOptions = ctx.getEnemy().getTalkOptions(ctx.getTalkCount());
                    selectedTalkOpt = 0;
                    phase = Phase.TALK_SELECTION;
                }
                case PLAYER_ATTACKED -> {
                    phase = Phase.PLAYER_RESULT;
                    dialogueBox.setText(lastResult.getMessage());
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
        lastResult = talkAction.resolveOption(option, ctx);
        ctx.setLastResult(lastResult);
        GameLogger.get().info("Talk: \"" + option.getText()
                + "\" delta=" + option.getHostilityDelta()
                + " hostility=" + ctx.getHostility());
        phase = Phase.PLAYER_RESULT;
        dialogueBox.setText(lastResult.getMessage());
    }

    private void executeEnemyTurn() {
        try {
            lastResult = engine.executeEnemyTurn(ctx);
            ctx.setLastResult(lastResult);
            phase = Phase.ENEMY_RESULT;
            dialogueBox.setText(lastResult.getMessage());
        } catch (GameException e) {
            GameLogger.get().error("Enemy turn error", e);
            phase = Phase.PLAYER_TURN;
        }
    }

    private void exitBattle() {
        onExit();
        BattleOutcome outcome;
        if (lastResult == null) {
            outcome = new BattleOutcome(BattleOutcome.Result.WIN,
                    ctx.getPlayer().getHp());
        } else {
            outcome = switch (lastResult.getType()) {
                case PLAYER_DEFEATED -> new BattleOutcome(
                        BattleOutcome.Result.LOSE, ctx.getPlayer().getHp());
                case PLAYER_FLED -> new BattleOutcome(
                        BattleOutcome.Result.FLEE, ctx.getPlayer().getHp());
                default -> new BattleOutcome(
                        BattleOutcome.Result.WIN, ctx.getPlayer().getHp());
            };
        }
        onDone.accept(outcome);
    }

    private String buildTerminalMessage(BattleResult result) {
        return switch (result.getType()) {
            case ENEMY_DEFEATED -> result.getMessage() + "\n* The path is clear.";
            case MERCY_GRANTED -> result.getMessage() + "\n* They step aside.";
            case PLAYER_FLED -> result.getMessage();
            case PLAYER_DEFEATED -> result.getMessage() + "\n\n* Press Z to continue.";
            default -> result.getMessage();
        };
    }
}