
package view.screens;

import view.Game;
import java.io.IOException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.ResourceLoader;
import view.Game;

/**
 *
 * @author Victor
 */
public class Loading extends BasicGameState {
    
    public static final int ID = -1;
    
    /** The image that will be shown on load completion */
    private Image image;
    /** The font that will be rendered on load completion */
    private Font font;
    /** The next resource to load */
    private DeferredResource nextResource;
    /** True if we've loaded all the resources and started rendereing */
    private boolean started = false;
    
    private Game game;

    public Loading(Game game) {
        this.game = game;
    }
    
    @Override
    public void render(GameContainer gc,StateBasedGame sbg, Graphics g) throws SlickException { 
         if (nextResource != null) {
                g.drawString("Loading: "+nextResource.getDescription(), 100, 100);
        }

        int total = LoadingList.get().getTotalResources();
        int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();

        float bar = loaded / (float) total;
        g.fillRect(100,150,loaded*40,20);
        g.drawRect(100,150,total*40,20);
    } 

    @Override
    public void update(GameContainer gc,StateBasedGame sbg, int i) throws SlickException { 
        if (started) {
            //game.initGame(gc);
            game.enterState(MainMenu.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black) );
        }
        
        if (nextResource != null) {
            try {
                nextResource.load();
            } catch (IOException e) {
                throw new SlickException("Failed to load: "+nextResource.getDescription(), e);
            }

            nextResource = null;
        }

        if (LoadingList.get().getRemainingResources() > 0) {
            nextResource = LoadingList.get().getNext();
        } else {
            if (!started) {
                started = true;
            }
        }
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        LoadingList.setDeferredLoading(true);
        
        /* MainMenu */
        new Image("resources/MainMenu/play.png");
        new Image("resources/MainMenu/load.png");
        new Image("resources/MainMenu/credits.png");
        new Image("resources/MainMenu/exit.png");
        
        new Image("resources/MainMenu/play_pressed.png");
        new Image("resources/MainMenu/load_pressed.png");
        new Image("resources/MainMenu/credits_pressed.png");
        new Image("resources/MainMenu/exit_pressed.png");
        
        new Image("resources/MainMenu/bg.png");
        
        new Image("resources/right.png");
        new Image("resources/left.png");
        
        new Sound("resources/sounds/Bleepy 001.wav");
        new Music("resources/sounds/BSO/Bob_Marley_Jammin_8_Bit.wav");
        new Music("resources/sounds/BSO/Bob Marley - Waiting In Vain ( 8-bit Sounds ).wav");
        
        new Sound("resources/sounds/Bleepy 001.wav");
        
        ResourceLoader.getResourceAsStream("resources/font.ttf");
        
        /* Player */
        
        new Sound("resources/sounds/nintendo_09.wav");
        new Music("resources/sounds/BSO/UB40 - Red Wine (8 bit ).wav");
        
        new Image("resources/stand.png");
        new Image("resources/stand-high.png");
        new Image("resources/right.png");
        new Image("resources/left.png");
        new Image("resources/jump.png");
        new Image("resources/jump_left.png");
        new Image("resources/right-high.png");
        new Image("resources/left-high.png");
        new Image("resources/jump-high.png");
        new Image("resources/jump_left-high.png");
        new Image("resources/rolling-joint.png");
        new Image("resources/rolling-joint.png");
                
    }

    @Override
    public int getID() {
        return ID;
    }

}
