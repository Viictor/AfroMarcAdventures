
package model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import view.Player;

import java.util.ArrayList;


/**
 *
 * @author Victor
 */
public class Level1 extends Level{
    
    public Level1(String level, Player player) throws SlickException {
        map = new TiledMap("resources/levels/" + level + ".tmx", "resources");
        background = new Image("resources/levels/" + map.getMapProperty("background", "bg-level.png"));
        characters = new ArrayList<>();
        this.player = player;
        characters.add(player);
        loadTileMap();
    }

}
