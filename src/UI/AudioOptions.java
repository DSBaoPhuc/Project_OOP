package UI;

import GameStates.GameStates;
import Main.Game;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import static Utilize.Constants.UI.PauseButtons.SOUND_SIZE;
import static Utilize.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static Utilize.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

public class AudioOptions {
    private Game game;

    private SoundButtons musicButton, sfxButton;

    private VolumeButtom volumeButtom;


    public AudioOptions(Game game){
        this.game = game;
        createSoundButton();
        createVolumeButtons();
    }

    private void createVolumeButtons() {
        int volumeX = (int) ( 309 * Game.SCALE);
        int volumeY = (int) ( 278 * Game.SCALE);

        volumeButtom = new VolumeButtom(volumeX, volumeY, SLIDER_WIDTH, VOLUME_HEIGHT );
    }

    private void createSoundButton() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);

        musicButton = new SoundButtons(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButtons(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    public void update(){
        //sound
        musicButton.update();
        sfxButton.update();

        //volume
        volumeButtom.update();
    }

    public void draw(Graphics g){
        //draw SoundButtons
        musicButton.draw(g);
        sfxButton.draw(g);

        //volume
        volumeButtom.draw(g);
    }

    public boolean isInside(MouseEvent e, PauseButton b){
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void mouseDragged(MouseEvent e){
        if(volumeButtom.isMousePressed()){
            float valueBefore = volumeButtom.getFloatValue();
            volumeButtom.changeX(e.getX());
            float valueAfter = volumeButtom.getFloatValue();
            if(valueBefore != valueAfter){
                game.getAudioPlayer().setVolume(valueAfter);
            }
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
        else if (isInside(e, volumeButtom)) {
            volumeButtom.setMousePressed(true);
        }

    }

    public void mouseReleased(MouseEvent e) {
        if(isInside(e, musicButton)){
            if(musicButton.isMousePressed()){
                musicButton.setMuted(!musicButton.isMuted());
                game.getAudioPlayer().toggleSongMute();
            }
        }
        else if(isInside(e, sfxButton)){
            if(sfxButton.isMousePressed()){
                sfxButton.setMuted(!sfxButton.isMuted());
                game.getAudioPlayer().toggleEffectMute();
            }
        }
        //sound
        musicButton.resetBools();
        sfxButton.resetBools();
        //volume
        volumeButtom.resetBools();

    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        //volume
        volumeButtom.setMouseOver(false);

        if(isInside(e, musicButton)){
            musicButton.setMouseOver(true);
        }
        else if(isInside(e, sfxButton)){
            sfxButton.setMouseOver(true);
        }
        else if(isInside(e, volumeButtom)){
            volumeButtom.setMouseOver(true);
        }
    }

}
