package Entities;

import GameStates.Playing;
import Main.Game;
import Utilize.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Utilize.Constants.Player_Constants.*;
import static Utilize.HelpMethods.*;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean left,right,up,down,jump;
    private boolean moving = false, attacking = false;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] levelData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    // jumping / gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //status bar
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    //Attack
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimation();
        initHitbox(x,y,(int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) 20 * Game.SCALE, (int) 20 * Game.SCALE);
    }


    public void update(){
        updateHealthBar();
        if(currentHealth <= 0){
            playing.setGameOver(true);
            return;
        }


        updateAttackBox();

        updatePosition();
        if(attacking){
            checkAttack();
        }
        updateAniTick();
        setAnimation();

    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1){
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) (maxHealth)) * healthBarWidth);
    }

    public void render(Graphics g, int lvOffset){
        g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvOffset + flipX,(int) (hitbox.y - yDrawOffset), width * flipW, height, null);
        //drawHitbox(g, lvOffset);
//        drawAttackBox(g, lvOffset);
        drawUI(g);
    }

    private void drawAttackBox(Graphics g, int lvOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int) attackBox.x - lvOffsetX, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAniTick() {
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }

    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving){
            playerAction = RUNNING;
        }
        else {
            playerAction = IDLE;
        }
        if(inAir){
            if(airSpeed < 0){
                playerAction = JUMP;
            }
            else {
                playerAction = FALLING;
            }
        }

        if (attacking){
            playerAction = ATTACK;
            if(startAni != ATTACK){
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }

        if(startAni != playerAction){
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePosition(){
        moving = false;

        if(jump){
            jump();
        }

        if(!inAir){
            if((!left && !right) || (left && right)){
                return;
            }
        }

        float xSpeed = 0;

        if(left){
            xSpeed -= playerSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(!inAir){
            if(!IsEntityOnFloor(hitbox, levelData)){
                inAir = true;

            }
        }

        if(inAir){
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            }
            else {
                hitbox.y = GetEntityXPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0){
                    resetInAir();
                }
                else {
                    airSpeed = fallSpeedAfterCollision;
                    updateXPos(xSpeed);
                }
            }
        }
        else {
            updateXPos(xSpeed);
        }
        moving = true;
    }

    private void jump() {
        if(inAir){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)){
            hitbox.x += xSpeed;
        }
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value){
        currentHealth += value;

        if(currentHealth <= 0){
            currentHealth = 0;
        } else if (currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }
    }

    private void loadAnimation() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLevelData(int[][] levelData){
        this.levelData = levelData;
        if(!IsEntityOnFloor(hitbox, levelData)){
            inAir = true;
        }
    }
    public void resetDirBoolean() {
        left =false;
        right = false;
        up = false;
        down = false;
    }

    public void resetAll(){
        resetDirBoolean();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(!IsEntityOnFloor(hitbox, levelData)){
            inAir = true;
        }
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public boolean getLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean getRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean getUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean getDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
    public void setJump(boolean jump){
        this.jump = jump;
    }

}
