package com.mygdx.pirategame.pathfinding;

import com.badlogic.gdx.math.Vector2;

/**
 * A location checkpoint along a path
 * @author James McNair
 */
public class Checkpoint {
    /**
     * Used to create a checkpoint from a tilemap grid position. The location of the checkpoint will be the centre of the grid
     *
     * @param x        The x coord
     * @param y        The y coord
     * @param gradient The gradient of the tilemap
     * @return The created checkpoint
     */
    public static Checkpoint createCheckpointFromTilemap(int x, int y, int gradient) {
        return new Checkpoint((float) (x * gradient) + gradient / 2, (float) (y * gradient) + gradient / 2, gradient);
    }

    public float x, y;

    public final float gradient;

    /**
     * @param x the x coord of the checkpoint in game space
     * @param y the y coord of the checkpoint in game space
     */
    public Checkpoint(float x, float y, float gradient) {
        this.x = x;
        this.y = y;

        this.gradient = gradient;
    }

    /**
     * @return the tilemap X locaiton
     */
    public int getTileX() {
        return (int) (x / gradient);
    }

    /**
     * @return The tilemap Y location
     */
    public int getTileY() {
        return (int) (y / gradient);
    }

    /**
     * Compare two Checkpoints
     * @param o The checkpoint to compare with
     * @return If the two checkpoints are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checkpoint that = (Checkpoint) o;
        return Float.compare(that.getTileX(), getTileX()) == 0 && Float.compare(that.getTileY(), getTileY()) == 0;
    }

    /**
     * @return A string representation of the checkpoint, useful for debugging
     */
    @Override
    public String toString() {
        return "Checkpoint{" +
                "x=" + x +
                ", y=" + y +
                ", gradient=" + gradient +
                '}';
    }

    /**
     * @return The location of the checkpoint as a vector
     */
    public Vector2 getVector2(){
        return new Vector2(x, y);
    }
}
