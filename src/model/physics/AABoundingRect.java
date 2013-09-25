
package model.physics;

import model.tiles.MovingTile;
import model.tiles.Tile;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 *
 * @author Victor
 */
public class AABoundingRect extends BoundingShape{
    
    public float x;
    public float y;
    public float width;
    public float height;

    public AABoundingRect(float x, float y, float width, float height) {
        this.x = x + width/2;
        this.y = y + height/2;
        this.width = width;
        this.height = height;
    }

    public void render(Graphics g, float offset_x, float offset_y) {
        g.setColor(Color.blue);
        g.drawRect(x-width/2-offset_x, y-offset_y, width, height);
    }

    @Override
    public boolean checkCollision(AABoundingRect box) {

        return !(box.x-box.width/2 > this.x+width/2 || box.x+box.width/2 < this.x-width/2 ||
                 box.y-box.height/2 > this.y+height/2 || box.y+box.height/2 < this.y-height/2);
    }

    @Override
    public void updatePosition(float newX, float newY) {
        this.x = newX + width/2;
        this.y = newY + height/2;
    }

    @Override
    public void movePosition(float x, float y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public ArrayList<Tile> getTilesOccupying(Tile[][] tiles, ArrayList<MovingTile> movTiles) {
        ArrayList<Tile> occupiedTiles = new ArrayList<>();
        boolean movingAdded = false;

        for(int i = (int) x; i <= x+width/2+(32-(width/2)%32); i+=32){
            for(int j = (int) y; j < y+(height/2)+(32-(height/2)%32); j+=32){
                if (i/32 > 79 || j/32 > 49 || i/32 < 0 || j/32 < 0) {
                    return null;
                } else {

                    for (MovingTile mTile : movTiles) {
                        if (Math.round(mTile.getXF()) == i/32 && Math.round(mTile.getYF()) == j/32) {
                            occupiedTiles.add(mTile);
                            movingAdded = true;
                        }
                    }

                    if (!movingAdded) {
                        occupiedTiles.add(tiles[i/32][j/32]);
                    }
                    movingAdded = false;
                }
            }
        }
        return occupiedTiles;
    }
    
    /**
     * Looks if the object is off the margins
     * @return -1 if its off the lowest margin, 1 if its off any of the other margins, 0 if it's not.
     */
    @Override
    public int offTheMargins() {
        for(int i = (int) x; i <= x+width/2+(32-(width/2)%32); i+=32){
            for(int j = (int) y; j <= Math.round(y+(height/2)+(32-(height/2)%32)); j+=32){
                if(j/32 > 49) {
                    return -1;
                } else if (i/32 > 79 || i/32 < 0 || j/32 < 0) {
                    return 1;
                } 
            }
        }
        return 0;
    }

    @Override
    public ArrayList<Tile> getGroundTiles(Tile[][] tiles, ArrayList<MovingTile> movTiles) {
        ArrayList<Tile> tilesUnderneath = new ArrayList<>();
        boolean movingAdded = false;
        int j = (int) (y+height/2+1);

        for(int i = (int) x; i <= x+width/2+(32-(width/2)%32); i+=32){

            for (MovingTile mTile : movTiles) {
                if (Math.round(mTile.getXF()) == i/32 && Math.round(mTile.getYF()) == j/32) {
                    tilesUnderneath.add(mTile);
                    movingAdded = true;
                }
            }

            if (!movingAdded) {
                tilesUnderneath.add(tiles[i/32][j/32]);
            }
            movingAdded = false;
        }

        return tilesUnderneath;
    }
    
}
