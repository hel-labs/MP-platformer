package com.platformer.overworld.levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.platformer.core.Game;
import com.platformer.overworld.utils.LoadSave;

public class LevelManager {

	private final Game game;
	private BufferedImage[] levelSprite;
	private final ArrayList<Level> levels = new ArrayList<>();
	private int levelIndex;

	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		loadLevels();
	}

	private void loadLevels() {
		levels.clear();

		Level levelOne = new Level(LoadSave.GetLevelData());
		levelOne.setCrabs(LoadSave.GetCrabs());
		levelOne.setPinkstars(LoadSave.GetPinkstars());
		levelOne.setSharks(LoadSave.GetSharks());

		levelOne.setSpikes(LoadSave.GetSpikes());
		levelOne.setPotions(LoadSave.GetPotions());
		levelOne.setContainers(LoadSave.GetContainers());
		levelOne.setCannons(LoadSave.GetCannons());
		levelOne.setTrees(LoadSave.GetTrees());
		levelOne.setGrass(LoadSave.GetGrass());

		levels.add(levelOne);
		levelIndex = 0;
	}

	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[48];
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 12; i++) {
				int index = j * 12 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
		}
	}

	public void draw(Graphics g, int xLvlOffset) {
		Level lvl = getCurrentLevel();
		int[][] lvlData = lvl.getLevelData();

		for (int j = 0; j < lvlData.length; j++) {
			for (int i = 0; i < lvlData[j].length; i++) {
				int index = lvl.getSpriteIndex(i, j);
				if (index < 0 || index >= levelSprite.length) {
					index = 0;
				}
				g.drawImage(levelSprite[index],
					Game.TILES_SIZE * i - xLvlOffset,
					Game.TILES_SIZE * j,
					Game.TILES_SIZE,
					Game.TILES_SIZE,
					null);
			}
		}
	}

	public void update() {
		// Reserved for animated tile updates.
	}

	public Level getCurrentLevel() {
		return levels.get(levelIndex);
	}

	public int getLevelIndex() {
		return levelIndex;
	}

	public void loadNextLevel() {
		levelIndex++;
		if (levelIndex >= levels.size()) {
			levelIndex = 0;
		}
	}

	public int getAmountOfLevels() {
		return levels.size();
	}
}
