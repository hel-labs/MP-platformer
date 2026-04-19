package com.platformer.battle.dialogue;

import java.awt.*;

public class DialogueBox {

    private String fullText      = "";
    private String displayedText = "";
    private float  charTimer     = 0f;
    private boolean finished     = false;
    private static final float CHAR_DELAY = 0.04f;
    private static final int   CORNER_RADIUS = 12;
    private static final int   PADDING       = 16;
    private static final Color BG_COLOR      = new Color(10, 10, 20, 220);
    private static final Color BORDER_COLOR  = new Color(200, 200, 220);
    private static final Color TEXT_COLOR    = Color.WHITE;
    private static final Font  TEXT_FONT     = new Font("Monospaced", Font.PLAIN, Math.max(14, Math.round(13 * com.platformer.core.Game.SCALE)));

    public void setText(String text) {
        this.fullText      = text == null ? "" : text;
        this.displayedText = "";
        this.charTimer     = 0f;
        this.finished      = fullText.isEmpty();
    }
    public void update(float dt) {
        if (finished) return;

        charTimer += dt;
        int charsToShow = (int)(charTimer / CHAR_DELAY);
        charsToShow = Math.min(charsToShow, fullText.length());
        displayedText = fullText.substring(0, charsToShow);

        if (charsToShow >= fullText.length()) {
            finished = true;
        }
    }
    public void skipToEnd() {
        displayedText = fullText;
        finished      = true;
    }
    public boolean isFinished() { return finished; }
    public boolean isEmpty() { return fullText.isEmpty(); }

    public void render(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(BG_COLOR);
        g.fillRoundRect(x, y, width, height, CORNER_RADIUS, CORNER_RADIUS);

        g.setColor(BORDER_COLOR);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x, y, width, height, CORNER_RADIUS, CORNER_RADIUS);
        g.setStroke(new BasicStroke(1f));

        g.setColor(TEXT_COLOR);
        g.setFont(TEXT_FONT);
        drawWrapped(g, displayedText,
                    x + PADDING,
                    y + PADDING + g.getFontMetrics().getAscent(),
                    width - PADDING * 2);

        if (finished && !fullText.isEmpty()) {
            long ms = System.currentTimeMillis();
            if ((ms / 500) % 2 == 0) {
                g.setFont(new Font("Monospaced", Font.BOLD, Math.max(12, Math.round(11 * com.platformer.core.Game.SCALE))));
                g.setColor(new Color(200, 200, 180));
                g.drawString("▼", x + width - PADDING - 8,
                             y + height - PADDING / 2);
            }
        }
    }
    private void drawWrapped(Graphics2D g, String text,
                              int x, int y, int maxWidth) {
        if (text == null || text.isEmpty()) return;

        FontMetrics fm   = g.getFontMetrics();
        int         lineH = fm.getHeight() + 2;

        String[] paragraphs = text.split("\n", -1);

        int drawY = y;
        for (String para : paragraphs) {
            String[] words = para.split(" ", -1);
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                String candidate = line.isEmpty()
                    ? word : line + " " + word;
                if (fm.stringWidth(candidate) > maxWidth && !line.isEmpty()) {
                    g.drawString(line.toString(), x, drawY);
                    drawY += lineH;
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(candidate);
                }
            }
            if (!line.isEmpty()) {
                g.drawString(line.toString(), x, drawY);
                drawY += lineH;
            }
        }
    }
}