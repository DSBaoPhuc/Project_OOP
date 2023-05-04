package UI;

import GameStates.GameStates;
import GameStates.Playing;
import Main.Game;
import Utilize.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utilize.Constants.UI.URMButtons.URM_SIZE;

public class GameOverOverlay {
    private Playing playing;
    private BufferedImage img;
    private int imgX, imgY, imgWidth, imgHeight;
    private URMButton menu, play;


    public GameOverOverlay(Playing playing){
        this.playing = playing;
        createImg();
        createButton();
    }

    public void createButton(){
        int menuX = (int) (335 * Game.SCALE);
        int playX = (int) (440 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        play = new URMButton(playX, y, URM_SIZE, URM_SIZE, 0);
        menu = new URMButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        imgWidth = (int) (img.getWidth() * Game.SCALE);
        imgHeight = (int) (img.getHeight() * Game.SCALE);
        imgX = Game.GAME_WIDTH / 2 - imgWidth / 2;
        imgY = (int) (100 * Game.SCALE);
    }

    public void draw(Graphics g){
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, imgX, imgY, imgWidth, imgHeight, null);
        menu.draw(g);
        play.draw(g);

//        g.setColor(Color.white);
//        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
//        g.drawString("Press ESC to enter Main Menu!", Game.GAME_WIDTH / 2, 300);
    }

    public void update(){
        menu.update();
        play.update();
    }

    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameStates.state = GameStates.MENU;
        }
    }

    private boolean isIn(URMButton b, MouseEvent e){
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e){
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(menu, e)){
            menu.setMouseOver(true);
        }
        else if(isIn(play, e)) {
            play.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAll();
                GameStates.state = GameStates.MENU;
            }
        }
        else if (isIn(play, e)) {
            if (play.isMousePressed()) {
                playing.resetAll();
            }
        }

        menu.resetBools();
        play.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(menu, e)){
            menu.setMousePressed(true);
        }
        else if (isIn(play, e)) {
            play.setMousePressed(true);
        }
    }
}
