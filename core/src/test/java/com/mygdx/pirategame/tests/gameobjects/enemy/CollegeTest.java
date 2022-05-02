package com.mygdx.pirategame.tests.gameobjects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.enemy.College;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import com.mygdx.pirategame.world.AvailableSpawn;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * Test the college class
 * @author James McNair, Marc Perales Salomo, Charlie Crosley
 */
@RunWith(PirateGameTest.class)
public class CollegeTest {

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
	 * test to ensure points increase when a college is destroyed (when college is
	 * not owned by player)
	 * TEST ID: UT_4
	 */
	@Test
	public void testPointsOnDestroy() {
		// mocking the hud


		GameScreen screen = mockedGameScreen;

		College college = new College(screen, CollegeMetadata.ANNELISTER, 0, null);

		// destroying the college
		college.setToDestroy = true;
		// giving the college a chance to destroy itself
		college.update(0);

		assertEquals(Hud.getPoints(), Integer.valueOf(100));
	}

	/**
	 * test to see if game ends when alcuin is defeated
	 * TEST ID: UT_5
	 */
	@Test
	public void testOnTrigAlcuinDeath() {
		GameScreen screen = mockedGameScreen;
		College college = new College(screen, CollegeMetadata.ALCUIN, 0, null);
		// destroying the college
		Hud.health = 0;
		college.setToDestroy = true;
		college.update(0);

		// call to check if the death screen is displayed
		screen.gameOverCheck();
		assertEquals(screen.isGameRunning(), false);
	}

	/**
	 * test to see if ships are added correctly
	 * TEST ID: UT_6
	 */
	@Test
	public void testShipAddedToCollege() {
		GameScreen screen = mockedGameScreen;
		College college = new College(screen, CollegeMetadata.ALCUIN, 1, new AvailableSpawn());
		Assert.assertFalse(college.fleet.isEmpty());
	}

	/**
	 * test to see if the college takes damage upon contact
	 * TEST ID: UT_7
	 */
	@Test
	public void testOnContact() {
		GameScreen screen = mockedGameScreen;
		College college = new College(screen, CollegeMetadata.ALCUIN, 0, null);
		float oldHealth = college.health;
		college.onContact();
		float newHealth = college.health;
		Assert.assertNotEquals(oldHealth, newHealth);
	}

	/**
	 * test to see if a cannonball is spawned when the fired method is called
	 * TEST ID: UT_8
	 */
	@Ignore
	public void testFireCannonball() {
		GameScreen screen = mockedGameScreen;
		College college = new College(screen, CollegeMetadata.ALCUIN, 0, null);
		int oldCannonballCount = college.cannonBalls.size;
		college.fire();
		int newCannonballCount = college.cannonBalls.size;
		Assert.assertNotEquals(oldCannonballCount, newCannonballCount);
	}
}
