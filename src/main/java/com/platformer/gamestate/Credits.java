package com.platformer.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.platformer.core.Game;
import com.platformer.overworld.utils.LoadSave;

public class Credits extends State implements Statemethods {
	private BufferedImage backgroundImg, creditsImg;
	private int bgX, bgY, bgW, bgH;
	private float bgYFloat;

	private ArrayList<ShowEntity> entitiesList;

	public Credits(Game game) {
		super(game);
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		creditsImg = LoadSave.GetSpriteAtlas(LoadSave.CREDITS);
		bgW = (int) (creditsImg.getWidth() * Game.SCALE);
		bgH = (int) (creditsImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = Game.GAME_HEIGHT;
		loadEntities();
	}

	private void loadEntities() {
		entitiesList = new ArrayList<>();
		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS), 5, 64, 40), (int) (Game.GAME_WIDTH * 0.05), (int) (Game.GAME_HEIGHT * 0.8)));
		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9, 72, 32), (int) (Game.GAME_WIDTH * 0.15), (int) (Game.GAME_HEIGHT * 0.75)));
		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS), 8, 34, 30), (int) (Game.GAME_WIDTH * 0.7), (int) (Game.GAME_HEIGHT * 0.75)));
		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8, 34, 30), (int) (Game.GAME_WIDTH * 0.8), (int) (Game.GAME_HEIGHT * 0.8)));
	}

	private BufferedImage[] getIdleAni(BufferedImage atlas, int spritesAmount, int width, int height) {
		BufferedImage[] arr = new BufferedImage[spritesAmount];
		for (int i = 0; i < spritesAmount; i++)
			arr[i] = atlas.getSubimage(width * i, 0, width, height);
		return arr;
	}

	@Override
	public void update() {
		bgYFloat -= 0.2f;
		for (ShowEntity se : entitiesList)
			se.update();
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int drawY = (int) (bgY + bgYFloat);

		g2.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g2.drawImage(creditsImg, bgX, drawY, bgW, bgH, null);
		drawReplacementCreditsText(g2, drawY);

		for (ShowEntity se : entitiesList)
			se.draw(g2);
	}

	private void drawReplacementCreditsText(Graphics2D g2, int panelY) {
		int margin = scaled(8);
		int innerX = bgX + margin;
		int innerY = panelY + margin;
		int innerW = bgW - margin * 2;
		int innerH = bgH - margin * 2;

		// Cover the baked-in text while keeping the same animated panel and border.
		g2.setColor(new Color(238, 189, 138));
		g2.fillRect(innerX, innerY, innerW, innerH);

		Object oldAA = g2.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		Color textColor = new Color(53, 47, 72);
		Color shadowColor = new Color(188, 84, 78);

		Font headingFont = new Font("Monospaced", Font.BOLD, scaled(13));
		Font bodyFont = new Font("Monospaced", Font.BOLD, scaled(9));

		int centerX = bgX + bgW / 2;

		drawCenteredStylizedText(g2, "contributors", headingFont, centerX, panelY + scaled(80), textColor, shadowColor);
		drawCenteredStylizedText(g2, "Sami Sharif Arka", bodyFont, centerX, panelY + scaled(145), textColor, shadowColor);
		drawCenteredStylizedText(g2, "Fahim Muntasir Galib", bodyFont, centerX, panelY + scaled(178), textColor, shadowColor);

		drawCenteredStylizedText(g2, "Playtester", headingFont, centerX, panelY + scaled(340), textColor, shadowColor);
		drawCenteredStylizedText(g2, "Nur e Samdani", bodyFont, centerX, panelY + scaled(405), textColor, shadowColor);

		drawCenteredStylizedText(g2, "Thank you so much for", headingFont, centerX, panelY + scaled(690), textColor, shadowColor);
		drawCenteredStylizedText(g2, "playing this game!", headingFont, centerX, panelY + scaled(735), textColor, shadowColor);

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldAA);
	}

	private void drawCenteredStylizedText(Graphics2D g2, String text, Font font, int centerX, int topY, Color textColor, Color shadowColor) {
		g2.setFont(font);
		FontMetrics fm = g2.getFontMetrics();
		int x = centerX - fm.stringWidth(text) / 2;
		int baseline = topY + fm.getAscent();

		g2.setColor(shadowColor);
		g2.drawString(text, x + scaled(1), baseline + scaled(1));

		g2.setColor(textColor);
		g2.drawString(text, x, baseline);
	}

	private int scaled(int value) {
		return Math.max(value, Math.round(value * Game.SCALE));
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

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
		}

		public void draw(Graphics g) {
			g.drawImage(idleAnimation[aniIndex], x, y, (int) (idleAnimation[aniIndex].getWidth() * 4), (int) (idleAnimation[aniIndex].getHeight() * 4), null);
		}

		public void update() {
			aniTick++;
			if (aniTick >= 25) {
				aniTick = 0;
				aniIndex++;
				if (aniIndex >= idleAnimation.length)
					aniIndex = 0;
			}

		}
	}

}