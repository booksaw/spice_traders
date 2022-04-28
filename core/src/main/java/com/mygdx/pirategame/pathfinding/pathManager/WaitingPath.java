package com.mygdx.pirategame.pathfinding.pathManager;

import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.gameobjects.enemy.SeaMonster;
import com.mygdx.pirategame.gameobjects.entity.Tornado;
import com.mygdx.pirategame.save.GameScreen;

/**
 * Superclass used for all pathing managers which are in a passive state waiting for a ship to come into range to attack
 * This class will switch to an attacking pathing manager if within range of an enemy
 * @author James McNair, Charlie Crosley, Robert Murphy
 */
public abstract class WaitingPath implements PathManager {

    protected EnemyShip ship = null;
    protected SeaMonster seaMonster = null;
    protected Tornado tornado = null;
    protected final GameScreen screen;

    /**
     * Used for all passive pathing managers that can start an attack
     * @param ship The ship that is being pathed
     * @param screen The gameScreen controlling the level
     */
    public WaitingPath(EnemyShip ship, GameScreen screen) {
        this.ship = ship;
        this.screen = screen;
    }

    /**
     * used for all passive pathing sea monsters that can start an attack
     * @param seaMonster The sea monster that is being pathed
     * @param screen The GameScreen controlling the level
     */
    public WaitingPath(SeaMonster seaMonster, GameScreen screen) {
        this.seaMonster = seaMonster;
        this.screen = screen;
    }

    /**
     * Used for all passive pathing tornadoes that can start an attack
     * @param tornado The tornado that is being pathed
     * @param screen The GameScreen controlling the level
     */
    public WaitingPath(Tornado tornado, GameScreen screen) {
        this.tornado = tornado;
        this.screen = screen;
    }

    /**
     * Update if the object should start an attack run
     * @param dt The Delta time of this update
     */
    @Override
    public void update(float dt) {
        // if the ship is in range of the player
        if (ship != null) {
            if ((ship.collegeMeta == null || !ship.collegeMeta.isPlayer()) && ship.b2body.getPosition().dst(screen.getPlayerPos()) < 3) {
                ship.setPathManager(new AttackPath(this, ship, screen));
            }
        }
        else {
            if (seaMonster.b2body.getPosition().dst(screen.getPlayerPos()) < 3) {
                seaMonster.setPathManager(new AttackPath(this, seaMonster, screen));
            }
            if (tornado.b2body.getPosition().dst(screen.getPlayerPos()) < 3) {
                tornado.setPathManager(new AttackPath(this, tornado, screen));
            }
        }


    }

}
