/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import org.newdawn.slick.Input;
import view.Player;

/**
 * @author Victor
 */
public class PlayerController {

    private Player player;
    private boolean high = false;
    private long time = 0;

    public PlayerController(Player player) {
        this.player = player;
    }

    public void handleInput(Input i, int delta) {

        if (time == 0) {
            time = System.currentTimeMillis();
        }

        boolean upPressed = i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP);
        boolean rightPressed = i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT);
        boolean leftPressed = i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT);

        if (i.isKeyDown(Input.KEY_X) && (System.currentTimeMillis() - time) > 200 && player.getYVelocity() == 0) {
            high = !high;
            if (high) {
                player.setXVelocity(0);
                player.setRollingEnabled(true);
                player.setMaximumFallSpeed(0.15f);
            } else {
                player.setMaximumFallSpeed(0.22f);
            }
            time = 0;
        }

        if (upPressed && player.getYVelocity() == 0) {
            player.jump();
        }
        if (rightPressed) {
            player.moveRight(delta);
        }
        if (leftPressed) {
            player.moveLeft(delta);
        }

        if (player.isHighEnabled() && (System.currentTimeMillis() - player.getHighTime()) > Player.HIGH_DURATION) {
            high = false;
            player.setHighEnabled(high);
            player.setHighTime(0);
            player.setMaximumFallSpeed(0.22f);
        }

        if (!rightPressed && !leftPressed) {
            player.setMoving(false);
        }
    }
}
