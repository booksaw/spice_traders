package com.mygdx.pirategame.tests.gameobjects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGame;
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
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Test the college class
 * @author James McNair, Marc Perales Salomo, Charlie Crosley, Dan Wade
 */
@RunWith(PirateGameTest.class)
public class PlayerWinTest{

    private static GameScreen mockedGameScreen;
    private static PirateGame mockedPirateGame;
    private static College AnneLister;
    private static College Constantine;
    private static College Goodricke;
    private static College Alcuin;

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
        mockedPirateGame = MockClass.mockPirateGame();

        Mockito.when(mockedGameScreen.getGame()).thenReturn(mockedPirateGame);

        HashMap<CollegeMetadata, College> colleges = new HashMap<>();

        Alcuin = new College(mockedGameScreen,CollegeMetadata.ALCUIN,0,null);
        AnneLister = new College(mockedGameScreen,CollegeMetadata.ANNELISTER,0,null);
        Constantine = new College(mockedGameScreen, CollegeMetadata.CONSTANTINE,0,null);
        Goodricke = new College(mockedGameScreen, CollegeMetadata.GOODRICKE,0,null);

        colleges.put(CollegeMetadata.ANNELISTER,AnneLister);
        colleges.put(CollegeMetadata.CONSTANTINE,Constantine);
        colleges.put(CollegeMetadata.GOODRICKE,Goodricke);


        Mockito.when(mockedGameScreen.getCollege(CollegeMetadata.ANNELISTER)).thenReturn(AnneLister);
        Mockito.when(mockedGameScreen.getCollege(CollegeMetadata.CONSTANTINE)).thenReturn(Constantine);
        Mockito.when(mockedGameScreen.getCollege(CollegeMetadata.GOODRICKE)).thenReturn(Goodricke);
        Mockito.when(mockedGameScreen.getCollege(CollegeMetadata.ALCUIN)).thenReturn(Alcuin);

        Mockito.doCallRealMethod().when(mockedGameScreen).gameOverCheck();

    }

    /**
     * Test to see the game ends when all colleges are defeated
     */
    @Test
    public void testPlayerWin(){
        // Make sure game is running
        assertEquals(true, mockedPirateGame.isGameRunning());

        AnneLister.setToDestroy = true;
        AnneLister.update(0);
        mockedGameScreen.gameOverCheck();

        // Check game is still running (two colleges remaining)
        assertEquals(true, mockedPirateGame.isGameRunning());

        Constantine.setToDestroy = true;
        Constantine.update(0);
        mockedGameScreen.gameOverCheck();

        // Check game is still running (one college remaining)
        assertEquals(true, mockedPirateGame.isGameRunning());

        // Destroy final college
        Goodricke.setToDestroy = true;
        Goodricke.update(0);
        mockedGameScreen.gameOverCheck();

        // Check game has finished as all colleges are defeated
        assertEquals(false, mockedPirateGame.isGameRunning());


    }
}
