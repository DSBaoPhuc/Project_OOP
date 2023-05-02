package Objects;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Utilize.Constants.ANI_SPEED;
import static Utilize.Constants.ObjectConstants.*;

public class GameObjects {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObjects(int x, int y, int objectType){
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void updateAniTick(){
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex ++;
            if(aniIndex >= GetSpriteAmount(objectType)){
                aniIndex = 0;
                if(objectType == BARREL || objectType == BOX){
                    doAnimation = false;
                    active = false;
                }
                else if(objectType == CANNON_LEFT || objectType == CANNON_RIGHT){
                    doAnimation = false;
                }
            }
        }
    }

    public void reset(){
        aniIndex = 0;
        aniTick = 0;
        active = true;

        if(objectType == BARREL || objectType == BOX || objectType == CANNON_LEFT || objectType == CANNON_RIGHT){
            doAnimation = false;
        }
        else {
            doAnimation = true;
        }
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE),(int) (height * Game.SCALE));
    }

    public void drawHitbox(Graphics g, int xLvOffset){
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvOffset,(int) hitbox.y,(int) hitbox.width,(int) hitbox.height);
    }

    public int getObjectType() {
        return objectType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }

    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex(){
        return aniIndex;
    }
}
