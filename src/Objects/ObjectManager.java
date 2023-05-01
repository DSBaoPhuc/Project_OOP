package Objects;

import GameStates.Playing;
import Levels.Level;
import Utilize.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilize.Constants.ObjectConstants.*;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;

    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();
    }

    public void checkObjTouchPlayer(Rectangle2D.Float hitbox){
        for(Potion p : potions){
            if(p.isActive()){
                if(hitbox.intersects(p.getHitbox())){
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }


    }

    public void applyEffectToPlayer(Potion p){
        if(p.getObjectType() == RED_POTION){
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        }
        else {
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    public void checkObjHit(Rectangle2D.Float attack_box){
        for(GameContainer gc: containers){
            if(gc.isActive()){
                if(gc.getHitbox().intersects(attack_box)){
                    gc.setAnimation(true);

                    int type = 0;
                    if(gc.getObjectType() == BARREL){
                        type = 1;
                    }

                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2) ,
                            (int) (gc.getHitbox().y - gc.getHitbox().height /3), type ));
                }
            }
        }
    }

    public void loadObject(Level newLevel) {
        potions = newLevel.getPotions();
        containers = newLevel.getContainers();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for (int i = 0; i < potionImgs.length ; i++) {
            for (int j = 0; j < potionImgs[i].length; j++) {
                potionImgs[i][j] = potionSprite.getSubimage(12 * j, 16 * i, 12 , 16);
            }
        }


        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int i = 0; i < containerImgs.length ; i++) {
            for (int j = 0; j < containerImgs[i].length; j++) {
                containerImgs[i][j] = containerSprite.getSubimage(40 * j, 30 * i, 40 , 30);
            }
        }
    }

    public void update(){
        for(Potion p: potions){
            if(p.isActive()){
                p.update();
            }
        }

        for(GameContainer gc: containers){
            if(gc.isActive()){
                gc.update();
            }
        }
    }

    public void draw(Graphics g, int xLvOffset){
        drawPotions(g, xLvOffset);
        drawContainer(g, xLvOffset);
    }

    private void drawContainer(Graphics g, int xLvOffset) {
        for(GameContainer gc: containers){
            if(gc.isActive()){
                int type = 0;
                if(gc.getObjectType() == BARREL){
                    type = 1;
                }
                g.drawImage(containerImgs[type][gc.getAniIndex()],
                        (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvOffset),
                        (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT, null );
            }
        }
    }

    private void drawPotions(Graphics g, int xLvOffset) {
        for(Potion p: potions) {
            if (p.isActive()) {
                int type = 0;
                if (p.getObjectType() == RED_POTION) {
                    type = 1;
                }

                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int) (p.getHitbox().x - p.getxDrawOffset() - xLvOffset),
                        (int) (p.getHitbox().y - p.getyDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT, null );
            }
        }
    }

    public void resetAllObj(){
        for(Potion p: potions){
            p.reset();
        }

        for(GameContainer gc: containers){
            gc.reset();
        }
    }

}
