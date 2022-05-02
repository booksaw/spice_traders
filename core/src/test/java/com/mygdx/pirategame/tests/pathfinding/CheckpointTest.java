package com.mygdx.pirategame.tests.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.pathfinding.Checkpoint;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit testing for the checkpoint class
 * @author James McNair
 * TEST ID: UT_34
 */
public class CheckpointTest {

    /**
     * Used to test if the x and y variables are declared correctly in the checkpoint class
     */
    @Test
    public void testXY() {
        Checkpoint checkpoint = new Checkpoint(1f, 1f, 1f);

        Assert.assertTrue(checkpoint.x == 1f && checkpoint.y == 1f && checkpoint.gradient == 1f);
    }

    /**
     * Tests the method Checkpoint.createCheckpointFromTilemap
     * ensuring the the tile coords are scaled correctly
     */
    @Test
    public void testCreateCheckpointFromTilemap() {
        Checkpoint cp = Checkpoint.createCheckpointFromTilemap(1, 1, 10);

        // performing x * gradient + (gradient / 2) as method centers the result to the middle of the tile
        Assert.assertTrue(cp.x == 15f && cp.y == 15f && cp.gradient == 10f);

    }

    /**
     * Test used to test the tile calculations
     */
    @Test
    public void testTileCalculations() {
        Checkpoint cp = new Checkpoint(10f, 10f, 10f);

        Assert.assertTrue(cp.getTileX() == 1 && cp.getTileY() == 1);

    }

    /**
     * Test the checkpoint comparison is valid
     */
    @Test
    public void testCheckpointComparison() {

        Checkpoint cp = new Checkpoint(1f, 1f, 1f);
        
        // testing invalid type
        Assert.assertFalse(cp.equals("hi"));
        
        // testing null type
        Assert.assertFalse(cp.equals(null));

        // testing different checkpoint
        Assert.assertFalse(cp.equals(new Checkpoint(2f, 1f, 1f)));

        // testing equivalent checkpoint
        Assert.assertTrue(cp.equals(new Checkpoint(1f, 1f, 1f)));

        // testing same object
        assertTrue(cp.equals(cp));
    }

    /**
     * Ensure a valid Vector2 is returned
     */
    @Test
    public void testVector2() {
        Checkpoint cp = new Checkpoint(1f, 1f, 1f);
        Assert.assertEquals(cp.getVector2(), new Vector2(1f, 1f));
    }

    /**
     * Test the toString method returns the expected value
     */
    @Test
    public void testToString() {
        Checkpoint cp = new Checkpoint(1f, 1f, 1f);
        Assert.assertEquals(cp.toString(), "Checkpoint{x=1.0, y=1.0, gradient=1.0}");
    }

}
