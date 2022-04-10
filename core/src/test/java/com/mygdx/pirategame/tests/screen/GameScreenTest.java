package com.mygdx.pirategame.tests.screen;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.mygdx.pirategame.screen.GameScreen;

public class GameScreenTest {
	
    @Test
    public void testDebugToolsDisabled() {
        assertFalse(GameScreen.PHYSICSDEBUG);
    }

}
