package com.mygdx.pirategame.tests.goldShop;

import static com.badlogic.gdx.math.MathUtils.ceil;
import static org.junit.Assert.assertEquals;

import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.screen.GoldShop;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;

/**
 * Test faster cannon shooting works as expected
 * @author Dan Wade
 */
@RunWith(PirateGameTest.class)
public class fasterCannonTest {

    private static GameScreen mockedGameScreen;
    private static GoldShop mockedGoldShop;
    private static Player testPlayer;

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


        mockedGoldShop = MockClass.mockGoldShop();
        mockedGameScreen = MockClass.mockGameScreen();
        testPlayer = new Player(mockedGameScreen);
        Mockito.when(mockedGoldShop.getPlayer()).thenReturn(testPlayer);
        Mockito.doCallRealMethod().when(mockedGoldShop).purchaseFasterCannon();

    }


    /**
     * Test cannon velocity increases after purchase of faster cannon from Gold Shop
     */
    @Test
    public void testVelocityIncrease(){
        int originalVelocity = testPlayer.getCannonVelocity();

        // Give the player 50 coins
        Hud.setCoins(50);

        mockedGoldShop.purchaseFasterCannon();

        // Test that the cannon velocity has changed
        int newVelocity = testPlayer.getCannonVelocity();
        int expectedVelocity = ceil(originalVelocity * mockedGoldShop.fasterCannonMultiplier);
        assertEquals(newVelocity,expectedVelocity);

    }

    /**
     * Test user balance decreases by the price of the faster cannon on purchase from Gold Shop
     */
    @Test
    public void testCoinBalanceAfterPurchase(){
        // Give the player enough coins to purchase the faster cannon
        Hud.setCoins(mockedGoldShop.fasterCannonPrice);
        int originalCoins = Hud.getCoins();

        mockedGoldShop.purchaseFasterCannon();

        // Test that the player's coin balance has decreased after purchase
        int newCoins = Hud.getCoins();
        assertEquals(newCoins, originalCoins - mockedGoldShop.fasterCannonPrice);
    }

    /**
     * Test that the player is not given a faster cannon if they do not have enough coins
     * TEST ID: UT_29
     */
    @Test
    public void testNotEnoughCoins(){
        // Give player less coins than needed to purchase faster cannon
        Hud.setCoins(mockedGoldShop.fasterCannonPrice - 1);

        int originalVelocity = testPlayer.getCannonVelocity();

        mockedGoldShop.purchaseFasterCannon();

        int newVelocity = testPlayer.getCannonVelocity();

        // Check that the cannon velocity has not changed
        assertEquals(newVelocity,originalVelocity);
    }

    /**
     * Test that cannon velocity does not increase greater than 12 on purchase of faster cannon
     * Note that this powerup can be purchased multiple times so is limited to not make the player too powerful
     * The limit of 12 is hardcoded in the purchaseFasterCannon() method in the GoldShop class
     * TEST ID: UT_30
     */
    @Test
    public void testMaxCannonVelocity(){

        /**
         * Test the edge case where getCannonVelocity() * fasterCannonMultiplier == 12, so velocity should now become 12
         */
        Hud.setCoins(mockedGoldShop.fasterCannonPrice);
        testPlayer.setCannonVelocity(ceil(12 / mockedGoldShop.fasterCannonMultiplier));

        mockedGoldShop.purchaseFasterCannon();
        // Cannon velocity should now equal 12
        assertEquals(testPlayer.getCannonVelocity(),12);

        /**
         * Test the case where getCannonVelocity() * fasterCannonMultiplier > 12
         * In this case the cannon velocity should not change as purchasing would make getCannonVelocity() > 12
         */

        Hud.setCoins(mockedGoldShop.fasterCannonPrice);

        mockedGoldShop.purchaseFasterCannon();
        assertEquals(testPlayer.getCannonVelocity(),12);

    }






}
