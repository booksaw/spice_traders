package com.mygdx.pirategame.tests.gameobjects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.enemy.College;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.pathfinding.Checkpoint;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import com.mygdx.pirategame.world.AvailableSpawn;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the EnemyShip class
 * @author Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class EnemyShipTest {

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
     * test to see if the college takes damage upon contact
     */
    @Test
    public void testOnContact() {
        GameScreen screen = mockedGameScreen;
        EnemyShip ship = new EnemyShip(screen, 0, 0, "college/Ships/anne_lister_ship.png", CollegeMetadata.ANNELISTER);
        float oldHealth = ship.health;
        ship.onContact();
        float newHealth = ship.health;
        Assert.assertNotEquals(oldHealth, newHealth);
    }

    /**
     * test to see if a cannonball is destroyed when set to destroy and the ship object is updated
     */
    @Ignore
    public void testDestroyCannonball() {
        GameScreen screen = mockedGameScreen;
        EnemyShip ship = new EnemyShip(screen, 0, 0, "college/Ships/anne_lister_ship.png", CollegeMetadata.ANNELISTER);
        ship.fire();
        int oldCannonballCount = ship.cannonBalls.size;
        ship.cannonBalls.get(0).setToDestroy();
        ship.update(0.0001f);
        int newCannonballCount = ship.cannonBalls.size;
        Assert.assertNotEquals(oldCannonballCount, newCannonballCount);
    }

    /**
     * test to see if the ship is destroyed when updated with setToDestroy flag as true
     */
    @Test
    public void testDestroyShip() {
        GameScreen screen = mockedGameScreen;
        EnemyShip ship = new EnemyShip(screen, 0, 0, "college/Ships/anne_lister_ship.png", CollegeMetadata.ANNELISTER);
        World world = ship.screen.getWorld();
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        ship.setToDestroy = true;
        ship.update(0.0001f);
        world.getBodies(bodies);

        Assert.assertFalse(bodies.contains(ship.b2body, false));
    }

    /**
     * test to see if a cannonball is spawned when the fired method is called
     */
    @Ignore
    public void testFireCannonball() {
        GameScreen screen = mockedGameScreen;
        EnemyShip ship = new EnemyShip(screen, 0, 0, "college/Ships/anne_lister_ship.png", CollegeMetadata.ANNELISTER);
        int oldCannonballCount = ship.cannonBalls.size;
        ship.fire();
        int newCannonballCount = ship.cannonBalls.size;
        Assert.assertNotEquals(oldCannonballCount, newCannonballCount);
    }
}
