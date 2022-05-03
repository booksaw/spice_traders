package com.mygdx.pirategame.pathfinding.pathManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.save.GameScreen;

import java.util.Random;

/**
 * This class is used to manage the pathing of a ship while it is not targeting a specific entity
 * @author James McNair
 */
public class PatrolPath extends WaitingPath{


    /**
     * Create a new PatrolPath, The ship must be assigned to a valid college else this will throw an error
     *
     * @param ship   The ship
     * @param screen The GameScreen managing the game
     */
    public PatrolPath(EnemyShip ship, GameScreen screen) {
        super(ship, screen);

        if (ship.collegeMeta == null) {
            throw new IllegalArgumentException("Ship cannot patrol a college when it is not assigned to a valid college");
        }
    }

    /**
     * Generate a new destination within range of the college the ship belongs to
     * @return The generated destination
     */
    @Override
    public Vector2 generateDestination() {
        Random rnd = new Random();

        int tileWidth = (int) PirateGame.PPM;

        while (true) {
            // generate random position for ship to go around the college
            int x = rnd.nextInt(3000) - 1500 + (int) (ship.collegeMeta.getX() * tileWidth);
            int y = rnd.nextInt(3000) - 1500 + (int) (ship.collegeMeta.getY() * tileWidth);

            // bounding the location
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (x > screen.getTileMapWidth()) {
                x = screen.getTileMapWidth();
            }
            if (y > screen.getTileMapHeight()) {
                y = screen.getTileMapHeight();
            }

            // checking if the location is valid
            if (ship.isTraversable(x, y)) {
                // going to that location
                return new Vector2(x, y);
            }
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
