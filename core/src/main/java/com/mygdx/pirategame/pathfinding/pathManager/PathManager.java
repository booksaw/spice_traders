package com.mygdx.pirategame.pathfinding.pathManager;

import com.badlogic.gdx.math.Vector2;

/**
 * Used to manage a type of pathing that a ship can have
 * @author James McNair
 */
public interface PathManager {

    /**
     * Generates a destination (called when a the current path fails or is completed)
     * @return The generated destination
     */
    public abstract Vector2 generateDestination();

    /**
     * Called once a tick in case this type of path finding requires remapping during a path
     * @param dt The Delta time of this update
     */
    public abstract void update(float dt);

}
