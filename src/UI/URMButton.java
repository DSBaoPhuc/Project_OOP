package UI;

import Utilize.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static Utilize.Constants.UI.URMButtons.*;

public class URMButton extends PauseButton{
    private BufferedImage[] UrmImgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public URMButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadUrmImgs();
    }

    private void loadUrmImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        UrmImgs = new BufferedImage[3];
        for (int i = 0; i < UrmImgs.length ; i++) {
            UrmImgs[i] = temp.getSubimage(i * URM_SIZE_DEFAULT, rowIndex * URM_SIZE_DEFAULT, URM_SIZE_DEFAULT, URM_SIZE_DEFAULT );
            
        }
    }

    public void update(){
        index = 0;
        if(mouseOver){
            index = 1;
        }
        if (mousePressed){
            index = 2;
        }
    }

    public void draw(Graphics g){
        g.drawImage(UrmImgs[index], x, y, URM_SIZE, URM_SIZE, null );
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
