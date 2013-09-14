
package model.tiles;

import model.physics.AABoundingRect;

/**
 *
 * @author Victor
 */
public class SolidTile extends Tile {

    public SolidTile(int x, int y) {
        super(x, y);
        boundingShape = new AABoundingRect(x*32,y*32,32,32);
    }
}
