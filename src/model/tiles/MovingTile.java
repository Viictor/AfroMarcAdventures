package model.tiles;

import model.physics.MovingBoundingRect;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Author: Victor
 * Date: 17/09/13
 */
public class MovingTile extends Tile {

    private Image img;
    protected float xF;
    protected float yF;

    public MovingTile(int x, int y) {
        super(x, y);
        this.xF = x;
        this.yF = y;
        try {
            img = new Image("resources/levels/moving.png");
        } catch (SlickException e) {
            e.printStackTrace();
        }
        boundingShape = new MovingBoundingRect(x*32,y*32,32,32);
    }

    public Image getImg() {
        return img;
    }

    public float getXF() {
        return this.xF;
    }

    public float getYF() {
        return this.yF;
    }

    @Override
    public void moveX(float x) {
        this.xF += x;
        this.x = Math.round(this.xF);
    }

    @Override
    public void moveY(float y) {
        this.yF += y;
        this.y = Math.round(this.yF);
    }
}
