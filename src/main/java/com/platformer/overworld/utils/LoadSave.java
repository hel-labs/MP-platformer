package com.platformer.overworld.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.platformer.core.Game;
import com.platformer.overworld.entities.Crabby;
import com.platformer.overworld.entities.Pinkstar;
import com.platformer.overworld.entities.Shark;
import com.platformer.overworld.objects.BackgroundTree;
import com.platformer.overworld.objects.Cannon;
import com.platformer.overworld.objects.GameContainer;
import com.platformer.overworld.objects.Grass;
import com.platformer.overworld.objects.Potion;
import com.platformer.overworld.objects.Spike;

public class LoadSave {

    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";

    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String PINKSTAR_ATLAS = "pinkstar_atlas.png";
    public static final String SHARK_ATLAS = "shark_atlas.png";

    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "trap_atlas.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "cannon_ball.png";
    public static final String TREE_ONE_ATLAS = "tree_one_atlas.png";
    public static final String TREE_TWO_ATLAS = "tree_two_atlas.png";
    public static final String GRASS_ATLAS = "grass_atlas.png";

    public static final String MENU_BUTTONS = "menu_buttons.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";

    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String COMPLETED_IMG = "completed_sprite.png";
    public static final String GAME_COMPLETED = "game_completed.png";

    public static final String DIALOGUE_BUBBLE_ATLAS = "dialogue_effect.png";
    public static final String RAIN_PARTICLE = "rain_particle.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        try (InputStream is = LoadSave.class.getResourceAsStream("/" + fileName)) {
            if (is != null) {
                img = ImageIO.read(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (img == null) {
            img = createFallbackImage(fileName);
        }

        return img;
    }

    private static BufferedImage createFallbackImage(String fileName) {
        int width = 512;
        int height = 512;

        if (LEVEL_ONE_DATA.equals(fileName)) {
            width = Game.TILES_IN_WIDTH;
            height = Game.TILES_IN_HEIGHT;
        }

        BufferedImage fallback = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int color = new Color(255, 20, 147, 255).getRGB();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fallback.setRGB(x, y, color);
            }
        }
        return fallback;
    }

    public static int[][] GetLevelData() {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);

        for (int j = 0; j < lvlData.length; j++) {
            for (int i = 0; i < lvlData[j].length; i++) {
                int srcX = Math.min(i, img.getWidth() - 1);
                int srcY = Math.min(j, img.getHeight() - 1);

                Color color = new Color(img.getRGB(srcX, srcY), true);
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }

        for (int i = 0; i < lvlData[0].length; i++) {
            lvlData[lvlData.length - 1][i] = 11;
        }

        return lvlData;
    }

    public static ArrayList<Crabby> GetCrabs() {
        return new ArrayList<>();
    }

    public static ArrayList<Pinkstar> GetPinkstars() {
        return new ArrayList<>();
    }

    public static ArrayList<Shark> GetSharks() {
        return new ArrayList<>();
    }

    public static ArrayList<Spike> GetSpikes() {
        return new ArrayList<>();
    }

    public static ArrayList<Potion> GetPotions() {
        return new ArrayList<>();
    }

    public static ArrayList<GameContainer> GetContainers() {
        return new ArrayList<>();
    }

    public static ArrayList<Cannon> GetCannons() {
        return new ArrayList<>();
    }

    public static ArrayList<BackgroundTree> GetTrees() {
        return new ArrayList<>();
    }

    public static ArrayList<Grass> GetGrass() {
        return new ArrayList<>();
    }
}
