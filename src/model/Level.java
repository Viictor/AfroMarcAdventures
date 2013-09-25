package model;

import model.tiles.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;
import view.Character;
import view.Game;
import view.Player;

import java.util.ArrayList;

/**
 * Author: Victor
 * Date: 15/09/13
 */
public abstract class Level {

    protected TiledMap map;
    protected Tile[][] tiles;
    protected ArrayList<MovingTile> movingTiles;
    protected ArrayList<Character> characters;
    protected Player player;
    protected Image background;
    protected float stepsMoved = 0;
    protected float velocity = 0.005f;
    protected boolean positive = true;

    public void update(int delta) {
        for (MovingTile tile : movingTiles) {
            if (stepsMoved < 1000 && positive) {
                stepsMoved++;
                tile.moveY(delta * -velocity);
                tile.getBoundingShape().movePosition(0,delta * -velocity);
            } else if (stepsMoved > 0) {
                stepsMoved--;
                tile.moveY(delta * velocity);
                tile.getBoundingShape().movePosition(0,delta * velocity);
                positive = false;
            } else {
                positive = true;
            }
        }
    }

    public void render(GameContainer gc, Graphics g) {

        int offset_x = getXOffset();
        int offset_y = getYOffset();

        renderBackground();

        for (MovingTile tile : movingTiles) {
            g.drawImage(tile.getImg(), tile.getXF()*32-offset_x, tile.getYF()*32-offset_y);
        }

        map.render(-(offset_x%32), -(offset_y%32), offset_x/32, offset_y/32, 33, 19);


        for(Character c : characters){
            c.render(offset_x,offset_y, gc, g);
        }
    }

    protected void renderBackground() {
        //first calculate the maximum amount we can "scroll" the background image before we have the rightmore or bottom most pixel on the screen
        float backgroundXScrollValue = (background.getWidth()-Game.WINDOW_WIDTH/Game.SCALE);
        float backgroundYScrollValue = (background.getHeight()-Game.WINDOW_HEIGTH/Game.SCALE);

        //we do the same for the map
        float mapXScrollValue = ((float)map.getWidth()*32-Game.WINDOW_WIDTH/Game.SCALE);
        float mapYScrollValue = ((float)map.getHeight()*32-Game.WINDOW_HEIGTH/Game.SCALE);

        //and now calculate the factor we have to multiply the offset with, making sure we multiply the offset by -1 to get it to negative
        float scrollXFactor = backgroundXScrollValue/mapXScrollValue * -1;
        float scrollYFactor = backgroundYScrollValue/mapYScrollValue * -1;

        //and now draw it using the factor and the offset to see where we start drawing
        background.draw(this.getXOffset()*scrollXFactor,this.getYOffset()*scrollYFactor);
    }

    protected void loadTileMap() {
        movingTiles = new ArrayList<>();
        tiles = new Tile[map.getWidth()][map.getHeight()];

        int layerIndex = map.getLayerIndex("CollisionLayer");

        if (layerIndex == -1) {
            System.err.println("Map does not have the layer \"CollisionLayer\"");
            System.exit(0);
        }

        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {

                int tileID = map.getTileId(i, j, layerIndex);

                Tile tile;

                String tileType = map.getTileProperty(tileID, "tileType", "air");

                switch(tileType){
                    case "solid":
                        tile = new SolidTile(i,j);
                        break;
                    case "deadly":
                        tile = new DeadlyTile(i,j);
                        break;
                    default:
                        tile = new AirTile(i,j);
                        break;
                }
                tiles[i][j] = tile;
            }

        }

        int nGroups = map.getObjectGroupCount();

        if (nGroups == -1) {
            System.err.println("Map does not have the layer \"MovingPlatfromsLayer\"");
        }

        for (int groupID = 0; groupID < nGroups; groupID++) {
            int objectCount = map.getObjectCount(groupID);

            for( int objectID = 0; objectID < objectCount; objectID++ ) // oi = object index
            {

                String objName = map.getObjectName(groupID, objectID);
                int objX = map.getObjectX(groupID, objectID);
                int objY = map.getObjectY(groupID, objectID);

                if (objName.equals("moving")) {
                    MovingTile tile = new MovingTile(objX/32,objY/32);
                    movingTiles.add(tile);
                }
            }
        }
    }

    public void addCharacter(view.Character c) {
        characters.add(c);
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public ArrayList<MovingTile> getMovingTiles() {
        return movingTiles;
    }

    public int getXOffset(){
        int offset_x;

        //the first thing we are going to need is the half-width of the screen, to calculate if the player is in the middle of our screen
        int half_width = (int) (Game.WINDOW_WIDTH/Game.SCALE/2);

        //next up is the maximum offset, this is the most right side of the map, minus half of the screen offcourse
        int maxX = map.getWidth()*32 -half_width;

        //now we have 3 cases here
        if(player.getX() < half_width){
            //the player is between the most left side of the map, which is zero and half a screen size which is 0+half_screen
            offset_x = 0;
        }else if(player.getX() > maxX){
            //the player is between the maximum point of scrolling and the maximum width of the map
            //the reason why we substract half the screen again is because we need to set our offset to the topleft position of our screen
            offset_x = maxX-half_width;
        }else{
            //the player is in between the 2 spots, so we set the offset to the player, minus the half-width of the screen
            offset_x = (int) (player.getX()-half_width);
        }

        return offset_x;
    }

    public int getYOffset(){
        int offset_y;

        int half_heigth = (int) (Game.WINDOW_HEIGTH/Game.SCALE/2);

        int maxY = map.getHeight()*32 -half_heigth;

        if(player.getY() < half_heigth){
            offset_y = 0;
        }else if(player.getY() > maxY){
            offset_y = maxY-half_heigth;
        }else{
            offset_y = (int) (player.getY()-half_heigth);
        }

        return offset_y;
    }

}
