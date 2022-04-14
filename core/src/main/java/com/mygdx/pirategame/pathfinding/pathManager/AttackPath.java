package com.mygdx.pirategame.pathfinding.pathManager;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.gameobjects.enemy.SeaMonster;
import com.mygdx.pirategame.save.GameScreen;

import java.util.Random;

/**
 * Class used for pathing an AI ship to attack a target
 */
public class AttackPath implements PathManager {

    // the path that the ship used to be on
    private final PathManager previousPath;
    private EnemyShip ship = null;
    private SeaMonster seaMonster = null;
    private final GameScreen screen;

    public AttackPath(PathManager previousPath, EnemyShip ship, GameScreen screen){
        this.previousPath = previousPath;
        this.ship = ship;
        this.screen = screen;
    }

    public AttackPath(PathManager previousPath, SeaMonster seaMonster, GameScreen screen){
        this.previousPath = previousPath;
        this.seaMonster = seaMonster;
        this.screen = screen;
    }

    @Override
    public Vector2 generateDestination() {
        Random rnd = new Random();

        int tileWidth = (int) PirateGame.PPM;

        // limiting attempts at finding a valid attack path
        for (int i = 0; i < 10; i++) {
            // generate random position for ship to go around the college
            // making the player position the origin
            Vector2 diff;
            if (ship != null) {
                diff = ship.b2body.getPosition().sub(screen.getPlayerPos());
            }
            else {
                diff = seaMonster.b2body.getPosition().sub(screen.getPlayerPos());
            }
            // limiting the distance of the player to 2 tiles
            diff = diff.limit(200 / tileWidth);
            diff.add(screen.getPlayerPos());

            int x = rnd.nextInt(200) + (int) (diff.x * tileWidth);
            int y = rnd.nextInt(200) + (int) (diff.y * tileWidth);

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
            if (ship != null) {
                if (ship.isTraversable(x, y)) {
                    // going to that location
                    return new Vector2(x, y);
                }
            }
            else {
                if (seaMonster.isTraversable(x, y)) {
                    // going to that location
                    return new Vector2(x, y);
                }
            }
        }
        System.out.println("reverting to previous path");
        // attack path could not be found, reverting to passive
        ship.setPathManager(previousPath);
        return null;
    }

    private int fireDelay = 0;

    @Override
    public void update(float dt) {
        if (ship != null) {
            // if the ship out of range of the player
            if (ship.b2body.getPosition().dst(screen.getPlayerPos()) > 6 || (ship.collegeMeta != null && !ship.collegeMeta.isPlayer() && ship.collegeMeta.getPosition().dst(ship.b2body.getPosition()) > 40)) {
                ship.setPathManager(previousPath);
                return;
            }
            fireDelay++;
            if(fireDelay > 50){
                ship.fire();
                fireDelay = 0;
            }
        }
        else {
            if (seaMonster.b2body.getPosition().dst(screen.getPlayerPos()) > 7) {
                seaMonster.setPathManager(previousPath);
                return;
            }

            fireDelay++;
            if(fireDelay > 50){
                seaMonster.fire();
                fireDelay = 0;
            }
        }
    }
}
