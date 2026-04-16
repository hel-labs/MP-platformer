package com.echoes.animation;

import com.echoes.exceptions.DataException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loads a spritesheet PNG and slices it into individual frames.
 *
 * Spritesheet layout convention:
 *   Each ROW    = one animation state
 *   Each COLUMN = one frame within that animation
 *
 * All frames must be the same pixel dimensions.
 *
 * PLACEHOLDER: pass any PNG path. Replace with your real spritesheet later.
 * If the resource is not found, a fallback colored image is used so the
 * game never crashes on missing sprites during development.
 */
public class SpriteSheet {

    private final BufferedImage sheet;
    private final int frameWidth;
    private final int frameHeight;

    /**
     * @param resourcePath  classpath path e.g. "/sprites/player.png"
     * @param frameWidth    width of a single frame in pixels
     * @param frameHeight   height of a single frame in pixels
     */
    public SpriteSheet(String resourcePath, int frameWidth, int frameHeight) {
        this.frameWidth  = frameWidth;
        this.frameHeight = frameHeight;

        BufferedImage loaded = null;
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is != null) {
                loaded = ImageIO.read(is);
            } else {
                // PLACEHOLDER fallback — solid color sheet used until real
                // sprites are dropped into src/main/resources/sprites/
                loaded = makePlaceholderSheet(frameWidth, frameHeight);
            }
        } catch (IOException e) {
            loaded = makePlaceholderSheet(frameWidth, frameHeight);
        }
        this.sheet = loaded;
    }

    /**
     * Returns the frame at the given column and row in the spritesheet grid.
     * Row 0 = first animation, Column 0 = first frame.
     */
    public BufferedImage getFrame(int col, int row) {
        int x = col * frameWidth;
        int y = row * frameHeight;

        // Guard against out-of-bounds slicing
        if (x + frameWidth  > sheet.getWidth()  ||
            y + frameHeight > sheet.getHeight()) {
            return makePlaceholderFrame(frameWidth, frameHeight,
                                        java.awt.Color.MAGENTA);
        }
        return sheet.getSubimage(x, y, frameWidth, frameHeight);
    }

    /**
     * Returns all frames in a single row as an array.
     * Use this to build one Animation per row.
     *
     * @param row        the row index (0-based)
     * @param frameCount how many frames are in this row
     */
    public BufferedImage[] getRow(int row, int frameCount) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = getFrame(i, row);
        }
        return frames;
    }

    public int getFrameWidth()  { return frameWidth;  }
    public int getFrameHeight() { return frameHeight; }
    public int getSheetWidth()  { return sheet.getWidth();  }
    public int getSheetHeight() { return sheet.getHeight(); }

    // ---------------------------------------------------------------
    // Placeholder helpers — used when real sprites are not yet present
    // ---------------------------------------------------------------

    /**
     * Creates a 4x6 grid placeholder sheet (4 cols × 6 rows) filled with
     * a default grey color. Each cell = one frame placeholder.
     */
    private BufferedImage makePlaceholderSheet(int fw, int fh) {
        int cols = 6;
        int rows = 6;
        BufferedImage img = new BufferedImage(fw * cols, fh * rows,
                                              BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(java.awt.Color.DARK_GRAY);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        // Draw a simple humanoid silhouette in the first frame as a marker
        g.setColor(java.awt.Color.LIGHT_GRAY);
        g.fillOval(fw / 4, 2, fw / 2, fw / 2);          // head
        g.fillRect(fw / 4, fw / 2 + 2, fw / 2, fh / 2); // body
        g.dispose();
        return img;
    }

    private BufferedImage makePlaceholderFrame(int fw, int fh,
                                               java.awt.Color color) {
        BufferedImage img = new BufferedImage(fw, fh,
                                             BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, fw, fh);
        g.dispose();
        return img;
    }
}
