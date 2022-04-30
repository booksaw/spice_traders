package com.mygdx.pirategame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.save.NewGameSaveLoader;
import com.mygdx.pirategame.screen.*;


/**
 * The start of the program. Sets up the main backbone of the game.
 * This includes most constants used for collision and changing screens
 * Provides access for screens to interact with each other and the options interface
 * @author Sam Pearson, Robert Murphy, Marc Perales Salomo, James McNair, Charlie Crosley
 * @version 1.0
 */
public class PirateGame extends Game {
	public static final float PPM = 100;

	//Bits used in collisions
	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short COLLEGE_FIRE_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short CANNON_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public static final short COLLEGE_BIT = 64;
	public static final short COLLEGE_SENSOR_BIT = 128;
	public static final short COIN_MAGNET_BIT = 256;
	public static final short SPEED_BOOST_BIT = 512;
	public static final short ABSORPTION_HEART_BIT = 1024;
	public static final short FASTER_SHOOTING_BIT = 2048;
	public static final short FREEZE_ENEMY_BIT = 4096;
	public static final short TORNADO_BIT = 8192;

	public SpriteBatch batch;

	//Variable for each screen
	private MainMenu menuScreen;
	private GameScreen gameScreen;
	private SkillTreeScreen skillTreeScreen;
	private DeathScreen deathScreen;
	private HelpScreen helpScreen;
	private VictoryScreen victoryScreen;

	private audioControls options;
	public Music song;

	//Constant for swapping between screens
	public final static int MENU = 0;
	public final static int GAME = 1;
	public final static int SKILL = 2;
	public final static int DEATH = 3;
	public final static int HELP = 4;
	public final static int VICTORY = 5;

	public boolean gameRunning = true;

	// Constant for selecting difficulty
	public final static float EASY = 2.5f;
	public final static float NORMAL = 1.5f;
	public final static float HARD = 0.7f;
	public float DIFFICULTY = NORMAL;


	/**
	 * Creates the main body of the game.
	 * Establishes the batch for the whole game as well as sets the first screen
	 * Also sets up audio interface
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		//Set starting screen
		MainMenu mainMenu = new MainMenu(this);
		setScreen(mainMenu);
		//Create options
		options = new audioControls();

		//Set background music and play if valid
		song = Gdx.audio.newMusic(Gdx.files.internal("sfx_and_music/pirate-music.mp3"));
		song.setLooping(true);
		if(getPreferences().isMusicEnabled()){
			song.play();
		}
		song.setVolume(getPreferences().getMusicVolume());
	}

	/**
	 * Change the difficulty of the game
	 * @param difficulty
	 */
	public void changeDifficulty(float difficulty) {
		// Ensure that only easy, normal or hard can be selected
		if (difficulty == EASY || difficulty == HARD) {
			DIFFICULTY = difficulty;
		} else {
			DIFFICULTY = NORMAL;
		}
	}

	/**
	 * Changes the screen without killing the prior screen. Allows for the screens to be returned to without making new ones
	 *
	 * @param screen the number of the screen that the user wants to swap to
	 */
	public void changeScreen(int screen) {
		//Depending on which value given, change the screen
		switch (screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MainMenu(this);
				this.setScreen(menuScreen);
				break;

			case GAME:
				if (gameScreen == null) gameScreen = new GameScreen(this, new NewGameSaveLoader(), false);
				if (skillTreeScreen == null) skillTreeScreen = new SkillTreeScreen(this);
				this.setScreen(gameScreen);
				break;

			case SKILL:
				if (skillTreeScreen == null) skillTreeScreen = new SkillTreeScreen(this);
				this.setScreen(skillTreeScreen);
				break;

			case DEATH:
				if (deathScreen == null) deathScreen = new DeathScreen(this);
				gameRunning = false;
				this.setScreen(deathScreen);
				break;

			case HELP:
				if (helpScreen == null) helpScreen = new HelpScreen(this);
				this.setScreen(helpScreen);
				break;

			case VICTORY:
				if (victoryScreen == null) victoryScreen = new VictoryScreen(this);
				this.setScreen(victoryScreen);
				break;
		}//
	}

	public void changeScreen(GameScreen screen){
		this.setScreen(screen);
	}

	/**
	 * Used to scale a location on the screen to a location within game coordinates
	 *
	 * @param point  The point on the screen
	 * @param camera The camera that is in charge of scaling
	 * @return The point translated into in-game coordinates
	 */
	public static Vector2 getScaledLocation(Vector2 point, OrthographicCamera camera) {
		return new Vector2(point.x * (camera.viewportWidth / Gdx.graphics.getWidth()), point.y * (camera.viewportHeight / Gdx.graphics.getHeight()));
	}

	/**
	 * Used to get the in game coordinates of the mouse
	 *
	 * @param camera The camera that is in charge of scaling
	 * @return The location of the mouse pointer in in-game coordinates
	 */
	public static Vector2 getScaledMouseLocation(OrthographicCamera camera) {
		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
		return getScaledLocation(new Vector2(mouseX, mouseY), camera);
	}

	/**
	 * Allows the user to interact with the audio options
	 *
	 * @return the options object
	 */
	public audioControls getPreferences() {
		return this.options;
	}

	/**
	 * Resets game values to default, used on game restart
	 */
	public void resetValues() {
		GameScreen.setMaxSpeed(4f);
		GameScreen.setAcceleration(0.1f);
		GameScreen.setShootingDelay(1f);

		// Turn off power ups
		// TODO: Turn off power ups
	}
	/**
	 * Kills the game screen and skill tree so they can be refreshed on next game start
	 */
	public void killGame(){
		gameScreen = null;
		skillTreeScreen = null;
	}
	
	/**
	 * Kill end screens so they can be made again.
	 */
	public void killEndScreen(){
		deathScreen = null;
		victoryScreen = null;
	}
	/**
	 * (Not Used)
	 * Renders the visual data for all objects
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Disposes game data
	 */
	@Override
	public void dispose () {
		batch.dispose();
	}

	public boolean isGameRunning() {
		return gameRunning;
	}
}
