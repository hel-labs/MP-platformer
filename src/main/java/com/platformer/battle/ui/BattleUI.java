package com.platformer.battle.ui;

import com.platformer.battle.engine.BattleContext;
//import com.platformer.battle.engine.BattleResult;
import com.platformer.battle.talk.TalkOption;
import com.platformer.battle.actions.BattleAction;
import com.platformer.battle.animation.AnimationController;
import com.platformer.battle.dialogue.DialogueBox;
//import com.platformer.battle.entities.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class BattleUI {
    public static final int SCREEN_W = 800;
    public static final int SCREEN_H = 480;
    public static final int PANEL_Y = 290;
    public static final int PANEL_H = SCREEN_H - PANEL_Y;
    public static final int HP_BAR_W = 160;
    public static final int HP_BAR_H = 10;

    public static final Font FONT_LABEL = new Font("Monospaced", Font.BOLD, 12);
    public static final Font FONT_BODY = new Font("Monospaced", Font.PLAIN, 13);
    public static final Font FONT_ACTION = new Font("Monospaced", Font.BOLD, 14);
    public static final Font FONT_HINT = new Font("Monospaced", Font.ITALIC, 11);
    public static final Font FONT_SMALL = new Font("Monospaced", Font.PLAIN, 10);

    public static final Color COL_BG_TOP = new Color(10, 10, 30);
    public static final Color COL_BG_BOTTOM = new Color(5, 5, 15);
    public static final Color COL_PANEL = new Color(12, 12, 28);
    public static final Color COL_BORDER = new Color(80, 80, 140);
    public static final Color COL_HP_BG = new Color(60, 20, 20);
    public static final Color COL_HP_HIGH = new Color(80, 200, 80);
    public static final Color COL_HP_MED = new Color(220, 180, 0);
    public static final Color COL_HP_LOW = new Color(200, 50, 50);
    public static final Color COL_SELECTED = new Color(255, 220, 80);
    public static final Color COL_UNSELECT = new Color(180, 180, 200);
    public static final Color COL_MERCY_RDY = new Color(180, 255, 180);
    public static final Color COL_MERCY_NOT = new Color(130, 130, 150);
    public static final Color COL_TURN_CTR = new Color(100, 100, 140);

    public static final Color COL_TALK_CALM = new Color(100, 220, 130);
    public static final Color COL_TALK_PROV = new Color(220, 100, 100);
    public static final Color COL_TALK_NEUT = new Color(180, 180, 200);

    public static final Color COL_HOST_BG = new Color(40, 20, 20);
    public static final Color COL_HOST_FILL = new Color(200, 60, 60);
    public static final Color COL_HOST_ZERO = new Color(80, 200, 80);

    public void render(Graphics2D g, BattleContext ctx, int selectedAction, List<BattleAction> actions,
            boolean showActionMenu, List<TalkOption> talkOptions, int selectedTalkOpt, boolean showTalkMenu,
            DialogueBox dialogueBox) {
        renderBackground(g);
        renderCombatants(g, ctx);
        renderHPBars(g, ctx);
        renderHostilityBar(g, ctx);
        renderPanel(g);

        if (showActionMenu) {
            renderActionMenu(g, ctx, selectedAction, actions);
        } else if (showTalkMenu) {
            renderTalkOptions(g, talkOptions, selectedTalkOpt, ctx);
        } else {
            dialogueBox.render(g, 40, PANEL_Y + 10, SCREEN_W - 80, 90);
        }

        renderTurnCounter(g, ctx);
    }

    public void renderBackground(Graphics2D g) {
        GradientPaint grad = new GradientPaint(
                0, 0, COL_BG_TOP,
                0, PANEL_Y, COL_BG_BOTTOM);
        g.setPaint(grad);
        g.fillRect(0, 0, SCREEN_W, PANEL_Y);
    }

    public void renderPanel(Graphics2D g) {
        g.setColor(COL_PANEL);
        g.fillRect(0, PANEL_Y, SCREEN_W, PANEL_H);
        g.setColor(COL_BORDER);
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(0, PANEL_Y, SCREEN_W, PANEL_Y);
        g.setStroke(new BasicStroke(1f));
    }

    private void renderCombatants(Graphics2D g, BattleContext ctx) {
        renderEnemySprite(g, ctx);
        renderPlayerSprite(g, ctx);
    }

    private void renderEnemySprite(Graphics2D g, BattleContext ctx) {
        BufferedImage sprite = ctx.getEnemy().getBattleSprite();
        int ex = 160, ey = 80, ew = 128, eh = 128;

        if (sprite != null) {
            g.drawImage(sprite, ex - ew / 2, ey - eh / 2, ew, eh, null);
        } else {
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
        AnimationController animator = ctx.getPlayer().getAnimator();
        BufferedImage frame = (animator != null) ? animator.getCurrentFrame() : null;

        int px = 580, py = 160, pw = 64, ph = 96;

        if (frame != null) {
            g.drawImage(frame, px, py, pw, ph, null);
        } else {
            g.setColor(new Color(70, 130, 180, 180));
            g.fillRect(px, py, pw, ph);
            g.setColor(Color.WHITE);
            g.setFont(FONT_SMALL);
            g.drawString("YOU", px + 14, py + ph / 2);
        }
    }

    private void renderHPBars(Graphics2D g, BattleContext ctx) {
        renderHPBar(g,
                ctx.getEnemy().getName(),
                ctx.getEnemy().getHp(), ctx.getEnemy().getMaxHp(),
                40, PANEL_Y - 36);

        renderHPBar(g,
                "HP",
                ctx.getPlayer().getHp(), ctx.getPlayer().getMaxHp(),
                500, PANEL_Y - 36);
    }

    private void renderHPBar(Graphics2D g, String label,
            int hp, int maxHp, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(FONT_LABEL);
        g.drawString(label, x, y);

        int barY = y + 4;
        float ratio = maxHp > 0 ? (float) hp / maxHp : 0f;
        int fillW = (int) (HP_BAR_W * ratio);
        Color fill = ratio > 0.5f ? COL_HP_HIGH
                : ratio > 0.25f ? COL_HP_MED
                        : COL_HP_LOW;

        g.setColor(COL_HP_BG);
        g.fillRoundRect(x, barY, HP_BAR_W, HP_BAR_H, 4, 4);
        if (fillW > 0) {
            g.setColor(fill);
            g.fillRoundRect(x, barY, fillW, HP_BAR_H, 4, 4);
        }
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(x, barY, HP_BAR_W, HP_BAR_H, 4, 4);
        g.setFont(FONT_SMALL);
        g.drawString(hp + "/" + maxHp, x + HP_BAR_W + 6, barY + HP_BAR_H);
    }

    private void renderHostilityBar(Graphics2D g, BattleContext ctx) {
        int x = 40, y = PANEL_Y - 18;
        int barW = 100, barH = 6;

        int hostility = ctx.getHostility();
        float ratio = (float) hostility / BattleContext.MAX_HOSTILITY;
        int fillW = (int) (barW * ratio);

        g.setFont(FONT_SMALL);
        g.setColor(new Color(160, 100, 100));
        g.drawString("HOSTILITY", x, y);

        g.setColor(COL_HOST_BG);
        g.fillRoundRect(x + 68, y - 8, barW, barH, 3, 3);

        Color fillColor = ctx.isMercyAvailable() ? COL_HOST_ZERO : COL_HOST_FILL;
        if (fillW > 0) {
            g.setColor(fillColor);
            g.fillRoundRect(x + 68, y - 8, fillW, barH, 3, 3);
        }

        g.setColor(new Color(100, 60, 60));
        g.drawRoundRect(x + 68, y - 8, barW, barH, 3, 3);

        if (ctx.isMercyAvailable()) {
            g.setFont(FONT_HINT);
            g.setColor(COL_MERCY_RDY);
            g.drawString("✦ mercy available", x + 176, y);
        }
    }

    private void renderActionMenu(Graphics2D g,
            BattleContext ctx,
            int selected,
            List<BattleAction> actions) {
        int startX = 50, startY = PANEL_Y + 24, colW = 180, rowH = 34;

        for (int i = 0; i < actions.size(); i++) {
            int col = i % 2, row = i / 2;
            int bx = startX + col * colW;
            int by = startY + row * rowH;
            boolean sel = (i == selected);

            if (sel) {
                g.setColor(new Color(60, 50, 10, 160));
                g.fillRoundRect(bx - 4, by, 164, 26, 6, 6);
                g.setColor(new Color(255, 220, 80, 80));
                g.setStroke(new BasicStroke(0.5f));
                g.drawRoundRect(bx - 4, by, 164, 26, 6, 6);
            }

            g.setFont(FONT_ACTION);
            g.setColor(sel ? COL_SELECTED : COL_UNSELECT);
            g.drawString((sel ? "▶ " : "  ") + actions.get(i).getLabel(),
                    bx, by + 18);
        }

        if (selected >= 0 && selected < actions.size()) {
            g.setFont(FONT_HINT);
            g.setColor(new Color(160, 160, 180));
            g.drawString(actions.get(selected).getDescription(),
                    startX, PANEL_Y + 100);
        }

        g.setFont(FONT_HINT);
        if (ctx.isMercyAvailable()) {
            g.setColor(COL_MERCY_RDY);
            g.drawString("✦ spare is ready", startX, PANEL_Y + PANEL_H - 18);
        } else {
            g.setColor(COL_MERCY_NOT);
            String hint = ctx.getEnemy().getMercyHint(ctx);
            String firstLine = hint.split("\n")[0].replace("* ", "");
            g.drawString(firstLine, startX, PANEL_Y + PANEL_H - 18);
        }
    }

    private void renderTalkOptions(Graphics2D g,
            List<TalkOption> options,
            int selected,
            BattleContext ctx) {
        int panX = 40, panY = PANEL_Y + 8;
        int panW = SCREEN_W - 80, panH = PANEL_H - 16;

        g.setColor(new Color(10, 10, 28, 235));
        g.fillRoundRect(panX, panY, panW, panH, 12, 12);
        g.setColor(COL_BORDER);
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(panX, panY, panW, panH, 12, 12);
        g.setStroke(new BasicStroke(1f));

        g.setFont(FONT_HINT);
        g.setColor(new Color(160, 160, 200));
        g.drawString("What do you say?", panX + 16, panY + 20);

        g.setColor(new Color(160, 100, 100));
        String hostStr = "Hostility: " + ctx.getHostility()
                + "/" + BattleContext.MAX_HOSTILITY;
        FontMetrics fm = g.getFontMetrics();
        g.drawString(hostStr, panX + panW - fm.stringWidth(hostStr) - 16,
                panY + 20);

        g.setColor(new Color(60, 60, 90));
        g.drawLine(panX + 12, panY + 28, panX + panW - 12, panY + 28);

        int optStartY = panY + 48;
        int optH = (panH - 56) / Math.max(options.size(), 1);

        for (int i = 0; i < options.size(); i++) {
            TalkOption opt = options.get(i);
            boolean sel = (i == selected);
            int oy = optStartY + i * optH;

            Color optColor = opt.isCalming() ? COL_TALK_CALM
                    : opt.isProvoking() ? COL_TALK_PROV
                            : COL_TALK_NEUT;

            if (sel) {
                g.setColor(new Color(
                        optColor.getRed(),
                        optColor.getGreen(),
                        optColor.getBlue(), 25));
                g.fillRoundRect(panX + 8, oy - 14,
                        panW - 16, optH - 4, 6, 6);
                g.setColor(new Color(
                        optColor.getRed(),
                        optColor.getGreen(),
                        optColor.getBlue(), 70));
                g.setStroke(new BasicStroke(0.5f));
                g.drawRoundRect(panX + 8, oy - 14,
                        panW - 16, optH - 4, 6, 6);
                g.setStroke(new BasicStroke(1f));
            }

            g.setFont(FONT_ACTION);
            g.setColor(sel ? optColor : dimColor(optColor));
            g.drawString((sel ? "▶ " : "  ") + opt.getText(),
                    panX + 20, oy);

            String effectLabel = opt.isCalming()
                    ? "▼ calm  (" + opt.getHostilityDelta() + ")"
                    : opt.isProvoking()
                            ? "▲ provoke (+" + opt.getHostilityDelta() + ")"
                            : "— neutral";

            g.setFont(FONT_HINT);
            g.setColor(sel ? optColor : dimColor(optColor));
            FontMetrics efm = g.getFontMetrics();
            g.drawString(effectLabel,
                    panX + panW - efm.stringWidth(effectLabel) - 20,
                    oy);
        }

        g.setFont(FONT_SMALL);
        g.setColor(new Color(80, 80, 110));
        g.drawString("[Z] confirm   [X] cancel",
                panX + panW - 130, panY + panH - 8);
    }

    private void renderTurnCounter(Graphics2D g, BattleContext ctx) {
        g.setFont(FONT_SMALL);
        g.setColor(COL_TURN_CTR);
        String text = "Turn " + ctx.getTurnCount();
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, SCREEN_W - fm.stringWidth(text) - 12, PANEL_Y + 18);
    }

    public static void drawCentered(Graphics2D g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(text, (SCREEN_W - fm.stringWidth(text)) / 2, y);
    }

    private Color dimColor(Color c) {
        return new Color(
                (int) (c.getRed() * 0.55f),
                (int) (c.getGreen() * 0.55f),
                (int) (c.getBlue() * 0.55f));
    }
}