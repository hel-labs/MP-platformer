package com.platformer.gamestate;

import com.platformer.core.Game;
import com.platformer.input.InputHandler;
import com.platformer.overworld.utils.LoadSave;
import com.platformer.utils.PlayerProfileManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class PlayerNameEntryScreen extends State implements Statemethods {

    private static final int MAX_NAME_LENGTH = 16;
    private static final int SPACE_WIDTH = 6;
    private static final int GLYPH_SPACING = 1;

    private static final String TITLE_TEXT = "MY PLATFORMER";
    private static final String PROMPT_TEXT = "ENTER YOUR NAME";
    private static final String HINT_TEXT = "Type your name and press ENTER";

    private final BufferedImage backgroundPink;
    private final Map<Character, BufferedImage> bigTextGlyphs;
    private final StringBuilder nameBuffer = new StringBuilder();

    public PlayerNameEntryScreen(Game game) {
        super(game);
        backgroundPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        bigTextGlyphs = loadBigTextGlyphs();

        String savedName = PlayerProfileManager.getCurrentPlayerName();
        if (savedName != null && !savedName.isBlank()) {
            nameBuffer.append(savedName);
        }
    }

    @Override
    public void update() {
        InputHandler input = game.getInputHandler();

        if (input.isJustPressed(InputHandler.ENTER)) {
            confirmAndContinue();
        }

        if (input.isJustPressed(InputHandler.ESCAPE)) {
            Gamestate.state = Gamestate.TITLE;
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            if (backgroundPink != null) {
                g2.drawImage(backgroundPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            } else {
                g2.setColor(new Color(231, 167, 191));
                g2.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            }

            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            drawBigTextCentered(g2, TITLE_TEXT, (int) (80 * Game.SCALE), Math.max(1, Math.round(2f * Game.SCALE)));
            drawBigTextCentered(g2, PROMPT_TEXT, (int) (190 * Game.SCALE), Math.max(1, Math.round(1.3f * Game.SCALE)));

            int boxW = (int) (360 * Game.SCALE);
            int boxH = (int) (54 * Game.SCALE);
            int boxX = (Game.GAME_WIDTH - boxW) / 2;
            int boxY = (int) (220 * Game.SCALE);

            g2.setColor(new Color(18, 14, 22, 210));
            g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);
            g2.setColor(new Color(255, 228, 188));
            g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);

            String shownName = getEnteredName();
            if (shownName.isEmpty()) {
                shownName = "PLAYER";
                g2.setColor(new Color(190, 170, 170));
            } else {
                g2.setColor(Color.WHITE);
            }

            g2.setFont(new Font("Monospaced", Font.BOLD, (int) (24 * Game.SCALE)));
            FontMetrics fm = g2.getFontMetrics();
            int nameX = boxX + (boxW - fm.stringWidth(shownName)) / 2;
            int nameY = boxY + (boxH + fm.getAscent()) / 2 - (int) (5 * Game.SCALE);
            g2.drawString(shownName, nameX, nameY);

            g2.setFont(new Font("Monospaced", Font.PLAIN, (int) (14 * Game.SCALE)));
            g2.setColor(new Color(255, 245, 230));
            int hintX = (Game.GAME_WIDTH - g2.getFontMetrics().stringWidth(HINT_TEXT)) / 2;
            g2.drawString(HINT_TEXT, hintX, boxY + boxH + (int) (32 * Game.SCALE));

            String escText = "ESC to go back";
            int escX = (Game.GAME_WIDTH - g2.getFontMetrics().stringWidth(escText)) / 2;
            g2.drawString(escText, escX, boxY + boxH + (int) (56 * Game.SCALE));
        } finally {
            g2.dispose();
        }
    }

    public void handleKeyTyped(char typed) {
        if (Gamestate.state != Gamestate.NAME_ENTRY) {
            return;
        }

        if (Character.isISOControl(typed)) {
            return;
        }

        if (nameBuffer.length() >= MAX_NAME_LENGTH) {
            return;
        }

        if (Character.isLetterOrDigit(typed) || typed == ' ' || typed == '-' || typed == '_') {
            nameBuffer.append(typed);
        }
    }

    public void handleKeyPressed(int keyCode) {
        if (Gamestate.state != Gamestate.NAME_ENTRY) {
            return;
        }

        if (keyCode == KeyEvent.VK_BACK_SPACE && nameBuffer.length() > 0) {
            nameBuffer.deleteCharAt(nameBuffer.length() - 1);
        }
    }

    private void confirmAndContinue() {
        String entered = getEnteredName();
        if (entered.isEmpty()) {
            return;
        }

        PlayerProfileManager.setCurrentPlayerName(entered);
        setGamestate(Gamestate.MENU);
    }

    private String getEnteredName() {
        return nameBuffer.toString().trim();
    }

    private void drawBigTextCentered(Graphics2D g, String text, int y, int scale) {
        int width = getBigTextWidth(text, scale);
        int x = (Game.GAME_WIDTH - width) / 2;
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

            int drawW = glyph.getWidth() * scale;
            int drawH = glyph.getHeight() * scale;
            g.drawImage(glyph, currentX, y, drawW, drawH, null);
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
            if (glyph != null) {
                width += (glyph.getWidth() + GLYPH_SPACING) * scale;
            } else {
                width += SPACE_WIDTH * scale;
            }
        }
        return width;
    }

    private Map<Character, BufferedImage> loadBigTextGlyphs() {
        Map<Character, BufferedImage> glyphs = new HashMap<>();

        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            BufferedImage img = loadImage("/res/big_text/" + (i + 1) + ".png");
            if (img != null) {
                glyphs.put(letter, img);
            }
        }

        for (int i = 0; i < 10; i++) {
            int fileIndex = 27 + i;
            char digit = (i < 9) ? (char) ('1' + i) : '0';
            BufferedImage img = loadImage("/res/big_text/" + fileIndex + ".png");
            if (img != null) {
                glyphs.put(digit, img);
            }
        }

        return glyphs;
    }

    private BufferedImage loadImage(String resourcePath) {
        try (InputStream is = PlayerNameEntryScreen.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
    }
}
