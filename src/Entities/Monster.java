package Entities;

import Main.Game;

import java.awt.geom.Rectangle2D;

import static Utilize.Constants.Directions.*;
import static Utilize.Constants.EnemyConstant.*;

public class Monster extends Enemy {
    //Attack
    private int attackBoxOffsetX;

    public Monster(float x, float y) {
        super(x, y, MONSTER_WIDTH, MONSTER_HEIGHT, MONSTER);
        initHitbox(22, 19);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }

    public void update(int[][] lvData, Player player){
        updateBehavior(lvData, player);
        updateAniTick();
        updateAttackBox();
    }

    private void updateAttackBox(){
        attackBox.x = hitbox.x - attackBoxOffsetX ;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvData, Player player){
        if(firstUpdate){
            firstUpdateCheck(lvData);
        }

        if (inAir){
            updateInAir(lvData);
        }
        else {
            switch (state){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(lvData, player)){
                        turnTowardsPlayer(player);
                        if(isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }

                    move(lvData);
                    break;
                case ATTACK:
                    if(aniIndex == 0){
                        attackChecked = false;
                    }

                    if(aniIndex == 3 && !attackChecked)
                        checkPlayerHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }



    public int flipX(){
        if(walkDirection == RIGHT){
            return width;
        }
        else {
            return 0;
        }
    }

    public int flipW(){
        if(walkDirection == RIGHT){
            return -1;
        }
        else {
            return 1;
        }
    }
}
