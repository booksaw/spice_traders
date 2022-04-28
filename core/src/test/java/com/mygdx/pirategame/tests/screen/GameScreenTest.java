package com.mygdx.pirategame.tests.screen;

import com.mygdx.pirategame.save.GameScreen;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Tests for the GameScreen class
 * @author James McNair
 */
public class GameScreenTest {

    /**
     * Test to ensure the debug tools are disabled
     */
    @Test
    public void testDebugToolsDisabled() {
        assertFalse(GameScreen.PHYSICSDEBUG);
    }

}
