package com.mygdx.pirategame.tests.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.entity.AbsorptionHeart;
import com.mygdx.pirategame.gameobjects.entity.CoinMagnet;
import com.mygdx.pirategame.gameobjects.entity.FreezeEnemy;
import com.mygdx.pirategame.gameobjects.entity.SpeedBoost;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.save.NewGameSaveLoader;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.junit.Assert;

/**
 * Unit tests for the SpeedBoost class
 * @author Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class SpeedBoostTest {

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
     * Tests the creation of the object, using arbitrary coordinates
     */
    @Test(expected = Test.None.class)
    public void testInstantiation() {
        new SpeedBoost(mockedGameScreen, 10, 10);
    }

    /**
     * Tests to see if heart is destroyed after setting destroyed to true and updating
     */
    @Test
    public void testDestroy() {
        SpeedBoost speedBoost = new SpeedBoost(mockedGameScreen, 10, 10);
        World world = speedBoost.getWorld();
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        speedBoost.setToDestroyed = true;
        speedBoost.update();
        world.getBodies(bodies);

        Assert.assertFalse(bodies.contains(speedBoost.b2body, false));
    }

    /**
     * Tests to see if heart is positioned correctly when not destroyed
     */
    @Test
    public void testPosition() {
        SpeedBoost speedBoost = new SpeedBoost(mockedGameScreen, 10, 10);
        Vector2 oldPos = speedBoost.b2body.getPosition();
        speedBoost.update();
        Vector2 newPos = speedBoost.b2body.getPosition();

        Assert.assertTrue(oldPos == newPos);
    }

    /**
     * Tests to see if power up ends correctly
     */
    @Test
    public void testPowerupEnd() {
        SpeedBoost speedBoost = new SpeedBoost(mockedGameScreen, 10, 10);
        speedBoost.timer = speedBoost.duration + 1;
        speedBoost.update();
        Assert.assertTrue(!speedBoost.active);
    }

    /**
     * Tests to see if the collision body is correctly defined
     */
    @Test
    public void testDefineEntity() {
        SpeedBoost speedBoost = new SpeedBoost(mockedGameScreen, 10, 10);
        Assert.assertTrue(speedBoost.b2body != null);
    }


    /**
     * Tests to see if entity is destroyed upon contact with another object
     * body destruction has been previously tested when setToDestroyed is true
     */
    @Test
    public void testContact() {
        SpeedBoost speedBoost = new SpeedBoost(mockedGameScreen, 10, 10);
        speedBoost.entityContact();
        Assert.assertTrue(speedBoost.setToDestroyed);
    }
}
