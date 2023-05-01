package Levels;

import Entities.Crabby;
import Main.Game;
import Objects.GameContainer;
import Objects.Potion;
import Utilize.HelpMethods;
import Utilize.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilize.HelpMethods.*;

public class Level {
    private BufferedImage img;
    private int[][] lvData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private int lvTilesWide;
    private int maxTilesOffset;
    private int maxLvOffsetX;
    private Point playerSpawn;

    public Level (BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        createPotion();
        createContainer();
        calculateLvOffset();
        calculatePlayerSpawn();
    }

    private void createContainer() {
        containers = HelpMethods.GetContainer(img);
    }

    private void createPotion() {
        potions = HelpMethods.GetPotions(img);
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calculateLvOffset() {
        lvTilesWide = img.getWidth();
        maxTilesOffset = lvTilesWide - Game.TILES_IN_WIDTH;
        maxLvOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvData[y][x];
    }

    public int[][] getLevelData(){
        return lvData;
    }

    public int getLvOffset(){
        return maxLvOffsetX;
    }

    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }

    public Point getPlayerSpawn(){
        return playerSpawn;
    }

//    public BufferedImage getImg() {
//        return img;
//    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }
}
