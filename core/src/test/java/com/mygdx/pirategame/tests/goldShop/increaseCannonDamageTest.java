package com.mygdx.pirategame.tests.goldShop;

import static com.badlogic.gdx.math.MathUtils.ceil;
import static org.junit.Assert.assertEquals;

import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.gameobjects.enemy.College;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Test the increased damage upgrade
 * @author Dan Wade
 */
@RunWith(PirateGameTest.class)
public class increaseCannonDamageTest {

    private static GameScreen mockedGameScreen;
    private static GoldShop mockedGoldShop;
    private static College testCollege;
    private static EnemyShip testEnemyShip;
    private static HashMap<CollegeMetadata, College> colleges = new HashMap<>();
    private static ArrayList<EnemyShip> ships = new ArrayList<>();

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

        Mockito.when(mockedGoldShop.getColleges()).thenReturn(colleges);
        Mockito.when(mockedGoldShop.getEnemyShips()).thenReturn(ships);

        Mockito.doCallRealMethod().when(mockedGoldShop).purchaseIncreaseCannonDamage();


        testCollege = new College(mockedGameScreen, CollegeMetadata.ANNELISTER, 0, null);
        colleges.put(CollegeMetadata.ANNELISTER,testCollege);

        String shipTexture = "college/Ships/" + CollegeMetadata.ANNELISTER.getFilePath() + "_ship.png";
        testEnemyShip = new EnemyShip(mockedGameScreen,0,0,shipTexture,CollegeMetadata.ANNELISTER);
        ships.add(testEnemyShip);

    }


    /**
     * Test cannon damage increases after purchase
     */
    @Test
    public void testCannonDamageIncrease() {
        // Make sure there are enough coins to purchase
        Hud.setCoins(mockedGoldShop.increaseCannonDamagePrice);

        float originalCollegeDamage = testCollege.damage;
        float expectedCollegeDamage = Math.round(originalCollegeDamage * mockedGoldShop.increaseCannonDamageMultiplier);

        float originalShipDamage = testEnemyShip.damage;
        float expectedShipDamage = Math.round(originalShipDamage * mockedGoldShop.increaseCannonDamageMultiplier);

        mockedGoldShop.purchaseIncreaseCannonDamage();

        float newCollegeDamage = testCollege.damage;
        float newShipDamage = testEnemyShip.damage;

        assertEquals(newCollegeDamage,expectedCollegeDamage,0.00001);
        assertEquals(newShipDamage,expectedShipDamage,0.00001);
    }

    /**
     * Test user balance decreases by the price of the increase cannon damage powerup on purchase from Gold Shop
     */
    @Test
    public void testCoinBalanceAfterPurchase(){
        // Give the player enough coins to purchase the faster cannon
        Hud.setCoins(mockedGoldShop.increaseCannonDamagePrice);
        int originalCoins = Hud.getCoins();

        mockedGoldShop.purchaseIncreaseCannonDamage();

        // Test that the player's coin balance has decreased after purchase
        int newCoins = Hud.getCoins();
        assertEquals(newCoins, originalCoins - mockedGoldShop.increaseCannonDamagePrice);
    }

    /**
     * Test that the player is not given increased cannon damage if they do not have enough coins
     */
    @Test
    public void testNotEnoughCoins(){
        // Give player less coins than needed to purchase
        Hud.setCoins(mockedGoldShop.increaseCannonDamagePrice- 1);

        float originalCollegeDamage = testCollege.damage;
        float originalShipDamage = testEnemyShip.damage;

        mockedGoldShop.purchaseIncreaseCannonDamage();

        float newCollegeDamage = testCollege.damage;
        float newShipDamage = testEnemyShip.damage;

        // Check damage values have not changed
        assertEquals(newCollegeDamage,originalCollegeDamage,0.00001);
        assertEquals(newShipDamage,originalShipDamage,0.00001);
    }



}
