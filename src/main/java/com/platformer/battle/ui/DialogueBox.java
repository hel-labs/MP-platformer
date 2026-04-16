package com.echoes.dialogue;

import java.awt.*;

/**
 * Renders a dialogue panel with a typewriter character-reveal effect.
 *
 * Usage:
 *   box.setText("* The slime wobbles nervously.");
 *   // Each tick:
 *   box.update(dt);
 *   box.render(g, x, y, width, height);
 *   // Check when done:
 *   if (box.isFinished()) { ... }
 *   // Player presses Z while text is still going:
 *   box.skipToEnd();
 */
public class DialogueBox {

    private String fullText      = "";
    private String displayedText = "";
    private float  charTimer     = 0f;
    private boolean finished     = false;

    // Seconds between each character reveal
    // 0.03 = fast, 0.06 = normal Undertale speed, 0.1 = slow dramatic
    private static final float CHAR_DELAY = 0.04f;

    // Panel visual config
    private static final int   CORNER_RADIUS = 12;
    private static final int   PADDING       = 16;
    private static final Color BG_COLOR      = new Color(10, 10, 20, 220);
    private static final Color BORDER_COLOR  = new Color(200, 200, 220);
    private static final Color TEXT_COLOR    = Color.WHITE;
    private static final Font  TEXT_FONT     =
        new Font("Monospaced", Font.PLAIN, 13);

    // ---------------------------------------------------------------
    // Control
    // ---------------------------------------------------------------

    /**
     * Set new text. Resets the typewriter from the beginning.
     * Safe to call mid-display — previous text is discarded.
     */
    public void setText(String text) {
        this.fullText      = text == null ? "" : text;
        this.displayedText = "";
        this.charTimer     = 0f;
        this.finished      = fullText.isEmpty();
    }

    /**
     * Advance the typewriter by delta time.
     * Call once per game tick.
     */
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

    /**
     * Immediately reveal all text.
     * Call when the player presses confirm while text is still typing.
     */
    public void skipToEnd() {
        displayedText = fullText;
        finished      = true;
    }

    /** True when all characters have been revealed. */
    public boolean isFinished() { return finished; }

    /** True when no text has been set yet. */
    public boolean isEmpty() { return fullText.isEmpty(); }

    // ---------------------------------------------------------------
    // Render
    // ---------------------------------------------------------------

    /**
     * Draw the dialogue panel at the given screen coordinates.
     *
     * @param g      Graphics2D context
     * @param x      left edge of the panel
     * @param y      top edge of the panel
     * @param width  panel width
     * @param height panel height
     */
    public void render(Graphics2D g, int x, int y, int width, int height) {
        // Background panel
        g.setColor(BG_COLOR);
        g.fillRoundRect(x, y, width, height, CORNER_RADIUS, CORNER_RADIUS);

        // Border
        g.setColor(BORDER_COLOR);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(x, y, width, height, CORNER_RADIUS, CORNER_RADIUS);
        g.setStroke(new BasicStroke(1f));

        // Text — wrap at panel width with padding
        g.setColor(TEXT_COLOR);
        g.setFont(TEXT_FONT);
        drawWrapped(g, displayedText,
                    x + PADDING,
                    y + PADDING + g.getFontMetrics().getAscent(),
                    width - PADDING * 2);

        // "▼" prompt when finished — blinks using system time
        if (finished && !fullText.isEmpty()) {
            long ms = System.currentTimeMillis();
            if ((ms / 500) % 2 == 0) {
                g.setFont(new Font("Monospaced", Font.BOLD, 11));
                g.setColor(new Color(200, 200, 180));
                g.drawString("▼", x + width - PADDING - 8,
                             y + height - PADDING / 2);
            }
        }
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    /**
     * Word-wraps text inside maxWidth, drawing line by line.
     * Handles '\n' as a forced line break.
     */
    private void drawWrapped(Graphics2D g, String text,
                              int x, int y, int maxWidth) {
        if (text == null || text.isEmpty()) return;

        FontMetrics fm   = g.getFontMetrics();
        int         lineH = fm.getHeight() + 2;

        // Split on forced newlines first
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
