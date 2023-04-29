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
    private SoundButtons musicButton, sfxButton;
    private URMButton menuB, replayB, unpauseB;
    private Playing playing;
    private VolumeButtom volumeButtom;

    public PauseOverlay(Playing playing){
        this.playing = playing;
        loadBackground();
        createSoundButton();
        createURMButtons();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        int volumeX = (int) ( 309 * Game.SCALE);
        int volumeY = (int) ( 278 * Game.SCALE);

        volumeButtom = new VolumeButtom(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT );
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

    private void createSoundButton() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);

        musicButton = new SoundButtons(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButtons(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);

    }

    public void update(){
        //sound
        musicButton.update();
        sfxButton.update();

        //urm
        menuB.update();
        replayB.update();
        unpauseB.update();

        //volume
        volumeButtom.update();
    }

    public void draw(Graphics g){
        //draw BackGround
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        //draw SoundButtons
        musicButton.draw(g);
        sfxButton.draw(g);

        //draw UrmButtons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        //volume
        volumeButtom.draw(g);
    }

    public boolean isInside(MouseEvent e, PauseButton b){
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void mouseDragged(MouseEvent e){
        if(volumeButtom.isMousePressed()){
            volumeButtom.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        if(isInside(e, musicButton)){
            musicButton.setMousePressed(true);
        }
        else if(isInside(e, sfxButton)){
            sfxButton.setMousePressed(true);
        }
        //urm 
        else if (isInside(e, menuB)) {
            menuB.setMousePressed(true);
        }
        else if (isInside(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        }
        else if (isInside(e, replayB)) {
            replayB.setMousePressed(true);
        }
        else if (isInside(e, volumeButtom)) {
            volumeButtom.setMousePressed(true);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if(isInside(e, musicButton)){
            if(musicButton.isMousePressed()){
                musicButton.setMuted(!musicButton.isMuted());
            }
        }
        else if(isInside(e, sfxButton)){
            if(sfxButton.isMousePressed()){
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }
        else if(isInside(e, menuB)){
            if(menuB.isMousePressed()){
                GameStates.state = GameStates.MENU;
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

        //sound
        musicButton.resetBools();
        sfxButton.resetBools();

        //urm
        menuB.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();

        //volume
        volumeButtom.resetBools();

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        //urm
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        //volume
        volumeButtom.setMouseOver(false);

        if(isInside(e, musicButton)){
            musicButton.setMouseOver(true);
        }
        else if(isInside(e, sfxButton)){
            sfxButton.setMouseOver(true);
        }
        else if(isInside(e, menuB)){
            menuB.setMouseOver(true);
        }
        else if(isInside(e, replayB)){
            replayB.setMouseOver(true);
        }
        else if(isInside(e, unpauseB)){
            unpauseB.setMouseOver(true);
        }
        else if(isInside(e, volumeButtom)){
            volumeButtom.setMouseOver(true);
        }
    }
}
