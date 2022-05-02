package com.mygdx.pirategame.tests.pathfinding.pathManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.PirateGameTest;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.pathfinding.PathFinder;
import com.mygdx.pirategame.pathfinding.pathManager.AttackPath;
import com.mygdx.pirategame.pathfinding.pathManager.RandomPath;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;

/**
 * Testing the attackPath class
 * @author James McNair
 * TEST ID: UT_34
 */
@RunWith(PirateGameTest.class)
public class AttackPathTest {

    private static GameScreen mockedGameScreen;

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

        mockedGameScreen = MockClass.mockGameScreenWithPlayer();
    }


    /**
     * Ensuring the random path generates a random destination
     */
    @Test
    public void testPatrolPathGeneration() {
        // running the test 20 times to account for randomness
        for (int i = 0; i < 20; i++) {

            String college = CollegeMetadata.ALCUIN.getFilePath();
            String shipPath = "college/Ships/" + college + "_ship.png";
            EnemyShip ship = new EnemyShip(mockedGameScreen, 13 * 64 / PirateGame.PPM, 11 * 64 / PirateGame.PPM,
                    shipPath, CollegeMetadata.ALCUIN);

            AttackPath path = new AttackPath(new RandomPath(ship, mockedGameScreen), ship, mockedGameScreen);

            // generating destination
            Vector2 dest = path.generateDestination();

            // ensuring the destination is valid
            PathFinder pathFinder = new PathFinder(mockedGameScreen, 64);

            Assert.assertTrue(pathFinder.isTraversable(dest.x, dest.y, 1, 1));
        }

    }


}
