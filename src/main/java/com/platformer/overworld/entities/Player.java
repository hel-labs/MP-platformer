package com.platformer.overworld.entities;

import static com.platformer.overworld.utils.Constants.Directions.LEFT;
import static com.platformer.overworld.utils.Constants.Directions.RIGHT;
import static com.platformer.overworld.utils.Constants.PlayerConstants.ATTACK;
import static com.platformer.overworld.utils.Constants.PlayerConstants.FALLING;
import static com.platformer.overworld.utils.Constants.PlayerConstants.GetSpriteAmount;
import static com.platformer.overworld.utils.Constants.PlayerConstants.HIT;
import static com.platformer.overworld.utils.Constants.PlayerConstants.IDLE;
import static com.platformer.overworld.utils.Constants.PlayerConstants.JUMP;
import static com.platformer.overworld.utils.Constants.PlayerConstants.RUNNING;
import static com.platformer.overworld.utils.HelpMethods.CanMoveHere;
import static com.platformer.overworld.utils.HelpMethods.GetEntityXPosNextToWall;
import static com.platformer.overworld.utils.HelpMethods.GetEntityYPosUnderRoofOrAboveFloor;
import static com.platformer.overworld.utils.HelpMethods.IsEntityOnFloor;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.platformer.core.Game;
import com.platformer.overworld.utils.LoadSave;
import com.platformer.core.BattleSnapshot;
import com.platformer.battle.core.BattleSnapshot;

public class Player extends Entity {

	private int hp     = 80;
private int maxHp  = 80;
private int attack = 10;
private boolean frozen = false;

	private BufferedImage[][] animations;
	private int playerAction = IDLE;

	private boolean moving;
	private boolean attacking;
	private boolean left, up, right, down, jump;

	private float playerSpeed = 1.0f * Game.SCALE;
	private int[][] lvlData;

	private final float xDrawOffset = 21 * Game.SCALE;
	private final float yDrawOffset = 4 * Game.SCALE;

	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

	private int tileY;
	private int maxPower = 100;
	private int currentPower = maxPower;

	private int attackBoxOffsetX;

	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(20, 27);
		initAttackBox();

		maxHealth = 100;
		currentHealth = maxHealth;
		walkSpeed = playerSpeed;
	}

	public BattleSnapshot createSnapshot() {
    return new BattleSnapshot(hp, maxHp, attack);
}
public void applyOutcome(BattleOutcome outcome) {
    if (outcome.isLose()) hp = Math.max(1, hp / 2);
    else                  hp = outcome.hpRemaining;
}
public void setFrozen(boolean frozen) { this.frozen = frozen; }
public boolean isFrozen()            { return frozen; }

	private void initAttackBox() {
		attackBoxOffsetX = (int) (Game.SCALE * 20);
		attackBox = new Rectangle2D.Float(x, y, (int) (28 * Game.SCALE), (int) (20 * Game.SCALE));
	}

	public void update() {
		updatePos();
		updateAttackBox();
		updateAnimationTick();
		setAnimation();
	}

	private void updateAttackBox() {
		if (right) {
			attackBox.x = hitbox.x + hitbox.width;
		} else if (left) {
			attackBox.x = hitbox.x - attackBoxOffsetX;
		} else {
			attackBox.x = hitbox.x + hitbox.width / 2f;
		}
		attackBox.y = hitbox.y + Game.SCALE * 8;
	}

	public void render(Graphics g, int xLvlOffset) {
		if (animations == null) {
			return;
		}
		g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - xLvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
		Toolkit.getDefaultToolkit().sync();
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= com.platformer.overworld.utils.Constants.ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
				if (playerAction == HIT) {
					playerAction = IDLE;
				}
			}
		}
	}

	private void setAnimation() {
		int startAni = playerAction;

		if (attacking) {
			playerAction = ATTACK;
		} else if (inAir) {
			playerAction = airSpeed < 0 ? JUMP : FALLING;
		} else if (moving) {
			playerAction = RUNNING;
		} else {
			playerAction = IDLE;
		}

		if (startAni != playerAction) {
			aniTick = 0;
			aniIndex = 0;
		}
	}

	private void updatePos() {
		if(frozen) return;
		moving = false;

		if (jump) {
			jump();
		}

		float xSpeed = 0;
		if (left) {
			xSpeed -= playerSpeed;
		}
		if (right) {
			xSpeed += playerSpeed;
		}

		if (!inAir && lvlData != null && !IsEntityOnFloor(hitbox, lvlData)) {
			inAir = true;
		}

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}
		} else {
			updateXPos(xSpeed);
		}

		tileY = (int) (hitbox.y / Game.TILES_SIZE);
	}

	private void jump() {
		if (inAir) {
			return;
		}
		inAir = true;
		airSpeed = jumpSpeed;
		jump = false;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if (xSpeed == 0 || lvlData == null) {
			return;
		}
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
			moving = true;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[9][6];
		for (int i = 0; i < animations.length; i++) {
			for (int j = 0; j < animations[i].length; j++) {
				animations[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
			}
		}
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (lvlData != null && !IsEntityOnFloor(hitbox, lvlData)) {
			inAir = true;
		}
	}

	public void resetDirBooleans() {
		left = false;
		up = false;
		right = false;
		down = false;
		jump = false;
	}

	public void changeHealth(int value) {
		currentHealth += value;
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
		if (currentHealth < 0) {
			currentHealth = 0;
		}
	}

	public void changeHealth(int value, Enemy enemy) {
		changeHealth(value);
		if (value < 0) {
			newState(HIT);
			pushBackOffsetDir = com.platformer.overworld.utils.Constants.Directions.UP;
			pushBackDir = enemy.getHitbox().x < hitbox.x ? RIGHT : LEFT;
			pushDrawOffset = 0;
		}
	}

	public void kill() {
		currentHealth = 0;
	}

	public void changePower(int value) {
		currentPower += value;
		if (currentPower > maxPower) {
			currentPower = maxPower;
		}
		if (currentPower < 0) {
			currentPower = 0;
		}
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getTileY() {
		return tileY;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public Rectangle2D.Float getAttackBox() {
		return attackBox;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
}
