package Objects;

import Entities.Player;
import GameStates.Playing;
import Levels.Level;
import Main.Game;
import Utilize.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Utilize.Constants.ObjectConstants.*;
import static Utilize.Constants.Projectile.*;
import static Utilize.HelpMethods.canCannonSeePlayer;
import static Utilize.HelpMethods.isProjectileHittingLevel;

public class ObjectManager {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage spike_Trap_Img, cannonBallImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike_Trap> spikeTraps;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectile = new ArrayList<>();

    public ObjectManager(Playing playing){
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikeTrapTouched(Player p){
        for(Spike_Trap st: spikeTraps){
            if(st.getHitbox().intersects(p.getHitbox())){
                p.kill();
            }
        }

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
            if(gc.isActive() && !gc.doAnimation){
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
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikeTraps = newLevel.getSpikeTraps();
        cannons = newLevel.getCannons();
        projectile.clear();
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

        spike_Trap_Img = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

        for (int i = 0; i < cannonImgs.length ; i++) {
            cannonImgs[i] = temp.getSubimage(i*40, 0, 40, 26);
        }

        cannonBallImgs = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);


    }

    public void update(int[][] lvData, Player player){
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

        updateCannon(lvData, player);

        updateProjectile(lvData, player);
    }

    private void updateProjectile(int[][] lvData, Player player) {
        for(Projectile p: projectile){
            if(p.isActive()){
                p.updatePosition();
                if(p.getHitbox().intersects(player.getHitbox())){
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if (isProjectileHittingLevel(p, lvData)) {
                    p.setActive(false);
                }
            }
        }
    }

    private void updateCannon(int[][] lvData, Player player) {
        for(Cannon c: cannons){
            if(!c.doAnimation) {
                if (c.getTileY() == player.getTileY()) {
                    if(isPlayerInRange(c, player)){
                        if(isPlayerInFrontOfCannon(c,player)){
                            if(canCannonSeePlayer(lvData, player.getHitbox(), c.getHitbox(), c.getTileY())){
                                c.setAnimation(true);
                            }
                        }
                    }
                }
            }
            c.update();
            if(c.getAniIndex() == 4 && c.getAniTick() == 0){
                shootCannon(c);
            }
        }
    }

    private void shootCannon(Cannon c) {
        int dir = 1;
        if(c.getObjectType() == CANNON_LEFT){
            dir = -1;
        }
        projectile.add(new Projectile((int)(c.getHitbox().x), (int) (c.getHitbox().y), dir )) ;
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if(c.getObjectType() == CANNON_LEFT){
            if(c.getHitbox().x > player.getHitbox().x){
                return true;
            }
        }
        else if(c.getHitbox().x < player.getHitbox().x){
            return true;
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) (Math.abs(player.getHitbox().x - c.getHitbox().x));
        return absValue <= Game.TILES_SIZE * 7;
    }

    public void draw(Graphics g, int xLvOffset){
        drawPotions(g, xLvOffset);
        drawContainer(g, xLvOffset);
        drawSpikeTraps(g,xLvOffset);
        drawCannon(g,xLvOffset);
        drawProjectile(g, xLvOffset);
    }

    private void drawProjectile(Graphics g, int xLvOffset) {
        for(Projectile p : projectile){
            if(p.isActive()){
                g.drawImage(cannonBallImgs, (int) (p.getHitbox().x - xLvOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
            }
        }
    }

    private void drawCannon(Graphics g, int xLvOffset) {
        for(Cannon c: cannons){
            int x = (int)(c.getHitbox().x - xLvOffset);
            int width = CANNON_WIDTH;
            if(c.getObjectType() == CANNON_RIGHT){
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int)(c.getHitbox().y), width, CANNON_HEIGHT, null );
        }
    }

    private void drawSpikeTraps(Graphics g, int xLvOffset) {
        for(Spike_Trap st : spikeTraps){
            g.drawImage(spike_Trap_Img, (int) (st.getHitbox().x - xLvOffset),
                    (int) (st.getHitbox().y - st.getyDrawOffset()),
                    SPIKE_TRAP_WIDTH,
                    SPIKE_TRAP_HEIGHT,
                    null );
        }
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
        loadObject(playing.getLevelManager().getCurrentLevel());
        for(Potion p: potions){
            p.reset();
        }

        for(GameContainer gc: containers){
            gc.reset();
        }

        for(Cannon c : cannons){
            c.reset();
        }

    }

}
