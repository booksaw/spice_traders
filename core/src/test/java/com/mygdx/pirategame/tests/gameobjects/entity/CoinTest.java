package com.mygdx.pirategame.tests.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.entity.AbsorptionHeart;
import com.mygdx.pirategame.gameobjects.entity.Coin;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(PirateGameTest.class)
public class CoinTest {

    private static GameScreen mockedGameScreen;

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
        new Coin(mockedGameScreen, 10, 10);
    }


    /**
     * Tests to see if heart is destroyed after setting destroyed to true and updating
     */
    @Test
    public void testDestroy() {
        Coin coin = new Coin(mockedGameScreen, 10, 10);
        World world = coin.getWorld();
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        coin.setToDestroyed = true;
        coin.update();
        world.getBodies(bodies);

        Assert.assertFalse(bodies.contains(coin.b2body, false));
    }

    /**
     * Tests to see if the coin moves position when in range of the player whilst
     * the coin magnet power up is activated
     */
    @Test
    public void testMagnetOn() {
        Coin coin = new Coin(mockedGameScreen, 10, 10);
        Vector2 oldPos = coin.b2body.getPosition().cpy();
        coin.inMagnetRange = true;
        coin.update();
        Vector2 newPos = coin.b2body.getPosition();

        Assert.assertNotEquals(oldPos, newPos);
    }


    /**
     * Tests to see if the collision body is correctly defined
     */
    @Test
    public void testDefineEntity() {
        Coin coin = new Coin(mockedGameScreen, 10, 10);
        Assert.assertTrue(coin.b2body != null);
    }

    /**
     * Tests to see if the larger collision body is used when
     * the toggle function is called
     */
    @Test
    public void testMagnetCollisionBodyOn() {
        Coin coin = new Coin(mockedGameScreen, 10, 10);
        coin.toggleCoinMagnet();
        Assert.assertTrue(coin.b2bodyMagnet.isActive());
    }

    /**
     * Tests to see if the default collision body is used when
     * the toggle function is called whilst the larger body is currently active
     */
    @Test
    public void testMagnetCollisionBodyOff() {
        Coin coin = new Coin(mockedGameScreen, 10, 10);
        coin.b2bodyMagnet.setActive(true);
        coin.toggleCoinMagnet();
        Assert.assertFalse(coin.b2bodyMagnet.isActive());
    }

    /**
     * Tests to see if the coin recognises if the coin magnet is activated
     */
    @Test
    public void testMagnetActive() {
        Coin coin = new Coin(mockedGameScreen, 10, 10);
        coin.coinMagnetActive = true;
        coin.entityContact();
        Assert.assertTrue(coin.inMagnetRange);
    }
}
