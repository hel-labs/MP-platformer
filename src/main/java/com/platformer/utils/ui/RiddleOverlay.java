package com.platformer.utils.ui;

import com.platformer.battle.dialogue.DialogueBox;
import com.platformer.core.Game;
import com.platformer.gamestate.Playing;
import com.platformer.overworld.levels.RiddleData;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RiddleOverlay {

    private Playing playing;
    private DialogueBox dialogueBox;
    private RiddleData riddleData;

    private boolean showingDialogue = true;
    private boolean showingRiddle = false;
    private boolean answered = false;

    private Rectangle trueBtn, falseBtn;

    private boolean trueHovered, falseHovered;
    private boolean truePressed, falsePressed;

    private static final int BOX_X = (int) (100 * Game.SCALE);
    private static final int BOX_Y = (int) (280 * Game.SCALE);
    private static final int BOX_W = (int) (600 * Game.SCALE);
    private static final int BOX_H = (int) (80 * Game.SCALE);

    private static final int BTN_W = (int) (80 * Game.SCALE);
    private static final int BTN_H = (int) (30 * Game.SCALE);
    private static final int BTN_Y = (int) (340 * Game.SCALE);

    public RiddleOverlay(Playing playing) {
        this.playing = playing;
        dialogueBox = new DialogueBox();

        int btnTrueX = Game.GAME_WIDTH / 2 - BTN_W - (int) (10 * Game.SCALE);
        int btnFalseX = Game.GAME_WIDTH / 2 + (int) (10 * Game.SCALE);
        trueBtn = new Rectangle(btnTrueX, BTN_Y, BTN_W, BTN_H);
        falseBtn = new Rectangle(btnFalseX, BTN_Y, BTN_W, BTN_H);
    }

    public void load(RiddleData data) {
        this.riddleData = data;
        this.showingDialogue = true;
        this.showingRiddle = false;
        this.answered = false;
        dialogueBox.setText(data.npcDialogue);
    }

    public void update(float dt) {
        if (showingDialogue) {
            dialogueBox.update(dt);
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        if (showingDialogue) {
            dialogueBox.render(g2, BOX_X, BOX_Y, BOX_W, BOX_H);
        } else if (showingRiddle) {
            drawRiddlePanel(g2);
        }
    }

    private void drawRiddlePanel(Graphics2D g) {
        g.setColor(new Color(10, 10, 20, 220));
        g.fillRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, 12, 12);
        g.setColor(new Color(200, 200, 220));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(BOX_X, BOX_Y, BOX_W, BOX_H, 12, 12);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, Math.max(14, Math.round(13 * Game.SCALE))));
        g.drawString(riddleData.riddleStatement, BOX_X + 16, BOX_Y + 30);

        drawButton(g, trueBtn, "TRUE", trueHovered, truePressed);
        drawButton(g, falseBtn, "FALSE", falseHovered, falsePressed);
    }

    private void drawButton(Graphics2D g, Rectangle btn, String label, boolean hovered, boolean pressed) {
        Color base = pressed ? new Color(80, 80, 160) : hovered ? new Color(60, 60, 120) : new Color(30, 30, 80);
        g.setColor(base);
        g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 8, 8);
        g.setColor(new Color(200, 200, 220));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 8, 8);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, Math.max(12, Math.round(11 * Game.SCALE))));
        FontMetrics fm = g.getFontMetrics();
        int lx = btn.x + (btn.width - fm.stringWidth(label)) / 2;
        int ly = btn.y + (btn.height + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(label, lx, ly);
    }

    public void onConfirmPressed() {
        if (showingDialogue) {
            if (!dialogueBox.isFinished()) {
                dialogueBox.skipToEnd();
            } else {
                showingDialogue = false;
                showingRiddle = true;
                dialogueBox.setText(riddleData.riddleStatement);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (!showingRiddle || answered)
            return;
        if (trueBtn.contains(e.getPoint()))
            truePressed = true;
        if (falseBtn.contains(e.getPoint()))
            falsePressed = true;
    }

    public void mouseReleased(MouseEvent e) {
        if (!showingRiddle || answered)
            return;
        if (trueBtn.contains(e.getPoint()) && truePressed) {
            answered = true;
            playing.handleNPCAnswer(true);
        } else if (falseBtn.contains(e.getPoint()) && falsePressed) {
            answered = true;
            playing.handleNPCAnswer(false);
        }
        truePressed = false;
        falsePressed = false;
    }

    public void mouseMoved(MouseEvent e) {
        trueHovered = trueBtn.contains(e.getPoint());
        falseHovered = falseBtn.contains(e.getPoint());
    }

    public boolean isAnswered() {
        return answered;
    }

    public boolean isShowingRiddle() {
        return showingRiddle;
    }
}