package Objects;

import Main.Game;

public class Spike_Trap extends GameObjects{
    public Spike_Trap(int x, int y, int objectType){
        super(x, y, objectType);

        initHitbox(32,16);
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16);
        hitbox.y += yDrawOffset;
    }
}
