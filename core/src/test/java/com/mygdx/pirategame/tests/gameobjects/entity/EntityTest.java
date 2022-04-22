package com.mygdx.pirategame.tests.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pirategame.MockClass;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.tests.FakeGL20;
import com.mygdx.pirategame.world.WorldCreator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class EntityTest {
    /*private static GameScreen mockedGameScreen;

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

    *//**
     * Tests the creation of the object, as the object does not have any methods
     * this is all that needs covering
     *//*
    @Test(expected = Test.None.class)
    public void testInstantiation() {
        new WorldCreator(mockedGameScreen);
    }*/
}
