package Utilize;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvData) {
        if (!isSolid(x, y, lvData)) {
            if (!isSolid(x + width, y + height, lvData)) {
                if (!isSolid(x + width, y, lvData)) {
                    if (!isSolid(x, y + height, lvData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isSolid(float x, float y, int[][] lvData) {
        int maxWidth = lvData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return isTileSolid((int) xIndex, (int) yIndex, lvData);
    }

    public static boolean isTileSolid(int XTile, int YTile, int[][] lvData) {
        int value = lvData[YTile][XTile];

        if (value >= 48 || value < 0 || value != 11) {
            return true;
        }
        return false;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);

        if (xSpeed > 0) {
            //right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1; //because not inside the wall
        } else {
            //left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityXPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            //falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvData) {
        //check the pixel below bottomleft and bottom right
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvData)) {
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvData) {
        if (xSpeed > 0) {
            return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvData);
        }
        else {
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvData);
        }
    }

    public static boolean isAllTileWalkable(int xStart, int xEnd, int y, int[][] lvData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileSolid(xStart + i, y, lvData)) {
                return false;
            }
            if (!isTileSolid(xStart + i, y + 1, lvData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSightClear ( int[][] lvData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return isAllTileWalkable(secondXTile, firstXTile, yTile, lvData);
        }
        else {
            return isAllTileWalkable(firstXTile, secondXTile, yTile, lvData);
        }
    }
}
