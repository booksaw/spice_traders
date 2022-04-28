package com.mygdx.pirategame.tests.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.entity.CoinMagnet;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.junit.Assert;

/**
 * Unit tests for the CoinMagnet class
 * @author Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class CoinMagnetTest {

    private static GameScreen mockedGameScreen;

    /**
     * Setup The test environment
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
        new CoinMagnet(mockedGameScreen, 10, 10);
    }

    /**
     * Tests to see if power up is destroyed after setting destroyed to true and updating
     */
    @Test
    public void testDestroy() {
        CoinMagnet coinMagnet = new CoinMagnet(mockedGameScreen, 10, 10);
        World world = coinMagnet.getWorld();
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        coinMagnet.setToDestroyed = true;
        coinMagnet.update();
        world.getBodies(bodies);

        Assert.assertFalse(bodies.contains(coinMagnet.b2body, false));
    }

    /**
     * Tests to see if the power up is positioned correctly when not destroyed
     */
    @Test
    public void testPosition() {
        CoinMagnet coinMagnet = new CoinMagnet(mockedGameScreen, 10, 10);
        Vector2 oldPos = coinMagnet.b2body.getPosition();
        coinMagnet.update();
        Vector2 newPos = coinMagnet.b2body.getPosition();

        Assert.assertEquals(oldPos, newPos);
    }

    /**
     * Tests to see if power up ends correctly
     */
    @Test
    public void testPowerupEnd() {
        CoinMagnet coinMagnet = new CoinMagnet(mockedGameScreen, 10, 10);
        coinMagnet.timer = coinMagnet.duration + 1;
        coinMagnet.update();
        Assert.assertTrue(!coinMagnet.active);
    }


    /**
     * Tests to see if the collision body is correctly defined
     */
    @Test
    public void testDefineEntity() {
        CoinMagnet coinMagnet = new CoinMagnet(mockedGameScreen, 10, 10);
        Assert.assertTrue(coinMagnet.b2body != null);
    }

    /**
     * Tests to see if the coin magnet is activated properly
     */
    @Test
    public void testToggleCoinMagnet() {
        CoinMagnet coinMagnet = new CoinMagnet(mockedGameScreen, 10, 10);
        coinMagnet.toggleCoinMagnet = true;
        coinMagnet.update();

        Assert.assertFalse(coinMagnet.toggleCoinMagnet);
    }

//    /**
//     * Tests to see if entity is destroyed upon contact with another object
//     * body destruction has been previously tested when setToDestroyed is true
//     */
//    @Test
//    public void testContact() {
//    	AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
//    	heart.entityContact();
//      	Assert.assertTrue(heart.setToDestroyed);
//    }
}
