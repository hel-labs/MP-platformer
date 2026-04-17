package com.platformer.overworld.utils;

import java.awt.geom.Rectangle2D;

import com.platformer.core.Game;
import com.platformer.overworld.objects.Projectile;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (lvlData == null) {
            return true;
        }

        if (!IsSolid(x, y, lvlData)) {
            if (!IsSolid(x + width, y + height, lvlData)) {
                if (!IsSolid(x + width, y, lvlData)) {
                    if (!IsSolid(x, y + height, lvlData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (lvlData == null) {
            return true;
        }
        return IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)
            || IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (lvlData == null) {
            return true;
        }
        return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData)
            || IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        if (x < 0 || x >= Game.GAME_WIDTH) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int maxY = lvlData.length - 1;
        int maxX = lvlData[0].length - 1;

        int row = Math.max(0, Math.min((int) yIndex, maxY));
        int col = Math.max(0, Math.min((int) xIndex, maxX));

        int value = lvlData[row][col];

        return value >= 48 || value < 0;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        if (airSpeed > 0) {
            int currentTile = (int) ((hitbox.y + hitbox.height) / Game.TILES_SIZE);
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (lvlData == null) {
            return true;
        }
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (lvlData == null) {
            return false;
        }
        int centerX = (int) ((hitbox.x + hitbox.width / 2) / Game.TILES_SIZE);
        int centerY = (int) ((hitbox.y + hitbox.height / 2) / Game.TILES_SIZE);

        centerY = Math.max(0, Math.min(centerY, lvlData.length - 1));
        centerX = Math.max(0, Math.min(centerX, lvlData[0].length - 1));

        return lvlData[centerY][centerX] == 0;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        int minX = Math.min(firstXTile, secondXTile);
        int maxX = Math.max(firstXTile, secondXTile);

        for (int i = minX; i <= maxX; i++) {
            if (i < 0 || i >= lvlData[0].length || yTile < 0 || yTile >= lvlData.length) {
                return false;
            }
            int value = lvlData[yTile][i];
            if (value >= 48 || value < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        return IsSightClear(lvlData, firstHitbox, secondHitbox, yTile);
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        Rectangle2D.Float hitbox = p.getHitbox();
        return IsSolid(hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height / 2, lvlData);
    }
}
