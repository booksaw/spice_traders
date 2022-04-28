package com.mygdx.pirategame.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.PirateGameTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * This class is used to target the class pirategame with scaling tests
 * @author James McNair, Marc Perales Salomo
 */
@RunWith(PirateGameTest.class)
public class PirateGameTestScaling {

    /**
     * Setting up before running this test class
     */
    @BeforeClass
    public static void init() {
        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;

        // Mock the graphics class.
        Gdx.graphics = Mockito.mock(Graphics.class);
        when(Gdx.graphics.getWidth()).thenReturn(500);
        when(Gdx.graphics.getHeight()).thenReturn(500);
    }

    /**
     * Used to test if the pirategame.getScaledLocaiton provides correct results
     */
    @Test
    public void testScaling() {

        OrthographicCamera camera = new OrthographicCamera();
        camera.viewportWidth = 500;
        camera.viewportHeight = 500;

        // checking on static location
        assertEquals(PirateGame.getScaledLocation(new Vector2(50.1f, 50.1f), camera), new Vector2(50.1f, 50.1f));

        camera.viewportWidth = 100;
        camera.viewportHeight = 100;
        // checking when scaling is required
        assertEquals(PirateGame.getScaledLocation(new Vector2(50, 50), camera), new Vector2(10, 10));
    }

}