package Levels;

import GameStates.GameStates;
import Main.Game;
import Utilize.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.TILES_SIZE;


public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    private int lvIndex = 0;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public void loadNextLevel(){
        lvIndex++;

        if(lvIndex >= levels.size()){
            lvIndex = 0;
            System.out.println("No more level");
            GameStates.state = GameStates.MENU;
        }

        Level newLevel = levels.get(lvIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
        game.getPlaying().setMaxLvOffset(newLevel.getLvOffset());
        game.getPlaying().getObjectManager().loadObject(newLevel);
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevel();
        for(BufferedImage img: allLevels){
            levels.add(new Level(img));
        }
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 12; i++){
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int lvOffset){
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(lvIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvIndex).getSpriteIndex(i,j);
                g.drawImage(levelSprite[index],Game.TILES_SIZE * i - lvOffset,Game.TILES_SIZE * j,TILES_SIZE,TILES_SIZE,null);
            }
            
        }

    }

    public void update(){

    }

    public Level getCurrentLevel() {
        return levels.get(lvIndex);
    }

    public int getAmountOfLevel(){
        return levels.size();
    }

    public int getLvIndex(){
        return lvIndex;
    }
}
