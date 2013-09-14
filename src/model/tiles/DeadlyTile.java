
package model.tiles;

import model.physics.DeadlyBoundingRect;

/**
 *
 * @author Victor
 */
public class DeadlyTile extends Tile {

    public DeadlyTile(int x, int y) {
        super(x, y);
        boundingShape = new DeadlyBoundingRect(x*32,y*32,32,32);
    }
}
