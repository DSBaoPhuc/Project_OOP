package UI;

import GameStates.*;
import Main.Game;
import Utilize.Constants;
import Utilize.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utilize.Constants.UI.PauseButtons.*;
import static Utilize.Constants.UI.URMButtons.*;
import static Utilize.Constants.UI.VolumeButtons.*;

public class PauseOverlay {
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private URMButton menuB, replayB, unpauseB;
    private Playing playing;
    private AudioOptions audioOptions;


    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBackground();
        createURMButtons();
        audioOptions = playing.getGame().getAudioOptions();
    }


    private void createURMButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (461 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        unpauseB = new URMButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
        replayB = new URMButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        menuB = new URMButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
    }



    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);

    }

    public void update(){
        //urm
        menuB.update();
        replayB.update();
        unpauseB.update();

        //audio
        audioOptions.update();

    }

    public void draw(Graphics g){
        //draw BackGround
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        //draw URM_Buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        //draw Audio_Buttons
        audioOptions.draw(g);
    }

    public boolean isInside(MouseEvent e, PauseButton b){
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isInside(e, menuB)) {
            menuB.setMousePressed(true);
        }
        else if (isInside(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        }
        else if (isInside(e, replayB)) {
            replayB.setMousePressed(true);
        }
        else {
            audioOptions.mousePressed(e);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if(isInside(e, menuB)){
            if(menuB.isMousePressed()){
                playing.resetAll();
                playing.setGameState(GameStates.MENU);
                playing.unpauseGame();
            }
        }
        else if(isInside(e, replayB)){
            if(replayB.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }
        }
        else if(isInside(e, unpauseB)){
            if(unpauseB.isMousePressed()){
                playing.unpauseGame();
            }
        }
        else {
            audioOptions.mouseReleased(e);
        }

        //urm
        menuB.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        //urm
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);

        if(isInside(e, menuB)){
            menuB.setMouseOver(true);
        }
        else if(isInside(e, replayB)){
            replayB.setMouseOver(true);
        }
        else if(isInside(e, unpauseB)){
            unpauseB.setMouseOver(true);
        }
        else {
            audioOptions.mouseMoved(e);
        }
    }
}
