
package model.physics;

import model.tiles.MovingTile;
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
            return checkCollision(bv);
        }
        return false;
    }
    
    protected abstract boolean checkCollision(AABoundingRect box);
    
    public abstract void updatePosition(float newX, float newY);
    
    public abstract void movePosition(float x, float y);
    
    public abstract ArrayList<Tile> getTilesOccupying(Tile[][] tiles, ArrayList<MovingTile> movTiles);
    
    public abstract ArrayList<Tile> getGroundTiles(Tile[][] tiles, ArrayList<MovingTile> movTiles);
    
    public abstract int offTheMargins();
}
