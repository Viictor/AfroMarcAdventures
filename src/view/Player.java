
package view;

import model.physics.AABoundingRect;
import org.newdawn.slick.*;
import view.screens.MainMenu;

import java.util.EnumMap;

/**
 * @author Victor
 */
public class Player extends Character {

    public static final int SPRITE_WIDTH = 63;
    public static final int SPRITE_HEIGHT = 63;
    public static final long HIGH_DURATION = 50000;

    public static Music highSound;

    private SpriteSheet right;
    private SpriteSheet left;
    private SpriteSheet jump;
    private SpriteSheet jumpLeft;
    private SpriteSheet rightHigh;
    private SpriteSheet leftHigh;
    private SpriteSheet jumpHigh;
    private SpriteSheet jumpLeftHigh;
    private SpriteSheet rolling;

    private SpriteSheet rollingLeft;
    protected EnumMap<Player.Facing, Image> spritesHigh;
    protected EnumMap<Player.Facing, Animation> movingHighAnim;
    protected EnumMap<Player.Facing, Animation> jumpingHighAnim;

    protected EnumMap<Player.Facing, Animation> rollingAnim;
    private Sound jumpSound = new Sound("resources/sounds/nintendo_09.wav");
    private boolean highEnabled = false;
    private long highTime = 0;
    private boolean rollingEnabled = false;
    private long rollingTime = 0;
    private long rollingDuration;

    public Player(float x, float y) throws SlickException {
        super(x, y);
        setSprite(new Image("resources/stand.png"), new Image("resources/stand-high.png"));

        initSprites();
        setMovingAnimation(right, left, rightHigh, leftHigh, 120);
        setJumpingAnimation(jump, jumpLeft, jumpHigh, jumpLeftHigh, 150);
        setRollingAnimation(rolling, rollingLeft, 250);
        rollingDuration = rollingAnim.get(facing).getFrameCount() * 250;

        boundingShape = new AABoundingRect(x, y, 45, 63);

        accelerationSpeed = 0.001f;
        maximumSpeed = 0.25f;
        maximumFallSpeed = 0.22f;
        decelerationSpeed = 0.001f;

        highSound = new Music("resources/sounds/BSO/UB40 - Red Wine (8 bit ).wav");
    }

    private void initSprites() throws SlickException {
        right = new SpriteSheet(new Image("resources/right.png"), SPRITE_WIDTH, SPRITE_HEIGHT);
        left = new SpriteSheet(new Image("resources/left.png"), SPRITE_WIDTH, SPRITE_HEIGHT);

        jump = new SpriteSheet(new Image("resources/jump.png"), SPRITE_WIDTH, SPRITE_HEIGHT);
        jumpLeft = new SpriteSheet(new Image("resources/jump_left.png"), SPRITE_WIDTH, SPRITE_HEIGHT);

        rightHigh = new SpriteSheet(new Image("resources/right-high.png"), SPRITE_WIDTH, SPRITE_HEIGHT);
        leftHigh = new SpriteSheet(new Image("resources/left-high.png"), SPRITE_WIDTH, SPRITE_HEIGHT);

        jumpHigh = new SpriteSheet(new Image("resources/jump-high.png"), SPRITE_WIDTH, SPRITE_HEIGHT);
        jumpLeftHigh = new SpriteSheet(new Image("resources/jump_left-high.png"), SPRITE_WIDTH, SPRITE_HEIGHT);

        rolling = new SpriteSheet(new Image("resources/rolling-joint.png"), SPRITE_WIDTH, SPRITE_HEIGHT);
        rollingLeft = new SpriteSheet(new Image("resources/rolling-joint.png"), SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    @Override
    public void updateBoundingShape() {
        boundingShape.updatePosition(x, y);
    }

    private void setSprite(Image image, Image imageHigh) {
        sprites = new EnumMap<>(Facing.class);
        sprites.put(Facing.RIGHT, image);
        sprites.put(Facing.LEFT, image.getFlippedCopy(true, false));

        spritesHigh = new EnumMap<>(Facing.class);
        spritesHigh.put(Facing.RIGHT, imageHigh);
        spritesHigh.put(Facing.LEFT, imageHigh.getFlippedCopy(true, false));
    }

    private void setMovingAnimation(SpriteSheet sR, SpriteSheet sL, SpriteSheet shr, SpriteSheet shl, int frameDuration) {
        movingAnim = new EnumMap<>(Facing.class);
        movingAnim.put(Facing.RIGHT, new Animation(sR, frameDuration));
        movingAnim.put(Facing.LEFT, new Animation(sL, frameDuration));

        movingHighAnim = new EnumMap<>(Facing.class);
        movingHighAnim.put(Facing.RIGHT, new Animation(shr, frameDuration));
        movingHighAnim.put(Facing.LEFT, new Animation(shl, frameDuration));
    }

    private void setJumpingAnimation(SpriteSheet sR, SpriteSheet sL, SpriteSheet shr, SpriteSheet shl, int frameDuration) {
        jumpingAnim = new EnumMap<>(Facing.class);
        jumpingAnim.put(Facing.RIGHT, new Animation(sR, frameDuration));
        jumpingAnim.put(Facing.LEFT, new Animation(sL, frameDuration));
        jumpingAnim.get(Facing.RIGHT).setAutoUpdate(false);
        jumpingAnim.get(Facing.LEFT).setAutoUpdate(false);

        jumpingHighAnim = new EnumMap<>(Facing.class);
        jumpingHighAnim.put(Facing.RIGHT, new Animation(shr, frameDuration));
        jumpingHighAnim.put(Facing.LEFT, new Animation(shl, frameDuration));
        jumpingHighAnim.get(Facing.RIGHT).setAutoUpdate(false);
        jumpingHighAnim.get(Facing.LEFT).setAutoUpdate(false);
    }

    private void setRollingAnimation(SpriteSheet sR, SpriteSheet sL, int frameDuration) {
        rollingAnim = new EnumMap<>(Facing.class);
        rollingAnim.put(Facing.RIGHT, new Animation(sR, frameDuration));
        rollingAnim.put(Facing.LEFT, new Animation(sL, frameDuration));
        rollingAnim.get(Facing.RIGHT).setLooping(false);
        rollingAnim.get(Facing.LEFT).setLooping(false);
    }

    @Override
    public void render(float offset_x, float offset_y, GameContainer gc, Graphics g) {

        if (rollingEnabled) {
            rollingAnim.get(facing).draw(x - offset_x, y - offset_y);
        }
        if (System.currentTimeMillis() - rollingTime > rollingDuration) {
            if (rollingEnabled) {
                setRollingEnabled(false);
                highEnabled = true;
            }
            rollingAnim.get(facing).restart();

            if (!highEnabled) {
                if (jumpingAnim != null && y_velocity != 0) {
                    jumpingAnim.get(facing).draw(x - offset_x, y - offset_y);
                } else if (movingAnim != null && moving) {
                    movingAnim.get(facing).draw(x - offset_x, y - offset_y);
                } else {
                    sprites.get(facing).draw(x - offset_x, y - offset_y);
                }
            } else {
                if (jumpingHighAnim != null && y_velocity != 0) {
                    jumpingHighAnim.get(facing).draw(x - offset_x, y - offset_y);
                } else if (movingHighAnim != null && moving) {
                    movingHighAnim.get(facing).draw(x - offset_x, y - offset_y);
                } else {
                    spritesHigh.get(facing).draw(x - offset_x, y - offset_y);
                }
            }
        }

        ((AABoundingRect)boundingShape).render(g, offset_x, offset_y);
    }

    @Override
    public void jump() {
        if (onGround) {
            if (highEnabled) {
                y_velocity = -0.4f;
            } else {
                y_velocity = -0.6f;
            }
            jumpSound.play(1, 0.2f);
        }
    }

    @Override
    public void setFrame(int i, boolean high) {
        if (high) {
            jumpingHighAnim.get(facing).setCurrentFrame(i);
        } else {
            jumpingAnim.get(facing).setCurrentFrame(i);
        }
    }

    public boolean isHighEnabled() {
        return highEnabled;
    }

    public void setHighEnabled(boolean highEnabled) {
        this.highEnabled = highEnabled;
        if (!this.highEnabled) {
            highSound.stop();
            if (!MainMenu.music.playing()) {
                MainMenu.music.play();
            }
        }
    }

    public boolean isRollingEnabled() {
        return rollingEnabled;
    }

    public long getHighTime() {
        return highTime;
    }

    public void setHighTime(long highTime) {
        this.highTime = highTime;
    }

    public void setRollingEnabled(boolean rollingEnabled) {
        this.rollingEnabled = rollingEnabled;
        if (this.rollingEnabled) {
            MainMenu.music.stop();
            highSound.play();
            rollingTime = System.currentTimeMillis();
        } else {
            rollingTime = 0;
            highTime = System.currentTimeMillis();
        }
    }
}
