package com.platformer.overworld.entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.platformer.battle.entities.*;
import com.platformer.core.BattleSnapshot;
import com.platformer.overworld.levels.Level;
import com.platformer.overworld.states.Playing;
import com.platformer.overworld.utils.LoadSave;
import static com.platformer.overworld.utils.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArr, pinkstarArr, sharkArr;
    private Level currentLevel;

    // Tracks which overworld enemy triggered the current battle.
    // Deactivated when battle is won, stays active if fled.
    private Enemy pendingBattleEnemy = null;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        this.currentLevel = level;
    }

    public void update(int[][] lvlData) {
        Player player = playing.getPlayer();

        // Check contact BEFORE updating enemy AI.
        // If contact triggers battle, skip all AI updates this tick.
        if (checkBattleContact(player)) {
            return;
        }

        boolean isAnyActive = false;

        for (Crabby c : currentLevel.getCrabs()) {
            if (c.isActive()) {
                c.update(lvlData, playing);
                isAnyActive = true;
            }
        }

        for (Pinkstar p : currentLevel.getPinkstars()) {
            if (p.isActive()) {
                p.update(lvlData, playing);
                isAnyActive = true;
            }
        }

        for (Shark s : currentLevel.getSharks()) {
            if (s.isActive()) {
                s.update(lvlData, playing);
                isAnyActive = true;
            }
        }

        if (!isAnyActive) {
            playing.setLevelCompleted(true);
        }
    }

    private boolean checkBattleContact(Player player) {
        if (playing.isBattleTriggered()) {
            return false;
        }

        Rectangle2D.Float phb = player.getHitbox();

        for (Crabby c : currentLevel.getCrabs()) {
            if (c.isActive() && c.getState() != DEAD && phb.intersects(c.getHitbox())) {
                pendingBattleEnemy = c;
                triggerBattle(player, new CrabbyBattleEnemy());
                return true;
            }
        }

        for (Pinkstar p : currentLevel.getPinkstars()) {
            if (p.isActive() && p.getState() != DEAD && phb.intersects(p.getHitbox())) {
                pendingBattleEnemy = p;
                triggerBattle(player, new PinkstarBattleEnemy());
                return true;
            }
        }

        for (Shark s : currentLevel.getSharks()) {
            if (s.isActive() && s.getState() != DEAD && phb.intersects(s.getHitbox())) {
                pendingBattleEnemy = s;
                triggerBattle(player, new SharkBattleEnemy());
                return true;
            }
        }

        return false;
    }

    private void triggerBattle(Player player, BattleEnemy battleEnemy) {
        player.setFrozen(true);
        BattleSnapshot snapshot = player.createSnapshot();
        playing.setBattleTriggered(true);
        playing.getGame().startBattle(snapshot, battleEnemy);
    }

    public void onBattleEnd(boolean playerWon) {
        if (playerWon && pendingBattleEnemy != null) {
            pendingBattleEnemy.setActive(false);
        }
        pendingBattleEnemy = null;
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawCrabs(g, xLvlOffset);
        drawPinkstars(g, xLvlOffset);
        drawSharks(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : currentLevel.getCrabs()) {
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
                        (int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
                        (int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y + (int) c.getPushDrawOffset(),
                        CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
            }
        }
    }

    private void drawPinkstars(Graphics g, int xLvlOffset) {
        for (Pinkstar p : currentLevel.getPinkstars()) {
            if (p.isActive()) {
                g.drawImage(pinkstarArr[p.getState()][p.getAniIndex()],
                        (int) p.getHitbox().x - xLvlOffset - PINKSTAR_DRAWOFFSET_X + p.flipX(),
                        (int) p.getHitbox().y - PINKSTAR_DRAWOFFSET_Y + (int) p.getPushDrawOffset(),
                        PINKSTAR_WIDTH * p.flipW(), PINKSTAR_HEIGHT, null);
            }
        }
    }

    private void drawSharks(Graphics g, int xLvlOffset) {
        for (Shark s : currentLevel.getSharks()) {
            if (s.isActive()) {
                g.drawImage(sharkArr[s.getState()][s.getAniIndex()],
                        (int) s.getHitbox().x - xLvlOffset - SHARK_DRAWOFFSET_X + s.flipX(),
                        (int) s.getHitbox().y - SHARK_DRAWOFFSET_Y + (int) s.getPushDrawOffset(),
                        SHARK_WIDTH * s.flipW(), SHARK_HEIGHT, null);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby c : currentLevel.getCrabs()) {
            if (c.isActive() && c.getState() != DEAD && c.getState() != HIT) {
                if (attackBox.intersects(c.getHitbox())) {
                    c.hurt(20);
                    return;
                }
            }
        }

        for (Pinkstar p : currentLevel.getPinkstars()) {
            if (p.isActive()) {
                if (p.getState() == ATTACK && p.getAniIndex() >= 3) {
                    return;
                }
                if (p.getState() != DEAD && p.getState() != HIT) {
                    if (attackBox.intersects(p.getHitbox())) {
                        p.hurt(20);
                        return;
                    }
                }
            }
        }

        for (Shark s : currentLevel.getSharks()) {
            if (s.isActive() && s.getState() != DEAD && s.getState() != HIT) {
                if (attackBox.intersects(s.getHitbox())) {
                    s.hurt(20);
                    return;
                }
            }
        }
    }

    private void loadEnemyImgs() {
        crabbyArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9, 5, CRABBY_WIDTH_DEFAULT,
                CRABBY_HEIGHT_DEFAULT);
        pinkstarArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS), 8, 5, PINKSTAR_WIDTH_DEFAULT,
                PINKSTAR_HEIGHT_DEFAULT);
        sharkArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8, 5, SHARK_WIDTH_DEFAULT,
                SHARK_HEIGHT_DEFAULT);
    }

    private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
        BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
        for (int j = 0; j < tempArr.length; j++) {
            for (int i = 0; i < tempArr[j].length; i++) {
                tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
            }
        }
        return tempArr;
    }

    public void resetAllEnemies() {
        for (Crabby c : currentLevel.getCrabs()) {
            c.resetEnemy();
        }
        for (Pinkstar p : currentLevel.getPinkstars()) {
            p.resetEnemy();
        }
        for (Shark s : currentLevel.getSharks()) {
            s.resetEnemy();
        }
        pendingBattleEnemy = null;
    }
}
