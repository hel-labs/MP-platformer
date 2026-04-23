package com.platformer.overworld.states;

import static com.platformer.overworld.utils.Constants.UI.Buttons.B_HEIGHT_DEFAULT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.platformer.core.Game;
import com.platformer.overworld.ui.MenuButton;
import com.platformer.overworld.utils.LoadSave;

import com.platformer.gamestate.*;

import javax.imageio.ImageIO;

public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[5];
    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;
    private Map<Character, BufferedImage> bigTextGlyphs;
    private boolean leaderboardOverlayNeeded;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        bigTextGlyphs = loadBigTextGlyphs();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (25 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (130 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (180 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (230 * Game.SCALE), 3, Gamestate.CREDITS);
        int leaderboardRow = resolveLeaderboardRowIndex();
        buttons[3] = new MenuButton(Game.GAME_WIDTH / 2, (int) (280 * Game.SCALE), leaderboardRow, Gamestate.LEADERBOARD);
        leaderboardOverlayNeeded = leaderboardRow < 4;
        buttons[4] = new MenuButton(Game.GAME_WIDTH / 2, (int) (330 * Game.SCALE), 2, Gamestate.QUIT);
    }

    private int resolveLeaderboardRowIndex() {
        BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        if (atlas == null) {
            return 3;
        }

        int availableRows = atlas.getHeight() / B_HEIGHT_DEFAULT;
        if (availableRows >= 5) {
            return 4;
        }

        // Fall back to the CREDITS row when a dedicated LEADERBOARD row is not in the atlas.
        return 3;
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton mb : buttons) {
            mb.draw(g);
        }

        if (leaderboardOverlayNeeded) {
            drawLeaderboardLabel(g, buttons[3]);
        }
    }

    private void drawLeaderboardLabel(Graphics g, MenuButton button) {
        if (button == null || bigTextGlyphs.isEmpty()) {
            return;
        }

        String text = "LEADERBOARD";
        float spacing = Math.max(1f, 0.8f * Game.SCALE);
        Rectangle bounds = button.getBounds();

        float baseTextWidth = getBigTextWidth(text, 1f, spacing);
        float baseTextHeight = getBigTextHeight(1f);

        float maxTextWidth = bounds.width - (20f * Game.SCALE);
        float maxTextHeight = bounds.height - (14f * Game.SCALE);

        float widthScale = maxTextWidth / Math.max(baseTextWidth, 1f);
        float heightScale = maxTextHeight / Math.max(baseTextHeight, 1f);
        float scale = Math.max(1f, Math.min(Math.min(widthScale, heightScale), 3.5f));

        int textWidth = Math.round(getBigTextWidth(text, scale, spacing));
        int textHeight = Math.round(getBigTextHeight(scale));

        int startX = bounds.x + (bounds.width - textWidth) / 2;
        int startY = bounds.y + (bounds.height - textHeight) / 2;

        int stripPadX = Math.round(10f * Game.SCALE);
        int stripPadY = Math.round(7f * Game.SCALE);
        int stripX = bounds.x + stripPadX;
        int stripY = bounds.y + stripPadY;
        int stripW = bounds.width - (stripPadX * 2);
        int stripH = bounds.height - (stripPadY * 2);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // Hide the reused underlying label before drawing LEADERBOARD text.
            g2.setColor(new Color(229, 198, 126, 245));
            g2.fillRoundRect(stripX, stripY, stripW, stripH, 6, 6);

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            drawBigText(g2, text, startX, startY, scale, spacing);
        } finally {
            g2.dispose();
        }
    }

    private void drawBigText(Graphics2D g, String text, int x, int y, float scale, float spacing) {
        float currentX = x;
        String normalized = text.toUpperCase();

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (c == ' ') {
                currentX += 6 * scale;
                continue;
            }

            BufferedImage glyph = bigTextGlyphs.get(c);
            if (glyph == null) {
                currentX += 6 * scale;
                continue;
            }

            int drawW = Math.round(glyph.getWidth() * scale);
            int drawH = Math.round(glyph.getHeight() * scale);
            g.drawImage(glyph, Math.round(currentX), y, drawW, drawH, null);
            currentX += drawW + spacing;
        }
    }

    private float getBigTextWidth(String text, float scale, float spacing) {
        float width = 0;
        String normalized = text.toUpperCase();

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (c == ' ') {
                width += 6 * scale;
                continue;
            }

            BufferedImage glyph = bigTextGlyphs.get(c);
            if (glyph == null) {
                width += 6 * scale;
            } else {
                width += glyph.getWidth() * scale + spacing;
            }
        }

        return Math.max(0f, width - spacing);
    }

    private float getBigTextHeight(float scale) {
        int maxHeight = 0;
        for (BufferedImage glyph : bigTextGlyphs.values()) {
            maxHeight = Math.max(maxHeight, glyph.getHeight());
        }
        return maxHeight * scale;
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
        try (InputStream is = Menu.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed()) {
                    mb.applyGamestate();
                }
                if (mb.getState() == Gamestate.PLAYING) {
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                }
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton mb : buttons) {
            mb.resetBools();
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }

        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
