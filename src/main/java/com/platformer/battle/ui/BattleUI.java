package com.platformer.battle.ui;

import com.platformer.battle.engine.BattleContext;
import com.platformer.battle.talk.TalkOption;
import com.platformer.battle.actions.BattleAction;
import com.platformer.battle.animation.AnimationController;
import com.platformer.battle.dialogue.DialogueBox;
import com.platformer.core.Game;
import com.platformer.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class BattleUI {

    private static int ui(int value) {
        return Math.max(value, Math.round(value * Game.SCALE));
    }

    public static final int SCREEN_W = Game.GAME_WIDTH;
    public static final int SCREEN_H = Game.GAME_HEIGHT;
    public static final int PANEL_Y = (int) (SCREEN_H * 0.62f);
    public static final int PANEL_H = SCREEN_H - PANEL_Y;
    public static final int HP_BAR_W = ui(160);
    public static final int HP_BAR_H = ui(20);

    public static final Font FONT_LABEL = new Font("Monospaced", Font.BOLD, ui(12));
    public static final Font FONT_BODY = new Font("Monospaced", Font.PLAIN, ui(13));
    public static final Font FONT_ACTION = new Font("Monospaced", Font.BOLD, ui(14));
    public static final Font FONT_HINT = new Font("Monospaced", Font.ITALIC, ui(11));
    public static final Font FONT_SMALL = new Font("Monospaced", Font.PLAIN, ui(10));

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

    private final BufferedImage battleBg;
    private final BufferedImage bigCloud;
    private final BufferedImage smallCloud;
    private final BufferedImage statusBarImg;

    private final int statusBarWidth = (int) (192 * Game.SCALE);
    private final int statusBarHeight = (int) (58 * Game.SCALE);
    private final int statusBarX = (int) (10 * Game.SCALE);
    private final int statusBarY = (int) (10 * Game.SCALE);

    private final int healthBarWidth = (int) (150 * Game.SCALE);
    private final int healthBarHeight = (int) (4 * Game.SCALE);
    private final int healthBarXStart = (int) (34 * Game.SCALE);
    private final int healthBarYStart = (int) (14 * Game.SCALE);

    private final int staminaBarWidth = (int) (104 * Game.SCALE);
    private final int staminaBarHeight = (int) (2 * Game.SCALE);
    private final int staminaBarXStart = (int) (44 * Game.SCALE);
    private final int staminaBarYStart = (int) (34 * Game.SCALE);

    public BattleUI() {
        battleBg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void render(Graphics2D g, BattleContext ctx, int selectedAction, List<BattleAction> actions,
            boolean showActionMenu, List<TalkOption> talkOptions, int selectedTalkOpt, boolean showTalkMenu,
            DialogueBox dialogueBox) {
        renderBackground(g);
        renderCombatants(g, ctx);
        renderPlayerStatusTopLeft(g, ctx);
        renderEnemyStatusTopRight(g, ctx);
        renderHostilityBar(g, ctx);
        renderPanel(g);

        if (showActionMenu) {
            renderActionMenu(g, ctx, selectedAction, actions);
        } else if (showTalkMenu) {
            renderTalkOptions(g, talkOptions, selectedTalkOpt, ctx);
        } else {
            dialogueBox.render(g, ui(40), PANEL_Y + ui(10), SCREEN_W - ui(80), Math.max(ui(96), (int) (PANEL_H * 0.50f)));
        }

        renderTurnCounter(g, ctx);
    }

    public void renderBackground(Graphics2D g) {
        if (battleBg != null) {
            g.drawImage(battleBg, 0, 0, SCREEN_W, PANEL_Y, null);
        } else {
            GradientPaint grad = new GradientPaint(0, 0, COL_BG_TOP, 0, PANEL_Y, COL_BG_BOTTOM);
            g.setPaint(grad);
            g.fillRect(0, 0, SCREEN_W, PANEL_Y);
        }

        drawClouds(g);
    }

    private void drawClouds(Graphics2D g) {
        if (bigCloud != null) {
            int bigW = (int) (bigCloud.getWidth() * Game.SCALE);
            int bigH = (int) (bigCloud.getHeight() * Game.SCALE);
            int y = (int) (SCREEN_H * 0.35f) - bigH;
            for (int i = -1; i <= (SCREEN_W / Math.max(1, bigW)) + 1; i++) {
                g.drawImage(bigCloud, i * bigW, y, bigW, bigH, null);
            }
        }

        if (smallCloud != null) {
            int sw = (int) (smallCloud.getWidth() * Game.SCALE);
            int sh = (int) (smallCloud.getHeight() * Game.SCALE);
            int spacing = sw * 3;
            for (int i = 0; i < 8; i++) {
                int x = i * spacing - sw;
                int y = (int) (SCREEN_H * 0.18f) + (i % 3) * ui(12);
                g.drawImage(smallCloud, x, y, sw, sh, null);
            }
        }
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
        int groundY = PANEL_Y - ui(16);
        int playerCenterX = (int) (SCREEN_W * 0.25f);
        int enemyCenterX = (int) (SCREEN_W * 0.75f);
        int targetH = (int) (SCREEN_H * 0.20f);

        renderPlayerSprite(g, ctx, playerCenterX, groundY, targetH);
        renderEnemySprite(g, ctx, enemyCenterX, groundY, targetH);
    }

    private void renderEnemySprite(Graphics2D g, BattleContext ctx, int centerX, int groundY, int targetH) {
        BufferedImage sprite = ctx.getEnemy().getBattleSprite();

        if (sprite != null) {
            int eh = Math.max(ui(72), targetH);
            int ew = (int) (sprite.getWidth() * (eh / (float) sprite.getHeight()));
            // Keep source orientation so enemy faces left with current sprites.
            g.drawImage(sprite, centerX - ew / 2, groundY - eh, ew, eh, null);
        } else {
            int ew = (int) (SCREEN_H * 0.18f);
            int eh = ew;
            int ey = groundY - eh;
            g.setColor(new Color(100, 200, 100, 180));
            g.fillOval(centerX - ew / 2, ey, ew, eh);
            g.setColor(Color.WHITE);
            g.setFont(FONT_SMALL);
            FontMetrics fm = g.getFontMetrics();
            String name = ctx.getEnemy().getName();
            g.drawString(name, centerX - fm.stringWidth(name) / 2, ey + eh / 2);
        }
    }

    private void renderPlayerSprite(Graphics2D g, BattleContext ctx, int centerX, int groundY, int targetH) {
        AnimationController animator = ctx.getPlayer().getAnimator();
        BufferedImage frame = (animator != null) ? animator.getCurrentFrame() : null;

        if (frame != null) {
            int ph = Math.max(ui(72), targetH);
            int pw = (int) (frame.getWidth() * (ph / (float) frame.getHeight()));
            g.drawImage(frame, centerX - pw / 2, groundY - ph, pw, ph, null);
        } else {
            int pw = Math.max(88, (int) (SCREEN_W * 0.11f));
            int ph = (int) (pw * 1.5f);
            int px = centerX - pw / 2;
            g.setColor(new Color(70, 130, 180, 180));
            g.fillRect(px, groundY - ph, pw, ph);
            g.setColor(Color.WHITE);
            g.setFont(FONT_SMALL);
            g.drawString("YOU", px + 14, groundY - ph / 2);
        }
    }

    private void renderPlayerStatusTopLeft(Graphics2D g, BattleContext ctx) {
        if (statusBarImg != null) {
            g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        } else {
            g.setColor(new Color(18, 18, 32, 230));
            g.fillRoundRect(statusBarX, statusBarY, statusBarWidth, statusBarHeight, ui(8), ui(8));
            g.setColor(COL_BORDER);
            g.drawRoundRect(statusBarX, statusBarY, statusBarWidth, statusBarHeight, ui(8), ui(8));
        }

        int hp = ctx.getPlayer().getHp();
        int maxHp = ctx.getPlayer().getMaxHp();
        int stamina = ctx.getPlayer().getStamina();
        int maxStamina = ctx.getPlayer().getMaxStamina();

        int hpFill = maxHp > 0 ? (int) ((hp / (float) maxHp) * healthBarWidth) : 0;
        int stFill = maxStamina > 0 ? (int) ((stamina / (float) maxStamina) * staminaBarWidth) : 0;

        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, hpFill, healthBarHeight);

        g.setColor(Color.YELLOW);
        g.fillRect(staminaBarXStart + statusBarX, staminaBarYStart + statusBarY, stFill, staminaBarHeight);

        g.setColor(Color.BLACK);
        g.setFont(FONT_SMALL);
        g.drawString(hp + "/" + maxHp, healthBarXStart + statusBarX + healthBarWidth + ui(8), healthBarYStart + statusBarY + ui(6));
        g.drawString("ST " + stamina + "/" + maxStamina, staminaBarXStart + statusBarX + staminaBarWidth + ui(8), staminaBarYStart + statusBarY + ui(5));
    }

    private void renderEnemyStatusTopRight(Graphics2D g, BattleContext ctx) {
        int x = SCREEN_W - HP_BAR_W - ui(48);
        int y = ui(20);
        renderHPBar(g, ctx.getEnemy().getName(), ctx.getEnemy().getHp(), ctx.getEnemy().getMaxHp(), x, y);
    }

    private void renderHPBar(Graphics2D g, String label,
            int hp, int maxHp, int x, int y) {
        g.setColor(Color.BLACK);
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
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(x, barY, HP_BAR_W, HP_BAR_H, 4, 4);
        g.setFont(FONT_SMALL);
        g.drawString(hp + "/" + maxHp, x + HP_BAR_W + 6, barY + HP_BAR_H);
    }

    private void renderHostilityBar(Graphics2D g, BattleContext ctx) {
        int x = statusBarX + 920;
        int y = statusBarY + statusBarHeight - 10;
        int barW = statusBarWidth - ui(22);
        int barH = ui(10);

        int hostility = ctx.getHostility();
        float ratio = Math.max(0f, Math.min(1f, (float) hostility / BattleContext.MAX_HOSTILITY));
        int fillW = (int) (barW * ratio);

        g.setColor(new Color(16, 16, 28, 220));
        g.fillRoundRect(x, y, statusBarWidth, ui(26), ui(6), ui(6));
        g.setColor(COL_BORDER);
        g.drawRoundRect(x, y, statusBarWidth, ui(26), ui(6), ui(6));

        g.setFont(FONT_HINT);
        g.setColor(new Color(176, 130, 130));
        g.drawString("HOSTILITY", x + ui(8), y + ui(10));

        String meterText = hostility + "/" + BattleContext.MAX_HOSTILITY;
        FontMetrics fm = g.getFontMetrics();
        g.drawString(meterText, x + statusBarWidth - fm.stringWidth(meterText) - ui(8), y + ui(10));

        int barX = x + ui(8);
        int barY = y + ui(13);

        g.setColor(COL_HOST_BG);
        g.fillRoundRect(barX, barY, barW, barH, ui(3), ui(3));

        Color fillColor = ctx.isMercyAvailable() ? COL_HOST_ZERO : COL_HOST_FILL;
        if (fillW > 0) {
            g.setColor(fillColor);
            g.fillRoundRect(barX, barY, fillW, barH, ui(3), ui(3));
        }

        g.setColor(new Color(100, 60, 60));
        g.drawRoundRect(barX, barY, barW, barH, ui(3), ui(3));

        if (ctx.isMercyAvailable()) {
            g.setFont(FONT_HINT);
            g.setColor(COL_MERCY_RDY);
            g.drawString("MERCY AVAILABLE", x + ui(8), y + ui(24));
        }
    }

    private void renderActionMenu(Graphics2D g,
            BattleContext ctx,
            int selected,
            List<BattleAction> actions) {
        int colW = (int) (SCREEN_W * 0.24f);
        int totalW = colW * 2;
        int startX = (SCREEN_W - totalW) / 2;
        int startY = PANEL_Y + ui(20);
        int rowH = ui(34);

        for (int i = 0; i < actions.size(); i++) {
            int col = i % 2, row = i / 2;
            int bx = startX + col * colW;
            int by = startY + row * rowH;
            boolean sel = (i == selected);

            if (sel) {
                g.setColor(new Color(60, 50, 10, 160));
                g.fillRoundRect(bx - 4, by, colW - 16, 26, 6, 6);
                g.setColor(new Color(255, 220, 80, 80));
                g.setStroke(new BasicStroke(0.5f));
                g.drawRoundRect(bx - 4, by, colW - 16, 26, 6, 6);
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
                    startX, PANEL_Y + ui(108));
        }

        g.setFont(FONT_HINT);
        if (ctx.isMercyAvailable()) {
            g.setColor(COL_MERCY_RDY);
            g.drawString("✦ spare is ready", startX, PANEL_Y + PANEL_H - ui(18));
        } else {
            g.setColor(COL_MERCY_NOT);
            String hint = ctx.getEnemy().getMercyHint(ctx);
            String firstLine = hint.split("\n")[0].replace("* ", "");
            g.drawString(firstLine, startX, PANEL_Y + PANEL_H - ui(18));
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
                panX + panW - ui(180), panY + panH - ui(8));
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
