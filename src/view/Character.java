
package view;

import model.LevelObject;
import java.util.EnumMap;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 *
 * @author Victor
 */
public abstract class Character extends LevelObject{
    
    public enum Facing {LEFT, RIGHT};

    protected Player.Facing facing = Player.Facing.RIGHT;
    protected EnumMap<Player.Facing, Image> sprites;
    protected EnumMap<Player.Facing, Animation> movingAnim;
    protected EnumMap<Player.Facing, Animation> jumpingAnim;
    
    protected boolean                   moving = false;
    protected float                     accelerationSpeed = 1;
    protected float                     decelerationSpeed = 1;
    protected float                     maximumSpeed = 1;
    
    public Character(float x, float y) {
        super(x, y);
    }

    public void render(float offset_x, float offset_y, GameContainer gc, Graphics g) {
        if (jumpingAnim != null && y_velocity != 0) {
            jumpingAnim.get(facing).draw(x-2-offset_x, y-2-offset_y);
        } else if (movingAnim != null && moving) {
            movingAnim.get(facing).draw(x-2-offset_x, y-2-offset_y);
        } else {
            sprites.get(facing).draw(x-2-offset_x, y-2-offset_y);
        }
    }
    
    public void jump(){
        if(onGround) {
            y_velocity = -0.4f;
        }
    }
    
    public void setFrame(int i, boolean high){
        
    }
    
    public void moveLeft(int delta) {
        if(x_velocity > -maximumSpeed){
            x_velocity -= accelerationSpeed*delta;
            if(x_velocity < -maximumSpeed){
                x_velocity = -maximumSpeed;
            }
        }
        moving = true;
        facing = Facing.LEFT;
    }
    
    public void moveRight(int delta) {
         if(x_velocity < maximumSpeed){
            x_velocity += accelerationSpeed*delta;
            if(x_velocity > maximumSpeed){
                x_velocity = maximumSpeed;
            }
        }
        moving = true;
        facing = Facing.RIGHT;
    }
    
    public void decelerate(int delta) {
        if(x_velocity > 0){
            x_velocity -= decelerationSpeed * delta;
            if(x_velocity < 0)
                x_velocity = 0;
        }else if(x_velocity < 0){
            x_velocity += decelerationSpeed * delta;
            if(x_velocity > 0)
                x_velocity = 0;
        }
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }
}
