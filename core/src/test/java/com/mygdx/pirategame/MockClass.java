package com.mygdx.pirategame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.pathfinding.PathFinder;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.screen.GoldShop;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashMap;

/**
 * Useful util methods used to mock specific classes within the game
 * @author James McNair, Dan Wade, Marc Perales Salomo, Charlie Crosley
 */
public class MockClass {

    public static boolean gameStatus = false;

    /**
     * Used to mock the Hud class so all the static methods can be used and init the points and score to 0
     */
    public static void mockHudStatic() {
        Hud hud = Mockito.mock(Hud.class);

        Whitebox.setInternalState(hud, "scoreLabel", new Label(String.format("%03d", 0), new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
        Whitebox.setInternalState(hud, "coinLabel", new Label(String.format("%03d", 0), new Label.LabelStyle(new BitmapFont(), Color.YELLOW)));
        Whitebox.setInternalState(hud, "coinMulti", 1);

        // setting up score and coins to 0
        Hud.setPoints(0);
        Hud.setCoins(0);
        Hud.setHealth(100);
    }

    public static Hud mockHudObject(){
        Hud hud = Mockito.mock(Hud.class);

        Whitebox.setInternalState(hud, "scoreLabel", new Label(String.format("%03d", 0), new Label.LabelStyle(new BitmapFont(), Color.WHITE)));
        Whitebox.setInternalState(hud, "coinLabel", new Label(String.format("%03d", 0), new Label.LabelStyle(new BitmapFont(), Color.YELLOW)));
        Whitebox.setInternalState(hud, "coinMulti", 1);

        // setting up score and coins to 0
        Hud.setPoints(0);
        Hud.setCoins(0);
        Hud.setHealth(100);

        return hud;
    }

    /**
     * Used to mock the PirateGame class with a valid sprite batch
     *
     * @return the created PirateGame instance
     */
    public static PirateGame mockPirateGame() {
        PirateGame game = new PirateGame();
        try {
            game.batch = new ShellSpriteBatch();
            gameStatus = true;
        } catch(Exception e) {
            // do nothing
        }
        return game;
    }



    /**
     * Used to mock the game screen so it can be used in tests
     * @return The created game screen
     */
    public static GameScreen mockGameScreen() {
        // creating required variables, and mocking return values
        GameScreen screen = Mockito.mock(GameScreen.class);
        
        Mockito.when(screen.getWorld()).thenReturn(new World(new Vector2(0, 0), true));

        // mocking the map
        TmxMapLoader mapLoader = new TmxMapLoader();
        Mockito.when(screen.getMap()).thenReturn(mapLoader.load("map/map.tmx"));
        Mockito.when(screen.getTileMapWidth()).thenReturn(128 * 64);
        Mockito.when(screen.getTileMapHeight()).thenReturn(128 * 64);
        Mockito.when(screen.getTileWidth()).thenReturn(64);

        // mocking the path finder
        Mockito.when(screen.getPathFinder()).thenReturn(new PathFinder(screen, 64));

        // mocking the game difficulty
        Mockito.when(screen.getDifficulty()).thenReturn(1f);

        // Mock camera with the same config as the actual game camera
        OrthographicCamera camera = new OrthographicCamera();
        camera.zoom = 0.0155f;
        FitViewport viewport = new FitViewport(1280, 720, camera);
        camera.position.set(viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 3, 0);
        Mockito.when(screen.getCamera()).thenReturn(camera);


        // Mock the power up timers that are displayed in the HUD
        HashMap<String, Float> powerUpTimer = new HashMap<>();
        powerUpTimer.put("absorptionHeart", (float) 0);
        powerUpTimer.put("coinMagnet", (float) 0);
        powerUpTimer.put("fasterShooting", (float) 0);
        powerUpTimer.put("freezeEnemy", (float) 0);
        powerUpTimer.put("speedBoost", (float) 0);
        Mockito.when(screen.getPowerUpTimer()).thenReturn(powerUpTimer);

        return screen;
    }

    /**
     * Used to mock the game screen with the player position also mocked
     * @return the created GameScreen instance
     */
    public static GameScreen mockGameScreenWithPlayer() {
        GameScreen screen = mockGameScreen();

        Mockito.when(screen.getPlayerPos()).thenReturn(new Vector2(13*64 / PirateGame.PPM, 11*64 / PirateGame.PPM));

        Mockito.when(screen.getCenteredPlayerPos()).thenReturn(new Vector2(13*64 / PirateGame.PPM, 11*64 / PirateGame.PPM));

        return screen;
    }

    /**
     * Used to mock the gold shop class
     * @return The created goldShop instance
     */
    public static GoldShop mockGoldShop(){
        GoldShop mockedGoldShop = Mockito.mock(GoldShop.class);

        return mockedGoldShop;
    }

    public boolean getGameStatus() {
        return gameStatus;
    }
}
