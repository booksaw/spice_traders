package com.mygdx.pirategame.tests.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.gameobjects.entity.Tornado;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


/**
 * Test the Player class
 * @author Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class PlayerTest {

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
     * test to see if a cannonball is destroyed when set to destroy and the ship object is updated
     */
    @Test
    public void testDestroyCannonball() {
        GameScreen screen = mockedGameScreen;
        Player player = new Player(screen);
        player.timeFired = 1000;
        player.fire(screen.getCamera());
        int oldCannonballCount = player.cannonBalls.size;
        player.cannonBalls.get(0).setToDestroy();
        player.update(0.0001f);
        int newCannonballCount = player.cannonBalls.size;
        Assert.assertNotEquals(oldCannonballCount, newCannonballCount);
    }

    /**
     * test to see if the tornado moves the player when in range
     */
    @Test
    public void testTornado() {
        GameScreen screen = mockedGameScreen;
        Player player = new Player(screen);
        // Create tornado
        Tornado tornado = new Tornado(screen, 10, 10);
        tornado.player = player;
        GameScreen.Tornados.add(tornado);
        player.inTornadoRange = true;
        Vector2 oldPos = player.b2body.getPosition().cpy();
        // Ensure that the player is in range of the tornado
        player.setX(10);
        player.setY(10);
        player.update(0.0001f);
        Vector2 newPos = player.b2body.getPosition();
        Assert.assertNotEquals(oldPos, newPos);
    }
}
