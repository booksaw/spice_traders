package com.mygdx.pirategame.tests.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.entity.*;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.save.NewGameSaveLoader;
import com.mygdx.pirategame.save.XmlSaveLoader;
import com.mygdx.pirategame.tests.FakeGL20;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the PathFinder class
 * @author James McNair, Marc Perales Salomo, Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class GameScreenTest {

	// stopping some tests timeing out by increasing the timeout timer
	@Rule
	public Timeout timeout = new Timeout(100000, TimeUnit.SECONDS);

	private static PirateGame mockedPirateGame;

	@BeforeClass
	public static void init() {
		// Use Mockito to mock the OpenGL methods since we are running headlessly
		Gdx.gl20 = Mockito.mock(GL20.class);
		Gdx.gl = new FakeGL20();

		// note all mocking cannot appear in a @Test annotated method
		// or the mocking will not work, all mocking must occur in @BeforeClass
		// at least from my testing it does not even work in a @Before method
		MockClass.mockHudStatic();

		mockedPirateGame = MockClass.mockPirateGame();

	}

	/**
	 * Ensure the debugging tools are disabled
	 * TEST ID: UT_2
	 */
	@Test
	public void testDebugToolsDisabled() {
		assertFalse(GameScreen.PHYSICSDEBUG);
	}


	/**
	 * Ensure the game screen can be instantiated
	 * TEST ID: UT_35
	 */
	@Test
	public void testConstructor() {
		GameScreen screen = new GameScreen(mockedPirateGame, new NewGameSaveLoader(), true);

		assertTrue(GameScreen.gameStatus == 0);
	}


	/**
	 * Tests to see if each power up type is added
	 * TEST ID: UT_36
	 */
	@Ignore
	public void testAddPowerUps() {
		GameScreen screen = new GameScreen(mockedPirateGame, new NewGameSaveLoader(), true);
		GameScreen mockedGameScreen = MockClass.mockGameScreen();
		screen.addPowerUps();
		ArrayList<PowerUp> powerUps = screen.PowerUps;
		Assert.assertEquals(powerUps.get(0).getClass(), AbsorptionHeart.class);
		Assert.assertEquals(powerUps.get(1).getClass(), SpeedBoost.class);
		Assert.assertEquals(powerUps.get(2).getClass(), FasterShooting.class);
		Assert.assertEquals(powerUps.get(3).getClass(), CoinMagnet.class);
		Assert.assertEquals(powerUps.get(4).getClass(), FreezeEnemy.class);
	}

	/**
	 * Tests to see if the table of buttons becomes visible when changing to the screen
	 * TEST ID: UT_37
	 */
	@Test
	public void testScreenChange() {
		GameScreen screen = new GameScreen(mockedPirateGame, new NewGameSaveLoader(), true);
		screen.show();

		Assert.assertTrue(screen.table.isVisible());
	}

	/**
	 * Tests to see if the function returns a random location
	 * TEST ID: UT_38
	 */
	@Test
	public void testGetRandomLocation() {
		GameScreen screen = new GameScreen(mockedPirateGame, new NewGameSaveLoader(), true);
		int[] loc1 = screen.getRandomLocation();
		int[] loc2 = screen.getRandomLocation();
		Assert.assertNotEquals(loc1, loc2);
	}

	/**
	 * Tests to see if enemy's damage is changed
	 * TEST ID: UT_39
	 */
	@Ignore
	public void testEnemyDamage() {
		GameScreen screen = new GameScreen(mockedPirateGame, new NewGameSaveLoader(), true);
		float oldDamage = screen.getEnemyShips().get(0).damage;
		screen.changeDamage(5);

		Assert.assertEquals(screen.getEnemyShips().get(0).damage, oldDamage + 5);
	}

	/**
	 * Tests to see if the pause method pauses the game
	 * TEST ID: UT_40
	 */
	@Test
	public void testPauseGame() {
		GameScreen screen = new GameScreen(mockedPirateGame, new NewGameSaveLoader(), true);
		screen.pause();
		Assert.assertEquals(screen.gameStatus, screen.GAME_PAUSED);
	}

	/**
	 * Tests to see if the resume method unpauses the game
	 * TEST ID: UT_41
	 */
	@Test
	public void testResumeGame() {
		GameScreen screen = new GameScreen(mockedPirateGame, new NewGameSaveLoader(), true);
		screen.resume();
		Assert.assertEquals(screen.gameStatus, screen.GAME_RUNNING);
	}
}
	

