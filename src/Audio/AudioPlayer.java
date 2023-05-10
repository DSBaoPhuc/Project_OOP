package Audio;

import java.util.Random;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_1 = 4;
    public static int ATTACK_2 = 5;
    public static int ATTACK_3 = 6;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer(){
        loadSong();
        loadEffect();
        playSong(MENU_1);
    }

    public void setVolume(float volume){
        this.volume = volume;
        updateSongVolume();
        updateEffectVolume();
    }

    public void stopSong(){
        if(songs[currentSongId].isActive()){
            songs[currentSongId].stop();
        }
    }

    public void setLvSong(int lvIndex){
        if(lvIndex % 2 == 0){
            playSong(LEVEL_1);
        }
        else {
            playSong(LEVEL_2);
        }
    }

    public void lvCompleted(){
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    private Clip getCLip(String name){
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);

            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){

            e.printStackTrace();
        }

        return null;
    }

    private void loadSong(){
        String[] songNames = {"menu", "level1", "level2"};
        songs = new Clip[songNames.length];
        for (int i = 0; i < songs.length ; i++) {
            songs[i] = getCLip(songNames[i]);
        }

    }

    public void toggleSongMute(){
        this.songMute = !songMute;
        for(Clip c : songs){
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void playSong(int song){
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void loadEffect(){
        String[] effectNames = {"die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3"};
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effectNames.length ; i++) {
            effects[i] = getCLip(effectNames[i]);
        }

        updateEffectVolume();
    }

    public void toggleEffectMute(){
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(ATTACK_1);
    }

    public void playEffect(int effect){
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playAtkSound(){
        int start = 4;
        start += rand.nextInt(3);
        playEffect(start);
    }

    private void updateSongVolume(){
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);

    }

    private void updateEffectVolume(){
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

}
