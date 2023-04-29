package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Utilize.Constants.EnemyConstant.*;
import static Utilize.HelpMethods.*;
import static Utilize.Constants.Directions.*;

public abstract class Enemy extends Entity{
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir ;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.5f * Game.SCALE;
    protected float walkDirection = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected int maxHealth;
    protected int currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] lvData){
        if(!IsEntityOnFloor(hitbox,lvData)){
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvData){
        if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvData)){
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        }
        else {
            inAir = false;
            hitbox.y = GetEntityXPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    protected void move(int[][] lvData){
        float xSpeed = 0;
        if(walkDirection == LEFT){
            xSpeed = -walkSpeed;
        }
        else {
            xSpeed = walkSpeed;
        }

        if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvData)){
            if(IsFloor(hitbox, xSpeed, lvData)){
                hitbox.x += xSpeed;
                return;
            }
        }

        changWalkDirection();
    }

    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x){
            walkDirection = RIGHT;
        }
        else {
            walkDirection = LEFT;
        }
    }
    protected boolean canSeePlayer(int[][] lvData, Player player){
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY){
            if(isPlayerInRange(player)){
                if(isSightClear(lvData, hitbox, player.hitbox, tileY)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPlayerInRange(Player player) {
        int absValue = (int) (Math.abs(player.hitbox.x - hitbox.x));
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int) (Math.abs(player.hitbox.x - hitbox.x));
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState){
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0){
            newState(DEAD);
        }
        else {
            newState(HIT);
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player){
        if(attackBox.intersects(player.hitbox)){
            player.changeHealth(-getEnemyDamage(enemyType));
            attackChecked = true;
        }
    }

    protected void updateAniTick(){
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex ++;
            if(aniIndex >= getSpriteAmount(enemyType,  enemyState)){
                aniIndex = 0;

                switch (enemyState){
                    case ATTACK, HIT -> enemyState = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    protected void changWalkDirection() {
        if (walkDirection == LEFT){
            walkDirection = RIGHT;
        }
        else {
            walkDirection = LEFT;
        }
    }

    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        fallSpeed = 0;
    }

    public int getAniIndex(){
        return aniIndex;
    }

    public int getEnemyState(){
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }
}
