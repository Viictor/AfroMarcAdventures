package model.physics;

import model.Level1;
import model.LevelObject;
import model.tiles.MovingTile;
import model.tiles.Tile;
import view.Character;
import view.Player;

import java.util.ArrayList;

/**
 *
 * @author Victor
 */
public class Physics {

    private final float gravity = 0.0015f;
    private final float gravityModifier = 0.4f;
    private boolean high = false;
    private boolean playerDead = false;

    public void handlePysics(Level1 level, int delta, boolean high) {
        this.high = high;
        handleCharacters(level, delta);
    }

    private boolean checkCollision(LevelObject obj, Tile[][] mapTiles, ArrayList<MovingTile> movTiles) {
        //get only the tiles that matter
        ArrayList<Tile> tiles = obj.getBoundingShape().getTilesOccupying(mapTiles, movTiles);
        
        if (tiles != null ) {
            for (Tile t : tiles) {
                //if this tile has a bounding shape
                if (t.getBoundingShape() != null) {
                    if (t.getBoundingShape().checkCollision(obj.getBoundingShape())) {
                        
                        if (t.getBoundingShape() instanceof DeadlyBoundingRect) {
                            playerDead = true;
                            return false;
                        }
                        return true;
                    }
                }
            }
        } else {
            
            int off = obj.getBoundingShape().offTheMargins();
            if (off == -1) {
                playerDead = true;
            } else if (off == 1) {
                return true;
            } 
        }
        return false;
    }

    private boolean isOnGroud(LevelObject obj, Tile[][] mapTiles, ArrayList<MovingTile> movingTiles) {
        //we get the tiles that are directly "underneath" the characters, also known as the ground tiles
        ArrayList<Tile> tiles = obj.getBoundingShape().getGroundTiles(mapTiles, movingTiles);

        //we lower the the bounding object a bit so we can check if we are actually a bit above the ground
        obj.getBoundingShape().movePosition(0, 1);

        for (Tile t : tiles) {
            //not every tile has a bounding shape (air tiles for example)
            if (t.getBoundingShape() != null) {
                //if the ground and the lowered object collide, then we are on the ground
                if (t.getBoundingShape().checkCollision(obj.getBoundingShape())) {
                    if (t.getBoundingShape() instanceof DeadlyBoundingRect) {
                        playerDead = true;
                        return false;
                    }
                    //don't forget to move the object back up even if we are on the ground!
                    obj.getBoundingShape().movePosition(0, -1);
                    return true;
                }
            }
        }
        //and obviously we have to move the object back up if we don't hit the ground
        obj.getBoundingShape().movePosition(0, -1);

        return false;
    }

    private void handleCharacters(Level1 level, int delta) {
        for (Character c : level.getCharacters()) {

            //and now decelerate the character if he is not moving anymore
            if (!c.isMoving()) {
                c.decelerate(delta);
            }
            
            if (c.getYVelocity() < -0.05 && c instanceof Player) {
               c.setFrame(0, ((Player) c).isHighEnabled());
            } else if (c.getYVelocity() < 0.05 && c instanceof Player) {
               c.setFrame(1, ((Player) c).isHighEnabled());
            } else if (c.getYVelocity() > 0.15 && c instanceof Player) {
                c.setFrame(2, ((Player) c).isHighEnabled());
            }

            handleGameObject(c, level, delta);
        }
    }

    private void handleGameObject(LevelObject obj, Level1 level, int delta) {

        //first update the onGround of the object
        obj.setOnGround(isOnGroud(obj, level.getTiles(), level.getMovingTiles()));

        //now apply gravitational force if we are not on the ground or when we are about to jump
        if (!obj.isOnGround() || obj.getYVelocity() < 0) {
                if (high) {
                    obj.applyGravity(gravity * gravityModifier * delta);
                } else {
                    obj.applyGravity(gravity * delta);
                }

        } else {
            obj.setYVelocity(0);
        }

        //calculate how much we actually have to move
        float x_movement = obj.getXVelocity() * delta;
        float y_movement = obj.getYVelocity() * delta;

        //we have to calculate the step we have to take
        float step_y = 0;
        float step_x = 0;

        if (x_movement != 0) {
            step_y = Math.abs(y_movement) / Math.abs(x_movement);
            if (y_movement < 0) {
                step_y = -step_y;
            }

            if (x_movement > 0) {
                step_x = 1;
            } else {
                step_x = -1;
            }

            if ((step_y > 1 || step_y < -1) && step_y != 0) {
                step_x = Math.abs(step_x) / Math.abs(step_y);
                if (x_movement < 0) {
                    step_x = -step_x;
                }
                if (y_movement < 0) {
                    step_y = -1;
                } else {
                    step_y = 1;
                }
            }
        } else if (y_movement != 0) {
            //if we only have vertical movement, we can just use a step of 1
            if (y_movement > 0) {
                step_y = 1;
            } else {
                step_y = -1;
            }
        }

        //and then do little steps until we are done moving
        while (x_movement != 0 || y_movement != 0) {

            //we first move in the x direction
            if (x_movement != 0) {
                //when we do a step, we have to update the amount we have to move after this
                if ((x_movement > 0 && x_movement < step_x) || (x_movement > step_x && x_movement < 0)) {
                    step_x = x_movement;
                    x_movement = 0;
                } else {
                    x_movement -= step_x;
                }

                //then we move the object one step
                obj.setX(obj.getX() + step_x);

                //if we collide with any of the bounding shapes of the tiles we have to revert to our original position
                if (checkCollision(obj, level.getTiles(), level.getMovingTiles())) {

                    //undo our step, and set the velocity and amount we still have to move to 0, because we can't move in that direction
                    obj.setX(obj.getX() - step_x);
                    obj.setXVelocity(0);
                    x_movement = 0;
                }

            }
            //same thing for the vertical, or y movement
            if (y_movement != 0) {
                if ((y_movement > 0 && y_movement < step_y) || (y_movement > step_y && y_movement < 0)) {
                    step_y = y_movement;
                    y_movement = 0;
                } else {
                    y_movement -= step_y;
                }

                obj.setY(obj.getY() + step_y);

                if (checkCollision(obj, level.getTiles(), level.getMovingTiles())) {
                    obj.setY(obj.getY() - step_y);
                    obj.setYVelocity(0);
                    y_movement = 0;
                    break;
                }
            }
        }
    }

    public boolean isPlayerDead() {
        return playerDead;
    }
}
