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
 * Class to test the heath boost upgrade
 * @author Dan Wade
 */
@RunWith(PirateGameTest.class)
public class healthBoostTest {

    private static GameScreen mockedGameScreen;
    private static GoldShop mockedGoldShop;

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
;
        Mockito.doCallRealMethod().when(mockedGoldShop).purchaseHealthBoost();
        // Make sure player health is not 0
        Hud.setHealth(100);


    }


    /**
     * Test player's health increases after purchase
     */
    @Test
    public void testHealthIncrease(){
        int originalHealth = Hud.getHealth();

        // Give the player enough coins to purchase the health boost
        Hud.setCoins(mockedGoldShop.healthBoostPrice);

        mockedGoldShop.purchaseHealthBoost();

        int newHealth = Hud.getHealth();

        assertEquals(newHealth,originalHealth + mockedGoldShop.healthBoostValue);


    }

    /**
     * Test user balance decreases by the price of the health boost on purchase from Gold Shop
     */
    @Test
    public void testCoinBalanceAfterPurchase(){
        // Give the player enough coins to purchase the faster cannon
        Hud.setCoins(mockedGoldShop.healthBoostPrice);
        int originalCoins = Hud.getCoins();


        mockedGoldShop.purchaseHealthBoost();

        // Test that the player's coin balance has decreased after purchase
        int newCoins = Hud.getCoins();
        assertEquals(newCoins, originalCoins - mockedGoldShop.healthBoostPrice);
    }

    /**
     * Test that the player is not given a health boost if they do not have enough coins
     */
    @Test
    public void testNotEnoughCoins(){
        // Give player less coins than needed to purchase faster cannon
        Hud.setCoins(mockedGoldShop.healthBoostPrice - 1);

        int originalHealth = Hud.getHealth();

        mockedGoldShop.purchaseHealthBoost();

        int newHealth = Hud.getHealth();

        // Check that the cannon velocity has not changed
        assertEquals(originalHealth,newHealth);
    }






}
