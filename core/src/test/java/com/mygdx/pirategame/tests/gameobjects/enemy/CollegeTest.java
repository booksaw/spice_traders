package com.mygdx.pirategame.tests.gameobjects.enemy;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.enemy.College;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.screen.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;

@RunWith(PirateGameTest.class)
public class CollegeTest {

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
	 * test to ensure points increase when a college is destroyed (when college is
	 * not owned by player)
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
		assertEquals(screen.GameRunning(), false);
	}

}
