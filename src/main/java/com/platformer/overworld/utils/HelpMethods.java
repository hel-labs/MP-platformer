package com.platformer.overworld.utils;

import java.awt.geom.Rectangle2D;

import com.platformer.core.Game;
import com.platformer.overworld.objects.Projectile;

/**
 * Helper methods for collision checks, visibility, and tile queries.
 */
public class HelpMethods {

    /**
     * Checks whether a rectangle can move to the given coordinates.
     *
     * @param x target x
     * @param y target y
     * @param width rectangle width
     * @param height rectangle height
     * @param lvlData level collision grid
     * @return true if the area is walkable
     */
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
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

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    /**
     * @param p projectile
     * @param lvlData level collision grid
     * @return true if the projectile is colliding with a solid tile
     */
    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
    }

    /**
     * @param hitbox entity hitbox
     * @param lvlData level collision grid
     * @return true if the entity overlaps water tiles
     */
    public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
        // Will only check if entity touch top water. Can't reach bottom water if not
        // touched top water.
        if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48) {
            if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48) {
                return false;
            }
        }
        return true;
    }

    private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
        int xCord = (int) (xPos / Game.TILES_SIZE);
        int yCord = (int) (yPos / Game.TILES_SIZE);
        return lvlData[yCord][xCord];
    }

    /**
     * @param xTile tile x coordinate
     * @param yTile tile y coordinate
     * @param lvlData level collision grid
     * @return true if the tile is solid
     */
    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        switch (value) {
            case 11, 48, 49:
                return false;
            default:
                return true;
        }

    }

    /**
     * @param hitbox entity hitbox
     * @param xSpeed current horizontal speed
     * @return corrected x position next to a blocking wall
     */
    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else // Left
        {
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * @param hitbox entity hitbox
     * @param airSpeed current vertical speed
     * @return corrected y position below a roof or above a floor
     */
    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // Falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else // Jumping
        {
            return currentTile * Game.TILES_SIZE;
        }

    }

    /**
     * @param hitbox entity hitbox
     * @param lvlData level collision grid
     * @return true if the entity is standing on solid ground
     */
    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
            if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param hitbox entity hitbox
     * @param xSpeed horizontal speed
     * @param lvlData level collision grid
     * @return true if there is floor at the next horizontal step
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0) {
            return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        } else {
            return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        }
    }

    /**
     * @param hitbox entity hitbox
     * @param lvlData level collision grid
     * @return true if there is floor directly under the entity
     */
    public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
            if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param lvlData level collision grid
     * @param firstHitbox cannon hitbox
     * @param secondHitbox player hitbox
     * @param yTile cannon tile row
     * @return true if the cannon has line-of-sight to the player
     */
    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
        }
    }

    /**
     * @param xStart start tile x
     * @param xEnd end tile x
     * @param y tile row
     * @param lvlData level collision grid
     * @return true if all tiles in the range are non-solid
     */
    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param xStart start tile x
     * @param xEnd end tile x
     * @param y tile row
     * @param lvlData level collision grid
     * @return true if all tiles are clear and have solid floor underneath
     */
    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData)) {
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, y + 1, lvlData)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Player can sometimes be on an edge and in sight of enemy.
    // The old method would return false because the player x is not on edge.
    // This method checks both player x and player x + width.
    // If tile under playerBox.x is not solid, we switch to playerBox.x +
    // playerBox.width;
    // One of them will be true, because of prior checks.
    /**
     * Checks line-of-sight between an enemy and the player.
     *
     * @param lvlData level collision grid
     * @param enemyBox enemy hitbox
     * @param playerBox player hitbox
     * @param yTile tile row
     * @return true if no blocking tiles are between the two
     */
    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
        int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);

        int secondXTile;
        if (IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData)) {
            secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
        } else {
            secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);
        }

        if (firstXTile > secondXTile) {
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }

    /**
     * Legacy line-of-sight check that assumes the player box is fully supported.
     *
     * @param lvlData level collision grid
     * @param firstHitbox first hitbox
     * @param secondHitbox second hitbox
     * @param yTile tile row
     * @return true if no blocking tiles are between the two
     */
    public static boolean IsSightClear_OLD(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        } else {
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }
}
