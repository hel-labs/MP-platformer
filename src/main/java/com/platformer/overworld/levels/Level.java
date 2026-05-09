package com.platformer.overworld.levels;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.platformer.overworld.entities.*;
import com.platformer.overworld.objects.*;
import com.platformer.core.Game;

import static com.platformer.overworld.utils.Constants.EnemyConstants.*;
import static com.platformer.overworld.utils.Constants.ObjectConstants.*;

/**
 * Represents a parsed level with tile, enemy, and object data.
 */
public class Level {

    private BufferedImage img;
    private int[][] lvlData;

    private ArrayList<Crabby> crabs = new ArrayList<>();
    private ArrayList<Pinkstar> pinkstars = new ArrayList<>();
    private ArrayList<Shark> sharks = new ArrayList<>();
    private ArrayList<Potion> potions = new ArrayList<>();
    private ArrayList<Spike> spikes = new ArrayList<>();
    private ArrayList<GameContainer> containers = new ArrayList<>();
    private ArrayList<Cannon> cannons = new ArrayList<>();
    private ArrayList<BackgroundTree> trees = new ArrayList<>();
    private ArrayList<Grass> grass = new ArrayList<>();

    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    /**
     * @param img level image used for data extraction
     */
    public Level(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        loadLevel();
        calcLvlOffsets();
    }

    private void loadLevel() {

        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc..
        // Removed many methods in HelpMethods class.
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color c = new Color(img.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
        }
    }

    private void loadLevelData(int redValue, int x, int y) {
        if (redValue >= 50) {
            lvlData[y][x] = 0;
        } else {
            lvlData[y][x] = redValue;
        }
        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                grass.add(new Grass((int) (x * Game.TILES_SIZE), (int) (y * Game.TILES_SIZE) - Game.TILES_SIZE, getRndGrassType(x)));
        }
    }

    private int getRndGrassType(int xPos) {
        return xPos % 2;
    }

    private void loadEntities(int greenValue, int x, int y) {
        switch (greenValue) {
            case CRABBY ->
                crabs.add(new Crabby(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case PINKSTAR ->
                pinkstars.add(new Pinkstar(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case SHARK ->
                sharks.add(new Shark(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
            case 100 ->
                playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        switch (blueValue) {
            case RED_POTION, BLUE_POTION ->
                potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case BOX, BARREL ->
                containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case SPIKE ->
                spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            case CANNON_LEFT, CANNON_RIGHT ->
                cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
            case TREE_ONE, TREE_TWO, TREE_THREE ->
                trees.add(new BackgroundTree(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
        }
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    /**
     * @param x tile x
     * @param y tile y
     * @return sprite index at the tile location
     */
    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    /** @return collision data for the level */
    public int[][] getLevelData() {
        return lvlData;
    }

    /** @return maximum horizontal offset */
    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    /** @return player spawn point */
    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    /** @return crab enemies list */
    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    /** @return shark enemies list */
    public ArrayList<Shark> getSharks() {
        return sharks;
    }

    /** @return potions list */
    public ArrayList<Potion> getPotions() {
        return potions;
    }

    /** @return breakable containers list */
    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    /** @return spikes list */
    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    /** @return cannons list */
    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    /** @return pinkstar enemies list */
    public ArrayList<Pinkstar> getPinkstars() {
        return pinkstars;
    }

    /** @return background trees list */
    public ArrayList<BackgroundTree> getTrees() {
        return trees;
    }

    /** @return grass decorations list */
    public ArrayList<Grass> getGrass() {
        return grass;
    }

}
