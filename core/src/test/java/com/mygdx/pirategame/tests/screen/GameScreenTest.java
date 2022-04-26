package com.mygdx.pirategame.tests.screen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.save.NewGameSaveLoader;
import com.mygdx.pirategame.tests.FakeGL20;

@RunWith(PirateGameTest.class)
public class GameScreenTest {
	
	// stopping some tests timeing out by increasing the timeout timer
	@Rule
    public Timeout imeout = new Timeout(100, TimeUnit.SECONDS);
	
	private static PirateGame mockedPirateGame;

	@BeforeClass
	public static void init() {
		// Use Mockito to mock the OpenGL methods since we are running headlessly
		Gdx.gl20 = Mockito.mock(GL20.class);
		Gdx.gl = new FakeGL20();

		// note all mocking cannot appear in a @Test annotated method
		// or the mocking will not work, all mocking must occur in @BeforeClass
		// at least from my testing it does not even work in a @Before method
		mockedPirateGame = MockClass.mockPirateGame();
	}

	@Test
	public void testDebugToolsDisabled() {
		assertFalse(GameScreen.PHYSICSDEBUG);
	}
	
	@Test(timeout = 10000)
	public void testConstructor() { 
		new GameScreen(mockedPirateGame, new NewGameSaveLoader());
		
		assertTrue(true);
	}
	

}
