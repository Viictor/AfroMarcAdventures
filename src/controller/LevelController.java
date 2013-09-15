package controller;

import model.Level1;
import model.physics.Physics;
import org.newdawn.slick.Color;
import org.newdawn.slick.*;
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
public class LevelController extends BasicGameState implements ComponentListener {

    public static final int ID = 1;
    private Level1 level;
    private String startingLevel;
    private Player player;
    private PlayerController playerController;
    private Physics physics;
    private MouseOverArea mouseOverRestart;
    private Game game;

    private TrueTypeFont ttFont;
    private TrueTypeFont ttFont40;


    public LevelController(String startingLevel, Game game) {
        this.startingLevel = startingLevel;
        this.game = game;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        player = new Player(48, 1440);
        level = new Level1(startingLevel, player);
        playerController = new PlayerController(player);
        physics = new Physics();
        Image restartbutton = new Image("resources/MainMenu/play.png");
        Image restartbuttonPressed = new Image("resources/MainMenu/play_pressed.png");
        
        mouseOverRestart = new MouseOverArea(gc, restartbutton,
                Game.WINDOW_WIDTH/2 - restartbutton.getWidth() / 2,
                Game.WINDOW_HEIGTH/2 - restartbutton.getHeight() / 2 + 100,
                restartbutton.getWidth(), restartbutton.getHeight(), this);
        mouseOverRestart.setMouseDownImage(restartbuttonPressed);
        mouseOverRestart.setMouseDownSound(new Sound("resources/sounds/Bleepy 001.wav"));
        
        
        try {
            
            InputStream inputStream = ResourceLoader.getResourceAsStream("resources/font.ttf");

            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(Font.PLAIN, 20);
            Font font40 = font.deriveFont(Font.PLAIN, 40);
            
            ttFont = new TrueTypeFont(font, true);
            ttFont40 = new TrueTypeFont(font40, true);
            
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(LevelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if (!physics.isPlayerDead()) {
            g.scale(Game.SCALE, Game.SCALE);
            level.render(gc, g);
            ttFont.drawString(10, 10, "High : " + player.isHighEnabled());
        } else {
            //level.render(gc, g);
            ttFont40.drawString(Game.WINDOW_WIDTH / 2 - ttFont40.getWidth("You Died !")/2, Game.WINDOW_HEIGTH / 2, "You Died !");
            mouseOverRestart.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (!physics.isPlayerDead()) {
            if (!player.isRollingEnabled()) {
                playerController.handleInput(gc.getInput(), delta);
            }
            physics.handlePysics(level, delta, player.isHighEnabled());
        } else {
            Input in = gc.getInput();
            if (in.isKeyDown(Input.KEY_ENTER)) {
                game.restartLevel(LevelController.ID, startingLevel);
            }
        }

    }

    @Override
    public void componentActivated(AbstractComponent ac) {
        if (ac == mouseOverRestart) {
            game.enterState(LevelController.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        }
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        super.leave(container, game);
        this.init(container, game);
    }
}
