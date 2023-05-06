package GameStates;

import Audio.AudioPlayer;
import Main.Game;
import UI.MenuButton;

import java.awt.event.MouseEvent;

public class State {
    protected Game game;

    public boolean isInside(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public State(Game game){
        this.game = game;
    }

    public Game getGame(){
        return game;
    }

    public void setGameState(GameStates state){
        switch (state){
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLvSong(game.getPlaying().getLevelManager().getLvIndex());
        }

        GameStates.state = state;
    }
}
