package com.echoes.ui;

import com.echoes.battle.BattleContext;
import com.echoes.battle.BattleResult;
import com.echoes.battle.actions.BattleAction;
import com.echoes.dialogue.DialogueBox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Renders the complete battle screen.
 *
 * Layout (800 × 480 window):
 *
 *  ┌─────────────────────────────────────────────────┐
 *  │  [ENEMY DISPLAY SPRITE]     [PLAYER SPRITE]     │  ← combat area (y 0–290)
 *  │  [ENEMY NAME + HP BAR]      [PLAYER HP BAR]     │
 *  ├─────────────────────────────────────────────────┤
 *  │  [DIALOGUE BOX / ACTION MENU]                   │  ← UI panel (y 290–480)
 *  │  [MERCY HINT]  [TURN COUNTER]                   │
 *  └─────────────────────────────────────────────────┘
 *
 * When playerTurn is true → action menu is shown.
 * When playerTurn is false → dialogue box is shown.
 */
public class BattleUI {

    // Layout constants
    public static final int SCREEN_W  = 800;
    public static final int SCREEN_H  = 480;
    public static final int PANEL_Y   = 290;
    public static final int PANEL_H   = SCREEN_H - PANEL_Y;

    private static final int HP_BAR_W = 160;
    private static final int HP_BAR_H = 10;

    // Fonts
    private static final Font FONT_LABEL  = new Font("Monospaced", Font.BOLD,  12);
    private static final Font FONT_BODY   = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font FONT_ACTION = new Font("Monospaced", Font.BOLD,  14);
    private static final Font FONT_HINT   = new Font("Monospaced", Font.ITALIC,11);
    private static final Font FONT_SMALL  = new Font("Monospaced", Font.PLAIN, 10);

    // Colors
    private static final Color COL_BG_TOP    = new Color(10,  10,  30);
    private static final Color COL_BG_BOTTOM = new Color(5,   5,   15);
    private static final Color COL_PANEL     = new Color(12,  12,  28);
    private static final Color COL_BORDER    = new Color(80,  80, 140);
    private static final Color COL_HP_BG     = new Color(60,  20,  20);
    private static final Color COL_HP_HIGH   = new Color(80, 200,  80);
    private static final Color COL_HP_MED    = new Color(220,180,   0);
    private static final Color COL_HP_LOW    = new Color(200,  50,  50);
    private static final Color COL_SELECTED  = new Color(255, 220,  80);
    private static final Color COL_UNSELECT  = new Color(180, 180, 200);
    private static final Color COL_MERCY_RDY = new Color(180, 255, 180);
    private static final Color COL_MERCY_NOT = new Color(130, 130, 150);
    private static final Color COL_TURN_CTR  = new Color(100, 100, 140);

    // ---------------------------------------------------------------
    // Main render entry point
    // ---------------------------------------------------------------

    /**
     * Render the full battle screen.
     *
     * @param g              Graphics2D context
     * @param ctx            current battle context
     * @param selectedAction index of the highlighted action in the menu
     * @param actions        ordered list of player actions
     * @param playerTurn     true = show action menu, false = show dialogue
     * @param dialogueBox    the shared dialogue box (updated externally)
     */
    public void render(Graphics2D g,
                       BattleContext ctx,
                       int selectedAction,
                       List<BattleAction> actions,
                       boolean playerTurn,
                       DialogueBox dialogueBox) {

        renderBackground(g);
        renderCombatants(g, ctx);
        renderHPBars(g, ctx);
        renderPanel(g);

        if (playerTurn) {
            renderActionMenu(g, ctx, selectedAction, actions);
        } else {
            dialogueBox.render(g, 40, PANEL_Y + 10, SCREEN_W - 80, 90);
        }

        renderTurnCounter(g, ctx);
    }

    // ---------------------------------------------------------------
    // Background
    // ---------------------------------------------------------------

    private void renderBackground(Graphics2D g) {
        GradientPaint grad = new GradientPaint(
            0, 0,      COL_BG_TOP,
            0, PANEL_Y, COL_BG_BOTTOM);
        g.setPaint(grad);
        g.fillRect(0, 0, SCREEN_W, PANEL_Y);
    }

    private void renderPanel(Graphics2D g) {
        g.setColor(COL_PANEL);
        g.fillRect(0, PANEL_Y, SCREEN_W, PANEL_H);
        g.setColor(COL_BORDER);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(0, PANEL_Y, SCREEN_W, PANEL_Y);
        g.setStroke(new BasicStroke(1f));
    }

    // ---------------------------------------------------------------
    // Combatant sprites
    // ---------------------------------------------------------------

    private void renderCombatants(Graphics2D g, BattleContext ctx) {
        renderEnemySprite(g, ctx);
        renderPlayerSprite(g, ctx);
    }

    private void renderEnemySprite(Graphics2D g, BattleContext ctx) {
        BufferedImage sprite = ctx.getEnemy().getBattleSprite();
        int ex = 160;
        int ey = 80;
        int ew = 128;
        int eh = 128;

        if (sprite != null) {
            g.drawImage(sprite, ex - ew / 2, ey - eh / 2, ew, eh, null);
        } else {
            // PLACEHOLDER — colored oval until real sprite is set
            g.setColor(new Color(100, 200, 100, 180));
            g.fillOval(ex - ew / 2, ey - eh / 2, ew, eh);
            g.setColor(Color.WHITE);
            g.setFont(FONT_SMALL);
            FontMetrics fm = g.getFontMetrics();
            String name = ctx.getEnemy().getName();
            g.drawString(name, ex - fm.stringWidth(name) / 2, ey + 4);
        }
    }

    private void renderPlayerSprite(Graphics2D g, BattleContext ctx) {
        BufferedImage frame = ctx.getPlayer().getAnimator().getCurrentFrame();
        int px = 580;
        int py = 160;
        int pw = 64;
        int ph = 96;

        if (frame != null) {
            g.drawImage(frame, px, py, pw, ph, null);
        } else {
            // PLACEHOLDER
            g.setColor(new Color(70, 130, 180, 180));
            g.fillRect(px, py, pw, ph);
            g.setColor(Color.WHITE);
            g.setFont(FONT_SMALL);
            g.drawString("YOU", px + 14, py + ph / 2);
        }
    }

    // ---------------------------------------------------------------
    // HP bars
    // ---------------------------------------------------------------

    private void renderHPBars(Graphics2D g, BattleContext ctx) {
        // Enemy HP — bottom left of combat area
        renderHPBar(g,
            ctx.getEnemy().getName(),
            ctx.getEnemy().getHp(),
            ctx.getEnemy().getMaxHp(),
            40, PANEL_Y - 36);

        // Player HP — bottom right of combat area
        renderHPBar(g,
            "HP",
            ctx.getPlayer().getHp(),
            ctx.getPlayer().getMaxHp(),
            500, PANEL_Y - 36);
    }

    private void renderHPBar(Graphics2D g, String label,
                              int hp, int maxHp,
                              int x, int y) {
        // Label
        g.setColor(Color.WHITE);
        g.setFont(FONT_LABEL);
        g.drawString(label, x, y);

        int barY = y + 4;

        // Background
        g.setColor(COL_HP_BG);
        g.fillRoundRect(x, barY, HP_BAR_W, HP_BAR_H, 4, 4);

        // Fill
        float ratio    = maxHp > 0 ? (float) hp / maxHp : 0f;
        int   fillW    = (int)(HP_BAR_W * ratio);
        Color fillCol  = ratio > 0.5f ? COL_HP_HIGH
                       : ratio > 0.25f ? COL_HP_MED
                       : COL_HP_LOW;
        if (fillW > 0) {
            g.setColor(fillCol);
            g.fillRoundRect(x, barY, fillW, HP_BAR_H, 4, 4);
        }

        // Border
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(x, barY, HP_BAR_W, HP_BAR_H, 4, 4);

        // Numeric value
        g.setFont(FONT_SMALL);
        g.setColor(Color.WHITE);
        g.drawString(hp + "/" + maxHp,
                     x + HP_BAR_W + 6, barY + HP_BAR_H);
    }

    // ---------------------------------------------------------------
    // Action menu
    // ---------------------------------------------------------------

    private void renderActionMenu(Graphics2D g,
                                   BattleContext ctx,
                                   int selected,
                                   List<BattleAction> actions) {
        // 2×2 grid of actions
        int startX = 50;
        int startY = PANEL_Y + 24;
        int colW   = 180;
        int rowH   = 34;

        for (int i = 0; i < actions.size(); i++) {
            int col  = i % 2;
            int row  = i / 2;
            int bx   = startX + col * colW;
            int by   = startY + row * rowH;
            boolean sel = (i == selected);

            g.setFont(FONT_ACTION);
            g.setColor(sel ? COL_SELECTED : COL_UNSELECT);
            g.drawString((sel ? "▶ " : "  ") + actions.get(i).getLabel(),
                         bx, by + 18);
        }

        // Action description for highlighted action
        if (selected >= 0 && selected < actions.size()) {
            g.setFont(FONT_HINT);
            g.setColor(new Color(160, 160, 180));
            g.drawString(actions.get(selected).getDescription(),
                         startX, PANEL_Y + 100);
        }

        // Mercy readiness indicator
        renderMercyIndicator(g, ctx);
    }

    private void renderMercyIndicator(Graphics2D g, BattleContext ctx) {
        g.setFont(FONT_HINT);
        if (ctx.getEnemy().isMercyReady(ctx)) {
            g.setColor(COL_MERCY_RDY);
            g.drawString("✦ mercy available",
                         50, PANEL_Y + PANEL_H - 18);
        } else {
            g.setColor(COL_MERCY_NOT);
            String hint = ctx.getEnemy().getMercyHint(ctx);
            // Show only first line of hint in the indicator strip
            String firstLine = hint.split("\n")[0].replace("* ", "");
            g.drawString(firstLine, 50, PANEL_Y + PANEL_H - 18);
        }
    }

    // ---------------------------------------------------------------
    // Turn counter
    // ---------------------------------------------------------------

    private void renderTurnCounter(Graphics2D g, BattleContext ctx) {
        g.setFont(FONT_SMALL);
        g.setColor(COL_TURN_CTR);
        String turnText = "Turn " + ctx.getTurnCount();
        FontMetrics fm  = g.getFontMetrics();
        g.drawString(turnText,
                     SCREEN_W - fm.stringWidth(turnText) - 12,
                     PANEL_Y + 18);
    }

    // ---------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------

    /** Draw centered text at a given y position. */
    public static void drawCentered(Graphics2D g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (SCREEN_W - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}
