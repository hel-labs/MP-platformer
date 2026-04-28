package com.platformer.gamestate;

import com.platformer.core.Game;
import com.platformer.input.InputHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TitleScreen extends State implements Statemethods {

    private static final String TITLE_TEXT = "MY PLATFORMER";
    private static final String ENTER_TEXT = "PRESS ENTER TO START";
    private static final int SPACE_WIDTH = 6;
    private static final int GLYPH_SPACING = 1;

    private final BufferedImage backgroundImage;
    private final Map<Character, BufferedImage> bigTextGlyphs;

    public TitleScreen(Game game) {
        super(game);
        backgroundImage = loadImageFromCandidates("/res/background_menu.png", "/background_menu.png");
        bigTextGlyphs = loadBigTextGlyphs();
    }

    @Override
    public void update() {
        InputHandler input = game.getInputHandler();
        if (input.isJustPressed(InputHandler.ENTER)) {
            Gamestate.state = Gamestate.NAME_ENTRY;
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            } else {
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            }

            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

            int titleScale = Math.max(1, Math.round(2f * Game.SCALE));
            int enterScale = Math.max(1, Math.round(1f * Game.SCALE));
            int titleY = Game.GAME_HEIGHT / 3;
            int enterY = (int) (Game.GAME_HEIGHT / 1.6f);

            if (!bigTextGlyphs.isEmpty()) {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                drawBigTextCentered(g2, TITLE_TEXT, titleY, titleScale);
                drawBigTextCentered(g2, ENTER_TEXT, enterY, enterScale);
            } else {
                g2.setColor(Color.WHITE);
                Font titleFont = new Font("Arial", Font.BOLD, (int) (40 * Game.SCALE));
                Font enterFont = new Font("Arial", Font.PLAIN, (int) (20 * Game.SCALE));

                g2.setFont(titleFont);
                drawCenteredString(g2, TITLE_TEXT, Game.GAME_HEIGHT / 3);

                g2.setFont(enterFont);
                drawCenteredString(g2, ENTER_TEXT, (int) (Game.GAME_HEIGHT / 1.6));
            }
        } finally {
            g2.dispose();
        }
    }

    private void drawBigTextCentered(Graphics2D g, String text, int y, int scale) {
        int textWidth = getBigTextWidth(text, scale);
        int x = (Game.GAME_WIDTH - textWidth) / 2;
        drawBigText(g, text, x, y, scale);
    }

    private void drawBigText(Graphics2D g, String text, int x, int y, int scale) {
        String normalized = text.toUpperCase();
        int currentX = x;
        for (int i = 0; i < normalized.length(); i++) {
            char ch = normalized.charAt(i);

            if (ch == ' ') {
                currentX += SPACE_WIDTH * scale;
                continue;
            }

            BufferedImage glyph = bigTextGlyphs.get(ch);
            if (glyph == null) {
                currentX += SPACE_WIDTH * scale;
                continue;
            }

            int drawWidth = glyph.getWidth() * scale;
            int drawHeight = glyph.getHeight() * scale;
            g.drawImage(glyph, currentX, y, drawWidth, drawHeight, null);
            currentX += (glyph.getWidth() + GLYPH_SPACING) * scale;
        }
    }

    private int getBigTextWidth(String text, int scale) {
        String normalized = text.toUpperCase();
        int width = 0;
        for (int i = 0; i < normalized.length(); i++) {
            char ch = normalized.charAt(i);
            if (ch == ' ') {
                width += SPACE_WIDTH * scale;
                continue;
            }

            BufferedImage glyph = bigTextGlyphs.get(ch);
            if (glyph != null)
                width += (glyph.getWidth() + GLYPH_SPACING) * scale;
            else
                width += SPACE_WIDTH * scale;
        }
        return width;
    }

    private Map<Character, BufferedImage> loadBigTextGlyphs() {
        Map<Character, BufferedImage> glyphs = new HashMap<>();

        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            BufferedImage img = loadImage("/res/big_text/" + (i + 1) + ".png");
            if (img != null)
                glyphs.put(letter, img);
        }

        for (int i = 0; i < 10; i++) {
            int fileIndex = 27 + i;
            char digit = (i < 9) ? (char) ('1' + i) : '0';
            BufferedImage img = loadImage("/res/big_text/" + fileIndex + ".png");
            if (img != null)
                glyphs.put(digit, img);
        }

        return glyphs;
    }

    private BufferedImage loadImageFromCandidates(String... resourcePaths) {
        for (String resourcePath : resourcePaths) {
            BufferedImage image = loadImage(resourcePath);
            if (image != null)
                return image;
        }
        return null;
    }

    private BufferedImage loadImage(String resourcePath) {
        try (InputStream is = TitleScreen.class.getResourceAsStream(resourcePath)) {
            if (is == null)
                return null;
            return ImageIO.read(is);
        } catch (IOException e) {
            return null;
        }
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (Game.GAME_WIDTH - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
    }
}