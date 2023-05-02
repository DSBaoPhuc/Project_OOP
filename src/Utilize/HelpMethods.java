package Utilize;

import Entities.Crabby;
import Main.Game;
import Objects.Cannon;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Spike_Trap;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Point;

import static Utilize.Constants.EnemyConstant.CRABBY;
import static Utilize.Constants.ObjectConstants.*;

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

    public static boolean canCannonSeePlayer(int[][] lvData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return isAllTilesClear(secondXTile, firstXTile, yTile, lvData);
        }
        else {
            return isAllTilesClear(firstXTile, secondXTile, yTile, lvData);
        }
    }

    public static boolean isAllTilesClear(int xStart, int xEnd, int y, int[][] lvData){
        for (int i = 0; i < xEnd - xStart; i++)
            if (isTileSolid(xStart + i, y, lvData))
                return false;
        return true;
    }

    public static boolean isAllTileWalkable(int xStart, int xEnd, int y, int[][] lvData) {
        if(isAllTilesClear(xStart, xEnd, y, lvData)) {
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!isTileSolid(xStart + i, y + 1, lvData)) {
                    return false;
                }
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

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvData = new int[img.getHeight()][img.getWidth()];

        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                lvData[i][j] = value;
            }
        }
        return lvData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img){
        ArrayList<Crabby> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getGreen();
                if (value == CRABBY) {
                    list.add(new Crabby(j * Game.TILES_SIZE, i * Game.TILES_SIZE));
                }
            }
        }
        return list;
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img){
        ArrayList<Potion> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getBlue();
                if (value == RED_POTION) {
                    list.add(new Potion(j * Game.TILES_SIZE, i * Game.TILES_SIZE, RED_POTION));
                }
                else if(value == BLUE_POTION) {
                    list.add(new Potion(j * Game.TILES_SIZE, i * Game.TILES_SIZE, BLUE_POTION));
                }
            }
        }
        return list;
    }

    public static ArrayList<GameContainer> GetContainer(BufferedImage img){
        ArrayList<GameContainer> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getBlue();
                if (value == BOX) {
                    list.add(new GameContainer(j * Game.TILES_SIZE, i * Game.TILES_SIZE, BOX));
                }
                else if(value == BARREL) {
                    list.add(new GameContainer(j * Game.TILES_SIZE, i * Game.TILES_SIZE, BARREL));
                }
            }
        }
        return list;
    }

    public static ArrayList<Spike_Trap> GetSpikeTraps(BufferedImage img){
        ArrayList<Spike_Trap> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getBlue();
                if (value == SPIKE_TRAP) {
                    list.add(new Spike_Trap(j * Game.TILES_SIZE, i * Game.TILES_SIZE, SPIKE_TRAP));
                }
            }
        }
        return list;
    }

    public static ArrayList<Cannon> GetCannon(BufferedImage img){
        ArrayList<Cannon> list = new ArrayList<>();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getBlue();
                if (value == CANNON_LEFT || value == CANNON_RIGHT) {
                    list.add(new Cannon(j * Game.TILES_SIZE, i * Game.TILES_SIZE, value));
                }
            }
        }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img){
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getGreen();
                if (value == 100) {
                    return new Point(j * Game.TILES_SIZE, i * Game.TILES_SIZE);
                }
            }
        }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }
}
