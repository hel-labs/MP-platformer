package com.platformer.overworld.objects;

import com.platformer.core.Game;
import com.platformer.overworld.entities.Player;
import com.platformer.overworld.levels.Level;
import com.platformer.gamestate.Playing;
import com.platformer.utils.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.platformer.utils.Constants.ObjectConstants.*;

public class CoinManager {

    private static final int COINS_PER_LEVEL = 10;

    private Playing playing;
    private ArrayList<Coin> coins = new ArrayList<>();
    private BufferedImage[] coinImgs;

    public CoinManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    private void loadImgs() {

        BufferedImage sheet = LoadSave.GetSpriteAtlas(LoadSave.COIN_ATLAS);
        System.out.println("Coin sheet size: " + sheet.getWidth() + "x" + sheet.getHeight());
        coinImgs = new BufferedImage[COIN_ANI_FRAMES];
        for (int i = 0; i < coinImgs.length; i++)
            coinImgs[i] = sheet.getSubimage(
                    i * COIN_WIDTH_DEFAULT, 0,
                    COIN_WIDTH_DEFAULT, COIN_HEIGHT_DEFAULT);
    }

    public void loadCoins(Level level) {
        coins.clear();
        int[][] lvlData = level.getLevelData();
        int rows = lvlData.length;
        int cols = lvlData[0].length;

        // Collect spike tile columns to exclude
        java.util.Set<Long> spikePositions = new java.util.HashSet<>();
        for (com.platformer.overworld.objects.Spike s : level.getSpikes()) {
            int spikeCol = (int) (s.getHitbox().x / Game.TILES_SIZE);
            int spikeRow = (int) (s.getHitbox().y / Game.TILES_SIZE);
            // Block the spike tile row and one above it
            spikePositions.add(encode(spikeCol, spikeRow));
            spikePositions.add(encode(spikeCol, spikeRow - 1));
        }

        // Finds locations without water/spikes etc.
        List<int[]> validPositions = new ArrayList<>();
        for (int row = 0; row < rows - 1; row++) {
            for (int col = 0; col < cols; col++) {
                if (spikePositions.contains(encode(col, row)))
                    continue; // skip spike zones
                int tileVal = lvlData[row][col];
                if (tileVal == 11) {
                    int below = lvlData[row + 1][col];
                    if (below != 11 && below != 48 && below != 49) {
                        validPositions.add(new int[] { col, row });
                    }
                }
            }
        }

        Collections.shuffle(validPositions, new Random());
        int count = Math.min(COINS_PER_LEVEL, validPositions.size());
        for (int i = 0; i < count; i++) {
            int px = validPositions.get(i)[0] * Game.TILES_SIZE;
            int py = validPositions.get(i)[1] * Game.TILES_SIZE;
            coins.add(new Coin(px, py));
        }
    }

    // Encodes col,row into a single long for the HashSet
    private long encode(int col, int row) {
        return ((long) row << 32) | (col & 0xFFFFFFFFL);
    }

    public void update() {
        for (Coin c : coins)
            if (c.isActive())
                c.update();
    }

    public void checkCoinTouched(Player player) {
        for (Coin c : coins) {
            if (c.isActive() && c.getHitbox().intersects(player.getHitbox())) {
                c.setActive(false);
                playing.addPoints(c.getValue());
            }
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (Coin c : coins) {
            if (!c.isActive())
                continue;
            int frame = c.getAniIndex();
            g.drawImage(
                    coinImgs[frame],
                    (int) (c.getHitbox().x - c.getxDrawOffset() - xLvlOffset),
                    (int) (c.getHitbox().y - c.getyDrawOffset()),
                    COIN_WIDTH, COIN_HEIGHT, null);
        }
    }

    public void reset() {
        coins.clear();
    }
}