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
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.junit.Assert;

/**
 * Unit tests for the AbsorptionHeart class
 *
 */
@RunWith(PirateGameTest.class)
public class AbsorptionHeartTest {

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
    public void testNotDestroy() {
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
      	Assert.assertTrue(heart.timer == 0);
    }
    
    /**
     * Tests to see if timer of power up is incremented each update
     */
    @Test
    public void testTimerIncrement() {
    	AbsorptionHeart heart = new AbsorptionHeart(mockedGameScreen, 10, 10);
    	float oldTimer = heart.timer;
    	heart.active = true;
    	heart.update();
    	float newTimer = heart.timer;
      	Assert.assertTrue(oldTimer != newTimer);
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
