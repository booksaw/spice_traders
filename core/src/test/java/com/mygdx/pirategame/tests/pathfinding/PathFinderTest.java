package com.mygdx.pirategame.tests.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.pathfinding.Checkpoint;
import com.mygdx.pirategame.pathfinding.PathFinder;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the PathFinder class
 * @author James McNair
 */
@RunWith(PirateGameTest.class)
public class PathFinderTest {

    private static GameScreen mockedGameScreen;

    /**
     * Setup the test environment
     */
    @BeforeClass
    public static void init() {
        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = new FakeGL20();

        // note all mocking cannot appear in a @Test annotated method
        // or the mocking will not work, all mocking must occur in @BeforeClass
        // at least from my testing it does not even work in a @Before method
        MockClass.mockHudStatic();

        mockedGameScreen = MockClass.mockGameScreen();
    }

    /**
     * Ensuring the debug tools for the pathfinder are disabled
     */
    @Test
    public void checkDebug() {
        // checking if the debug tools for pathfinding are disabled
        assertFalse("PathFinder debug tools must be disabled", PathFinder.PATHFINDERDEBUG);
    }

    /**
     * Testing if the pathfinder will detect the land at the given location
     */
    @Test
    public void testNotTraversableIsland() {
        PathFinder pathFinder = new PathFinder(mockedGameScreen,64);

        assertFalse(pathFinder.isTraversable(0, 0, 1, 1));

    }

    /**
     * Testing if the pathfinder will detect the rock in the given location
     */
    @Test
    public void testNotTraversableRock() {
        PathFinder pathFinder = new PathFinder(mockedGameScreen, 64);

        assertFalse(pathFinder.isTraversable(17 * 64, 10*64,3,3));

    }

    /**
     * Testing if the pathfinder will detect that the given location is sea
     */
    @Test
    public void testTraversable() {
        PathFinder pathFinder = new PathFinder(mockedGameScreen, 64);

        assertTrue(pathFinder.isTraversable(13*64, 11*64, 1, 1));
    }

    /**
     * Testing if the pathfinder returns null when an invalid start location is provided
     */
    @Test
    public void testPathFinderInvalidStart() {
        PathFinder pathFinder = new PathFinder(mockedGameScreen, 64);

        List<Checkpoint> cps = pathFinder.getPath(0, 0, 64, 64, 1, 1);
        Assert.assertEquals(null, cps);
    }

    /**
     * Ensuring a valid path is found between two valid locations
     */
    @Test
    public void testPathFinderValid() {
        PathFinder pathFinder = new PathFinder(mockedGameScreen, 64);

        List<Checkpoint> cps = pathFinder.getPath(2432, 2432, 2304, 1792, 1, 1);

        System.out.println(cps);
        Assert.assertFalse(cps == null || cps.isEmpty());
    }


}
