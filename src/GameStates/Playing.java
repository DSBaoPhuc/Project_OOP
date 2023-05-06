package GameStates;

import Entities.EnemyManager;
import Entities.Player;
import Levels.LevelManager;

import Main.Game;
import Objects.ObjectManager;
import UI.GameOverOverlay;
import UI.LevelCompletedOverlay;
import UI.PauseOverlay;
import Utilize.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;

    private int xLvOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvOffsetX;

    private boolean gameOver;
    private boolean lvCompleted;
    private BufferedImage backgroundImg;

    private boolean playerDying;



    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);

        calculateLevelOffset();
        loadStartLevel();
    }


    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager((this));

        player = new Player(200,200, (int)(64 * Game.SCALE), (int)(40 * Game.SCALE), this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if(paused){
            pauseOverlay.update();
        }
        else if (lvCompleted) {
            levelCompletedOverlay.update();
        }
        else if (gameOver) {
            gameOverOverlay.update();

        }
        else if (playerDying) {
            player.update();

        } else{
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseBorder();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        levelManager.draw(g, xLvOffset);
        player.render(g, xLvOffset);
        enemyManager.draw(g, xLvOffset);
        objectManager.draw(g, xLvOffset);

        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } 
        else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (lvCompleted) {
            levelCompletedOverlay.draw(g);
        }
    }

    public void loadNextLevel(){
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObject(levelManager.getCurrentLevel());
    }

    private void calculateLevelOffset() {
        maxLvOffsetX = levelManager.getCurrentLevel().getLvOffset();
    }

    private void checkCloseBorder(){
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvOffset;

        if(diff > rightBorder){
            xLvOffset += diff - rightBorder;
        }
        else if(diff < leftBorder){
            xLvOffset += diff - leftBorder;
        }

        if(xLvOffset > maxLvOffsetX ){
            xLvOffset = maxLvOffsetX;
        }
        else if(xLvOffset < 0){
            xLvOffset = 0;
        }
    }

    public void resetAll() {
        gameOver = false;
        paused = false;

        lvCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();

        objectManager.resetAllObj();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjTouchPlayer(hitbox);
    }

    public void checkSpikeTrapTouched(Player p) {
        objectManager.checkSpikeTrapTouched(p);
    }

    public void checkObjHit(Rectangle2D.Float attackBox){
        objectManager.checkObjHit(attackBox);
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver) {
            if (paused) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
            }
            else if (lvCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        }
        else{
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            }
            else if (lvCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        }
        else{
            gameOverOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            }
            else if (lvCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }
        }
        else{
            gameOverOverlay.mouseMoved(e);
        }
    }

    public void unpauseGame(){
        paused = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
//            case KeyEvent.VK_SPACE:
//                player.setJump(true);
//                break;
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
//            case KeyEvent.VK_SPACE:
//                player.setJump(false);
//                break;
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;
            }
        }
    }

    public void setLevelCompleted (boolean levelCompleted) {
        this.lvCompleted = levelCompleted;
        if(lvCompleted){
            game.getAudioPlayer().lvCompleted();
        }
    }
    public void setMaxLvOffset(int lvOffset){
        this.maxLvOffsetX = lvOffset;
    }

    public Player getPlayer(){
        return player;
    }

    public void windowFocusLost() {
        player.resetDirBoolean();
    }

    public EnemyManager getEnemyManager(){
        return enemyManager;
    }

    public ObjectManager getObjectManager(){
        return  objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void setPlayerDying(boolean b){
        this.playerDying = playerDying;
    }

}


