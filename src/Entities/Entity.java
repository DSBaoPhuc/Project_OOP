package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x,y;
    protected int width,height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed = 1.0f * Game.SCALE;


    public Entity (float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    protected void drawAttack(Graphics g, int xlvOffset){
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - xlvOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height );
    }

    protected void drawHitbox(Graphics g, int xLvOffset){
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvOffset,(int) hitbox.y,(int) hitbox.width,(int) hitbox.height);
    }

    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE),(int) (height * Game.SCALE));
    }

    public int getState(){
        return state;
    }


    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getAniIndex(){
        return aniIndex;
    }

}
