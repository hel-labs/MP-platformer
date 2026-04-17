package com.platformer.overworld.levels;

import java.util.ArrayList;
import java.util.List;

import com.platformer.overworld.entities.Crabby;
import com.platformer.overworld.entities.Pinkstar;
import com.platformer.overworld.entities.Shark;
import com.platformer.overworld.objects.BackgroundTree;
import com.platformer.overworld.objects.Cannon;
import com.platformer.overworld.objects.GameContainer;
import com.platformer.overworld.objects.Grass;
import com.platformer.overworld.objects.Potion;
import com.platformer.overworld.objects.Spike;

public class Level {

	private final int[][] lvlData;

	private final ArrayList<Crabby> crabs = new ArrayList<>();
	private final ArrayList<Pinkstar> pinkstars = new ArrayList<>();
	private final ArrayList<Shark> sharks = new ArrayList<>();

	private final ArrayList<Spike> spikes = new ArrayList<>();
	private final ArrayList<Potion> potions = new ArrayList<>();
	private final ArrayList<GameContainer> containers = new ArrayList<>();
	private final ArrayList<Cannon> cannons = new ArrayList<>();
	private final ArrayList<BackgroundTree> trees = new ArrayList<>();
	private final ArrayList<Grass> grass = new ArrayList<>();

	public Level(int[][] lvlData) {
		this.lvlData = lvlData;
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}

	public ArrayList<Crabby> getCrabs() {
		return crabs;
	}

	public ArrayList<Pinkstar> getPinkstars() {
		return pinkstars;
	}

	public ArrayList<Shark> getSharks() {
		return sharks;
	}

	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public ArrayList<Potion> getPotions() {
		return potions;
	}

	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	public ArrayList<Cannon> getCannons() {
		return cannons;
	}

	public ArrayList<BackgroundTree> getTrees() {
		return trees;
	}

	public ArrayList<Grass> getGrass() {
		return grass;
	}

	public void setCrabs(List<Crabby> list) {
		crabs.clear();
		crabs.addAll(list);
	}

	public void setPinkstars(List<Pinkstar> list) {
		pinkstars.clear();
		pinkstars.addAll(list);
	}

	public void setSharks(List<Shark> list) {
		sharks.clear();
		sharks.addAll(list);
	}

	public void setSpikes(List<Spike> list) {
		spikes.clear();
		spikes.addAll(list);
	}

	public void setPotions(List<Potion> list) {
		potions.clear();
		potions.addAll(list);
	}

	public void setContainers(List<GameContainer> list) {
		containers.clear();
		containers.addAll(list);
	}

	public void setCannons(List<Cannon> list) {
		cannons.clear();
		cannons.addAll(list);
	}

	public void setTrees(List<BackgroundTree> list) {
		trees.clear();
		trees.addAll(list);
	}

	public void setGrass(List<Grass> list) {
		grass.clear();
		grass.addAll(list);
	}
}
