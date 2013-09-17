
package view.screens;

import controller.LevelController;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.ResourceLoader;
import view.Game;
import view.Player;

import java.awt.Font;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class MainMenu extends BasicGameState implements ComponentListener{
    
    public static final  int ID = 0;
    
    private static final int PLAY = 0;
    private static final int LOAD = 1;
    private static final int CREDITS = 2;
    private static final int EXIT = 3;
    
    public static Music music;

    private TrueTypeFont ttFont;
    private TrueTypeFont ttFont20;
    
    private Image background;
    private Image[] buttons = new Image[4];
    private Image[] buttonsPressed = new Image[4];
    private MouseOverArea[] playArea = new MouseOverArea[4];
    private Music musicLevel_1;
    private Animation player;
    private Animation playerL;
    private Animation playerR;
    private long time = 0;
    private Game game;

    public MainMenu(Game game) {
        this.game = game;
    }

    @Override
    public int getID() {
        return ID;
    }
    
    private void initButtons() throws SlickException {
        
        buttons[0] = new Image("resources/MainMenu/play.png");
        buttons[1] = new Image("resources/MainMenu/load.png");
        buttons[2] = new Image("resources/MainMenu/credits.png");
        buttons[3] = new Image("resources/MainMenu/exit.png");
        
        buttonsPressed[0] = new Image("resources/MainMenu/play_pressed.png");
        buttonsPressed[1] = new Image("resources/MainMenu/load_pressed.png");
        buttonsPressed[2] = new Image("resources/MainMenu/credits_pressed.png");
        buttonsPressed[3] = new Image("resources/MainMenu/exit_pressed.png");
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        try {
            
            InputStream inputStream = ResourceLoader.getResourceAsStream("resources/font.ttf");

            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(Font.PLAIN, 60);
            Font font20 = font.deriveFont(Font.BOLD, 20);
            
            ttFont = new TrueTypeFont(font, true);
            ttFont20 = new TrueTypeFont(font20, true);
            
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(LevelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        background = new Image("resources/MainMenu/bg.png");
        initButtons();
        
        SpriteSheet right = new SpriteSheet(new Image("resources/right.png"), Player.SPRITE_WIDTH, Player.SPRITE_HEIGHT);
        SpriteSheet left = new SpriteSheet(new Image("resources/left.png"), Player.SPRITE_WIDTH, Player.SPRITE_HEIGHT);
        playerR = new Animation(right, 200);
        playerL = new Animation(left, 200);
        player = playerR;
        
        for (int i = 0; i < playArea.length; i++) {
            playArea[i] = new MouseOverArea(gc, buttons[i], 
                    (Game.WINDOW_WIDTH/2 - 404) + (buttons[i].getWidth()+ 50)*i, Game.WINDOW_HEIGTH - buttons[i].getHeight() - 20 , 
                    buttons[i].getWidth(), buttons[i].getHeight(), this);
            
            playArea[i].setMouseDownImage(buttonsPressed[i]);
            playArea[i].setMouseDownSound(new Sound("resources/sounds/Bleepy 001.wav"));
            
        }


        Music musicMenu = new Music("resources/sounds/BSO/Bob_Marley_Jammin_8_Bit.wav");
        musicLevel_1 = new Music("resources/sounds/BSO/Bob Marley - Waiting In Vain ( 8-bit Sounds ).wav");
        music = musicMenu;
        if (Game.SOUND) {
            music.loop();
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGTH);
        
        ttFont.drawString(Game.WINDOW_WIDTH / 2 - ttFont.getWidth("AfroMarc")/2, Game.WINDOW_HEIGTH / 2 - 200, "AfroMarc", Color.yellow);
        ttFont.drawString(Game.WINDOW_WIDTH / 2 - ttFont.getWidth("Adventures")/2, Game.WINDOW_HEIGTH / 2 - 130, "Adventures", Color.yellow);
        
        String movingControlls = "Move with W A S D or Arrows";
        String actionControlls = "Get High pressing the X key";
        ttFont20.drawString(Game.WINDOW_WIDTH / 2 - ttFont20.getWidth("Controls")/2, Game.WINDOW_HEIGTH / 2 + 130, "Controls", Color.white);
        ttFont20.drawString(Game.WINDOW_WIDTH / 2 - ttFont20.getWidth(movingControlls)/2, Game.WINDOW_HEIGTH / 2 + 150, movingControlls, Color.white);
        ttFont20.drawString(Game.WINDOW_WIDTH / 2 - ttFont20.getWidth(actionControlls)/2, Game.WINDOW_HEIGTH / 2 + 170, actionControlls, Color.white);
        
        player.draw(Game.WINDOW_WIDTH/2 - Player.SPRITE_WIDTH/2, Game.WINDOW_HEIGTH/2 - Player.SPRITE_HEIGHT/2);

        for (MouseOverArea aPlayArea : playArea) {
            aPlayArea.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        if (time == 0) {
            time = System.currentTimeMillis();
        }
        
        if ((System.currentTimeMillis() - time) >= 5000) {
            if (player.equals(playerL)) {
                player = playerR;
            } else {
                player = playerL;
            }
            time = 0;
        }
        
        Input in = gc.getInput();
        if (in.isKeyDown(Input.KEY_ENTER)) {
            if (Game.SOUND) {
                music.stop();
                music = musicLevel_1;
                music.loop();
            }
            game.enterState(LevelController.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }

    @Override
    public void componentActivated(AbstractComponent ac) {
        if (ac == playArea[PLAY]) {
            if (Game.SOUND) {
                music.stop();
                music = musicLevel_1;
                music.loop();
            }
            game.enterState(LevelController.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }else if (ac == playArea[EXIT]) {
            System.exit(0);
        }
    }
    
}
