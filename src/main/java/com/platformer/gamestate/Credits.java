package com.platformer.gamestate;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.platformer.core.Game;
import com.platformer.input.InputHandler;
import com.platformer.utils.LoadSave;

public class Credits extends State implements Statemethods {

    private BufferedImage backgroundImg;
    private Map<Character, BufferedImage> bigTextGlyphs;

    private final String[] creditsLines = {
            "CONTRIBUTORS",
            "SAMI SHARIF ARKA",
            "FAHIM MUNTASIR GALIB",
            "SHAMS RUBAYET PURBO",
            "",
            "PLAYTESTERS",
            "SAMDANI SAMIN",
            "",
            "SPECIAL THANKS TO",
            "ABDULLAH AL SAYED",
            "HSM"
    };

    private float scrollY = Game.GAME_HEIGHT; 
    private final float scrollSpeed = 0.5f;
    private final float lineSpacing = 45f * Game.SCALE;
    private final float titleScale = 2.5f;
    private final float nameScale = 1.8f;
    private final float glyphSpacing = 1.5f * Game.SCALE;

    private ArrayList<ShowEntity> entitiesList;

    public Credits(Game game) {
        super(game);
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        bigTextGlyphs = loadBigTextGlyphs();
        loadEntities();
    }

    private Map<Character, BufferedImage> loadBigTextGlyphs() {
        Map<Character, BufferedImage> glyphs = new HashMap<>();

        // Load A-Z
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            BufferedImage img = loadImage("/res/big_text/" + (i + 1) + ".png");
            if (img != null) {
                glyphs.put(letter, img);
            }
        }

        // Load 0-9
        for (int i = 0; i < 10; i++) {
            int fileIndex = 27 + i;
            char digit = (i < 9) ? (char) ('1' + i) : '0';
            BufferedImage img = loadImage("/res/big_text/" + fileIndex + ".png");
            if (img != null) {
                glyphs.put(digit, img);
            }
        }

        glyphs.put(' ', null);

        return glyphs;
    }

    private BufferedImage loadImage(String resourcePath) {
        try (InputStream is = Credits.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Could not load: " + resourcePath);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            System.err.println("Error loading image: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }

    private void loadEntities() {
        entitiesList = new ArrayList<>();
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS), 5, 64, 40),
                (int) (Game.GAME_WIDTH * 0.05), (int) (Game.GAME_HEIGHT * 0.85)));
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9, 72, 32),
                (int) (Game.GAME_WIDTH * 0.12), (int) (Game.GAME_HEIGHT * 0.85)));
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS), 8, 34, 30),
                (int) (Game.GAME_WIDTH * 0.80), (int) (Game.GAME_HEIGHT * 0.85)));
        entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8, 34, 30),
                (int) (Game.GAME_WIDTH * 0.87), (int) (Game.GAME_HEIGHT * 0.85)));
    }

    private BufferedImage[] getIdleAni(BufferedImage atlas, int spritesAmount, int width, int height) {
        BufferedImage[] arr = new BufferedImage[spritesAmount];
        for (int i = 0; i < spritesAmount; i++) {
            arr[i] = atlas.getSubimage(width * i, 0, width, height);
        }
        return arr;
    }

    @Override
    public void update() {
        InputHandler input = game.getInputHandler();
        if (input.isJustPressed(InputHandler.ESCAPE)) {
            Gamestate.state = Gamestate.MENU;
        }


        scrollY -= scrollSpeed;

        float totalHeight = creditsLines.length * lineSpacing;
        if (scrollY < -totalHeight) {
            scrollY = Game.GAME_HEIGHT;
        }

        for (ShowEntity se : entitiesList) {
            se.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawCredits(g);

        for (ShowEntity se : entitiesList) {
            se.draw(g);
        }
    }

    private void drawCredits(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            float currentY = scrollY;

            for (String line : creditsLines) {
                if (currentY > Game.GAME_HEIGHT || currentY < -50) {
                    currentY += lineSpacing;
                    continue;
                }

                if (line.isEmpty()) {
                    currentY += lineSpacing;
                    continue;
                }

                boolean isTitle = line.equals("CONTRIBUTORS") ||
                        line.equals("PLAYTESTERS") ||
                        line.equals("SPECIAL THANKS TO");

                float scale = isTitle ? titleScale : nameScale;

                float textWidth = getBigTextWidth(line, scale, glyphSpacing);
                float startX = (Game.GAME_WIDTH - textWidth) / 2f;

                drawBigText(g2, line, (int) startX, (int) currentY, scale, glyphSpacing);

                currentY += lineSpacing;
            }
        } finally {
            g2.dispose();
        }
    }

    private void drawBigText(Graphics2D g, String text, int x, int y, float scale, float spacing) {
        if (text == null || text.isEmpty() || bigTextGlyphs.isEmpty()) {
            return;
        }

        float currentX = x;
        String normalized = text.toUpperCase();

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);

            if (c == ' ') {
                currentX += 8 * scale;
                continue;
            }

            BufferedImage glyph = bigTextGlyphs.get(c);
            if (glyph == null) {
                currentX += 4 * scale;
                continue;
            }

            int drawW = Math.round(glyph.getWidth() * scale);
            int drawH = Math.round(glyph.getHeight() * scale);

            g.drawImage(glyph, Math.round(currentX), y, drawW, drawH, null);

            currentX += drawW + spacing;
        }
    }

    private float getBigTextWidth(String text, float scale, float spacing) {
        if (text == null || text.isEmpty() || bigTextGlyphs.isEmpty()) {
            return 0;
        }

        float width = 0;
        String normalized = text.toUpperCase();

        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);

            if (c == ' ') {
                width += 8 * scale;
                continue;
            }

            BufferedImage glyph = bigTextGlyphs.get(c);
            if (glyph == null) {
                width += 4 * scale;
            } else {
                width += glyph.getWidth() * scale + spacing;
            }
        }

        return Math.max(0f, width - spacing);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    private class ShowEntity {
        private BufferedImage[] idleAnimation;
        private int x, y, aniIndex, aniTick;

        public ShowEntity(BufferedImage[] idleAnimation, int x, int y) {
            this.idleAnimation = idleAnimation;
            this.x = x;
            this.y = y;
            this.aniIndex = 0;
            this.aniTick = 0;
        }

        public void draw(Graphics g) {
            if (idleAnimation == null || idleAnimation.length == 0)
                return;
            BufferedImage frame = idleAnimation[aniIndex];
            g.drawImage(frame, x, y,
                    (int) (frame.getWidth() * 4),
                    (int) (frame.getHeight() * 4), null);
        }

        public void update() {
            aniTick++;
            if (aniTick >= 25) {
                aniTick = 0;
                aniIndex = (aniIndex + 1) % idleAnimation.length;
            }
        }
    }
}