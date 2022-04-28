package com.mygdx.pirategame.tests.gameobjects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.enemy.SeaMonster;
import com.mygdx.pirategame.pathfinding.pathManager.AttackPath;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


/**
 * Test the sea monster class
 * @author Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class SeaMonsterTest {

    private static GameScreen mockedGameScreen;

    /**
     * Setup the testing environment
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

        mockedGameScreen = MockClass.mockGameScreenWithPlayer();
    }

    /**
     * Tests the creation of the object, using arbitrary coordinates
     */
    @Test(expected = Test.None.class)
    public void testInstantiation() {
        new SeaMonster(mockedGameScreen, 10, 10);
    }

    /**
     * Tests the creation of the object from a save file, using arbitrary coordinates
     */
    //@Test(expected = Test.None.class)
    @Ignore
    public void testInstantiationSaveFile() {
        new SeaMonster(mockedGameScreen, null);
    }

    /**
     * Tests to see if heart is destroyed after setting destroyed to true and updating
     */
    @Test
    public void testDestroy() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 10, 10);
        World world = seaMonster.screen.getWorld();
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        seaMonster.setToDestroy = true;
        seaMonster.destroySeaMonster();
        world.getBodies(bodies);

        Assert.assertFalse(bodies.contains(seaMonster.b2body, false));
    }

    /**
     * Tests to see if the collision body is correctly defined
     */
    @Test
    public void testDefineEntity() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 10, 10);
        Assert.assertTrue(seaMonster.b2body != null);
    }

    /**
     * Tests to see if entity loses health upon contact
     */
    @Test
    public void testContact() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 10, 10);
        float oldHealth = seaMonster.health;
        seaMonster.onContact();
        float newHealth = seaMonster.health;
        Assert.assertNotEquals(oldHealth, newHealth);
    }

    /**
     * Tests to see if entity is destroyed upon contact with another object
     * body destruction has been previously tested when setToDestroyed is true
     */
    @Test
    public void testContactOther() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 10, 10);
        seaMonster.setPathManager(new AttackPath(seaMonster.pathManager, seaMonster, seaMonster.screen));
        seaMonster.path = null;
        seaMonster.onContactOther();

        Assert.assertNotNull(seaMonster.path);
    }


    /**
     * Tests to see if the sea monster generates a new path manager
     * and path when in range of the player
     */
    @Test
    public void testPlayerInRangeNoPathManager() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 13*64 / PirateGame.PPM, 11*64 / PirateGame.PPM);
        // Update to set a path manager and generate a new path
        seaMonster.update(0.001f);
        Assert.assertNotNull(seaMonster.pathManager);
        Assert.assertNotNull(seaMonster.path);
    }

    /**
     * Tests to see if the sea monster generates a new path when in range of the player
     */
    @Test
    public void testPlayerMovesOutOfRange() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 20*64 / PirateGame.PPM, 20*64 / PirateGame.PPM);
        // sea monster is currently in moving state
        seaMonster.movement = true;
        seaMonster.update(0.0001f);
        Assert.assertFalse(seaMonster.movement);
    }

    /**
     * Tests to see if the sea monster generates a new path when in range of the player
     */
    @Test
    public void testEmptyPath() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 20*64 / PirateGame.PPM, 20*64 / PirateGame.PPM);
        seaMonster.setPathManager(new AttackPath(seaMonster.pathManager, seaMonster, seaMonster.screen));
        seaMonster.path = null;
        seaMonster.update(0.0001f);
        Assert.assertNotNull(seaMonster.path);
    }

    /**
     * Tests to see if
     */
    @Test
    public void testNoHealth() {
        SeaMonster seaMonster = new SeaMonster(mockedGameScreen, 20*64 / PirateGame.PPM, 20*64 / PirateGame.PPM);
        // sea monster is currently in moving state
        seaMonster.health = 0;
        seaMonster.update(0.0001f);
        Assert.assertTrue(seaMonster.setToDestroy);
    }
}
