package com.platformer.overworld.entities;

import static com.platformer.overworld.utils.Constants.Directions.LEFT;
import static com.platformer.overworld.utils.Constants.Directions.RIGHT;
import static com.platformer.overworld.utils.Constants.Directions.UP;
import static com.platformer.overworld.utils.HelpMethods.CanMoveHere;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.platformer.core.Game;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected Rectangle2D.Float attackBox;

	protected int aniTick, aniIndex;
	protected int state;

	protected int maxHealth;
	protected int currentHealth;
	protected float walkSpeed;

	protected boolean inAir;
	protected float airSpeed;

	protected int pushBackDir = LEFT;
	protected int pushBackOffsetDir = UP;
	protected float pushDrawOffset;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	protected void newState(int state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}

	protected void pushBack(int pushBackDir, int[][] lvlData, float speedMult) {
		float xSpeed = 0;
		if (pushBackDir == LEFT) {
			xSpeed = -walkSpeed;
		} else if (pushBackDir == RIGHT) {
			xSpeed = walkSpeed;
		}

		if (CanMoveHere(hitbox.x + xSpeed * speedMult, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed * speedMult;
		}
	}

	protected void updatePushBackDrawOffset() {
		float speed = 0.95f * Game.SCALE;
		if (pushBackOffsetDir == UP) {
			pushDrawOffset -= speed;
			if (pushDrawOffset < -30 * Game.SCALE) {
				pushBackOffsetDir = LEFT;
			}
		} else {
			if (pushDrawOffset < 0) {
				pushDrawOffset += speed;
			}
		}
	}

	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	public void drawAttackBox(Graphics g, int xLvlOffset) {
		if (attackBox == null) {
			return;
		}
		g.setColor(Color.RED);
		g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getState() {
		return state;
	}

	public int getAniIndex() {
		return aniIndex;
	}
}
