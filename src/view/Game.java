
package view;

import controller.LevelController;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import view.screens.MainMenu;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class Game extends StateBasedGame {
    
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGTH = WINDOW_WIDTH / 16*9;
    public static final boolean FULLSCREEN = false;
    
    public static final float SCALE = (float)(1.25*((double)WINDOW_WIDTH/1280));
    public static final String GAME_NAME = "AfroMarc Adventures";

    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Game());
            app.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGTH, FULLSCREEN);
            app.setShowFPS(false);
            app.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Game() {
        super(GAME_NAME);
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        initGame(gc);
    }
    
    public void initGame(GameContainer gc) throws SlickException {
        LevelController level_1 = new LevelController("level_4", this);
        MainMenu menu = new MainMenu(this);
        
        addState(menu);
        addState(level_1);
    }

    @Override
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);
        
        if (key == Input.KEY_ESCAPE) {
            System.exit(0);
        }
    }
    
    public void restartLevel(int state, String level) {
        enterState(state, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
    }
}
