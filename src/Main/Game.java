package Main;


import GameStates.GameStates;
import java.awt.Graphics;

import GameStates.Menu;
import GameStates.Playing;
import Utilize.LoadSave;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;

    public static final int TILES_DEFAULT_SIZE = 32;
    //Scale = How much should we scale everything(player, enemies, level)
    public static final float SCALE = 1.5f;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


    public Game(){
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(){

        switch (GameStates.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:

            case QUIT:

            default:
                System.exit(0);
                break;
        }

    }

    public void render(Graphics g){
        switch (GameStates.state){
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;

            default:
                break;
        }


    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;

        long lastcheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;


        while (true){
            long current_time = System.nanoTime();

            deltaU += (current_time - previousTime) / timePerUpdate;
            deltaF += (current_time - previousTime) / timePerFrame;
            previousTime = current_time;

            if(deltaU >= 1){
                update();
                updates++ ;
                deltaU-- ;
            }

            if(deltaF >= 1){
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if(System.currentTimeMillis() - lastcheck >= 1000){
                lastcheck = System.currentTimeMillis();
                System.out.println("FPS: "+ frames + "| UPS: " + updates );
                frames = 0;
                updates = 0;
            }
        }
    }
    public void windowFocusLost() {
        if(GameStates.state == GameStates.PLAYING){
            playing.getPlayer().resetDirBoolean();
        }
    }

    public Menu getMenu(){
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }
}
