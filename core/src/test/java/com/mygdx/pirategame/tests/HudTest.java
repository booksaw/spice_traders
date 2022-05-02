package com.mygdx.pirategame.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.ShellSpriteBatch;
import com.mygdx.pirategame.gameobjects.entity.AbsorptionHeart;
import com.mygdx.pirategame.save.GameScreen;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

/**
 * Unit tests for the Hud class
 * @author Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class HudTest {

    private static GameScreen mockedGameScreen;
    private static Hud hud;

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
        //MockClass.mockHudStatic();

        mockedGameScreen = MockClass.mockGameScreen();

        hud = new Hud(null, true);
    }

    /**
     * Tests to see if timers of the power ups change to zero when reset
     */
    @Test
    public void testResetPowerUpTimers() {
        //MockClass.mockHudStatic();
        Hud.setAbsorptionHeartTimer(1);
        Hud.setCoinMagnetTimer(2);
        Hud.setFasterShootingTimer(3);
        Hud.setFreezeEnemyTimer(4);
        Hud.setSpeedBoostTimer(5);

        Hud.resetPowerUpTimers();

        Assert.assertEquals(0, Hud.getAbsorptionHeartTimer(), 1);
        Assert.assertEquals(0, Hud.getCoinMagnetTimer(), 1);
        Assert.assertEquals(0, Hud.getFasterShootingTimer(), 1);
        Assert.assertEquals(0, Hud.getFreezeEnemyTimer(), 1);
        Assert.assertEquals(0, Hud.getSpeedBoostTimer(), 1);
    }

    /**
     * Tests to see if score increases each second
     */
    @Test
    public void testIncreaseScoreEverySecond() {
        float oldScore = Hud.getPoints();
        hud.setTimeCount(2f);
        hud.update(0.0001f);
        float newScore = Hud.getPoints();
        Assert.assertNotEquals(newScore, oldScore);
    }
}
