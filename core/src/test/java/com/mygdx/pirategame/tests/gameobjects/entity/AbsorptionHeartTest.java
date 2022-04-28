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
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.save.NewGameSaveLoader;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.junit.Assert;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the AbsorptionHeart class
 * @author Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class AbsorptionHeartTest {

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
        new AbsorptionHeart(mockedGameScreen, 10, 10);
    }
    
    /**
     * Tests to see if heart is destroyed after setting destroyed to true and updating
     */
    @Test
    public void testDestroy() {
    	AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
    	World world = heart.getWorld();
    	Array<Body> bodies = new Array<Body>();
    	world.getBodies(bodies);
    	
    	heart.setToDestroyed = true;
    	heart.update();
    	world.getBodies(bodies);
    	
    	Assert.assertFalse(bodies.contains(heart.b2body, false));
    }
    
    /**
     * Tests to see if heart is positioned correctly when not destroyed
     */
    @Test
    public void testPosition() {
    	AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
    	Vector2 oldPos = heart.b2body.getPosition();
    	heart.update();
    	Vector2 newPos = heart.b2body.getPosition();
    	    	
    	Assert.assertTrue(oldPos == newPos);
    }
    
    /**
     * Tests to see if power up ends correctly, this ability isn't on a timer so this is all that is needed
     */
    @Test
    public void testPowerupEnd() {
    	AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
    	heart.timer = heart.duration + 1;
    	heart.update();
      	Assert.assertTrue(!heart.active);
    }

    /**
     * Tests to see if the collision body is correctly defined
     */
    @Test
    public void testDefineEntity() {
        AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
        Assert.assertTrue(heart.b2body != null);
    }

    /**
     * Tests to see if the collision body is correctly defined
     */
    @Test
    public void testGetSound() {
        AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
        Assert.assertNotNull(heart.getSound());
    }


    /**
     * Tests to see if entity is destroyed upon contact with another object
     * body destruction has been previously tested when setToDestroyed is true
     */
    @Test
    public void testContact() {
    	AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
    	heart.entityContact();
      	Assert.assertTrue(heart.setToDestroyed);
    }
}
