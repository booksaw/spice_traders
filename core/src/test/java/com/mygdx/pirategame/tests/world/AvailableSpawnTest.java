package com.mygdx.pirategame.tests.world;

import com.mygdx.pirategame.world.AvailableSpawn;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing the AvaliableSpawn class
 * @author James McNair
 */
public class AvailableSpawnTest {

    /**
     * testing the constructor works as expected
     * TEST ID: UT_42
     */
    @Test(expected = Test.None.class)
    public void testInstantiation() {
        new AvailableSpawn();
    }

    /**
     * Ensuring the correct values are stored as avaliable spawn
     * TEST ID: UT_42
     */
    @Test
    public void testHashMapValidity() {
        AvailableSpawn spawn = new AvailableSpawn();

        Assert.assertFalse(spawn.tileBlocked.containsKey(1));
    }

}
