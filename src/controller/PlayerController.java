/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.Player;
import org.newdawn.slick.Input;

/**
 *
 * @author Victor
 */
public class PlayerController {
    
    private Player player;
    private boolean upPressed,rightPressed, leftPressed, high = false;
    private long time = 0;
    
    public PlayerController(Player player) {
        this.player = player;
    }
    
    public void handleInput(Input i, int delta) {
        
        if (time == 0) {
            time = System.currentTimeMillis();
        }
        
        if (i.isKeyDown(Input.KEY_W) || i.isKeyDown(Input.KEY_UP)) {
            upPressed = true;
        } else {
            upPressed = false;
        }
        if (i.isKeyDown(Input.KEY_D) || i.isKeyDown(Input.KEY_RIGHT)) {
            rightPressed = true;
        } else {
            rightPressed = false;
        }
        if (i.isKeyDown(Input.KEY_A) || i.isKeyDown(Input.KEY_LEFT)) {
            leftPressed = true;
        } else {
            leftPressed = false;
        }
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
        
        if (upPressed && player.getYVelocity() == 0){
            player.jump();
        }
        if (rightPressed){
            player.moveRight(delta);
        }
        if (leftPressed){
            player.moveLeft(delta);
        }
        
        player.setHighEnabled(high);
        
        if (!rightPressed && !leftPressed) {
            player.setMoving(false);
        }
    }
}
