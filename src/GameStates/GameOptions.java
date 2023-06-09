package GameStates;

import Main.Game;
import UI.AudioOptions;
import UI.PauseButton;
import UI.URMButton;
import Utilize.LoadSave;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static Utilize.Constants.UI.URMButtons.*;

public class GameOptions extends State implements StateMethods{
    private AudioOptions audioOptions;
    private BufferedImage bgImg, optionsBgImg;
    private int bgX, bgY, bgW, bgH;
    private URMButton menuB;

    public GameOptions(Game game){
        super(game);
        loadImgs();
        loadButtonn();
        audioOptions = game.getAudioOptions();
    }

    private void loadButtonn() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE);

        menuB = new URMButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadImgs() {
        bgImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        optionsBgImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

        bgW = (int) (optionsBgImg.getWidth() * Game.SCALE);
        bgH = (int) (optionsBgImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (33 * Game.SCALE);
    }

    @Override
    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBgImg, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        audioOptions.draw(g);
    }

    public boolean isInside(MouseEvent e, PauseButton b){
        return (b.getBounds().contains(e.getX(), e.getY()));
    }

    public void mouseDragged(MouseEvent e){
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isInside(e, menuB)){
            menuB.setMousePressed(true);
        }
        else {
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isInside(e, menuB)){
            if(menuB.isMousePressed()) {
                GameStates.state = GameStates.MENU;
            }
        }
        else {
            audioOptions.mouseReleased(e);
        }

        menuB.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        if(isInside(e, menuB)){
            menuB.setMouseOver(true);
        }
        else {
            audioOptions.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            GameStates.state = GameStates.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
