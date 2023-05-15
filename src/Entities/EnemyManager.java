package Entities;

import GameStates.Playing;
import Levels.Level;
import Utilize.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilize.Constants.EnemyConstant.*;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] monsterArr;
    private ArrayList <Monster> mons = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        mons = level.getMonsters();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for (Monster c: mons){
            if(c.getCurrentHealth() > 0){
                if(c.isActive()) {
                    if (attackBox.intersects(c.getHitbox())) {
                        c.hurt(10);
                        return;
                    }
                }
            }
        }
    }

    private void loadEnemyImgs() {
        monsterArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MONSTER_SPRITE);
        for (int i = 0; i < monsterArr.length ; i++) {
            for (int j = 0; j < monsterArr[i].length; j++) {
                monsterArr[i][j] = temp.getSubimage(j * MONSTER_WIDTH_DEFAULT, i * MONSTER_HEIGHT_DEFAULT, MONSTER_WIDTH_DEFAULT, MONSTER_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAllEnemies(){
        for(Monster c: mons){
            c.resetEnemy();
        }
    }

    public void update(int[][] lvData, Player player){
        boolean isAnyActive = false;
        for(Monster c : mons) {
            if (c.isActive()) {
                c.update(lvData, player);
                isAnyActive = true;
            }
        }
        if(!isAnyActive){
            playing.setLevelCompleted(true);
        }
    }

    public void draw(Graphics g, int xLvOffset){
        drawMonsters(g, xLvOffset);
    }

    private void drawMonsters(Graphics g, int xLvOffset) {
        for(Monster c : mons){
            if(c.isActive()) {
                g.drawImage(monsterArr[c.getState()][c.getAniIndex()], (int) c.getHitbox().x - xLvOffset - MONSTER_DRAWOFFSET_X + c.flipX(), (int) c.getHitbox().y - MONSTER_DRAWOFFSET_Y, MONSTER_WIDTH * c.flipW(), MONSTER_HEIGHT, null);
//                c.drawAttack(g, xLvOffset);
            }
        }
    }
}
