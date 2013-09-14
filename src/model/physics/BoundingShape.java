
package model.physics;

import model.tiles.Tile;
import java.util.ArrayList;

/**
 *
 * @author Victor
 */
public abstract class BoundingShape {
    
    public boolean checkCollision(BoundingShape bv) {
        if (bv instanceof AABoundingRect) {
            return checkCollision((AABoundingRect)bv);
        } else if (bv instanceof DeadlyBoundingRect) {
            return checkCollision((DeadlyBoundingRect)bv);
        }
        return false;
    }
    
    public abstract boolean checkCollision(AABoundingRect box);
    
    public abstract void updatePosition(float newX, float newY);
    
    public abstract void movePosition(float x, float y);
    
    public abstract ArrayList<Tile> getTilesOccupying(Tile[][] tiles);
    
    public abstract ArrayList<Tile> getGroundTiles(Tile[][] tiles);
    
    public abstract int offTheMargins();
}
