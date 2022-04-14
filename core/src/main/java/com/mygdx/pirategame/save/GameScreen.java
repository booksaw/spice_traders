package com.mygdx.pirategame.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.gameobjects.enemy.College;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.gameobjects.enemy.SeaMonster;
import com.mygdx.pirategame.gameobjects.entity.Coin;
import com.mygdx.pirategame.pathfinding.PathFinder;

import com.mygdx.pirategame.gameobjects.entity.*;

import com.mygdx.pirategame.screen.GoldShop;
import com.mygdx.pirategame.screen.MainMenu;
import com.mygdx.pirategame.screen.OptionsScreen;
import com.mygdx.pirategame.world.AvailableSpawn;
import com.mygdx.pirategame.world.WorldContactListener;
import com.mygdx.pirategame.world.WorldCreator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;


/**
 * Game Screen
 * Class to generate the various screens used to play the game.
 * Instantiates all screen types and displays current screen.
 *
 * @author Ethan Alabaster, Adam Crook, Joe Dickinson, Sam Pearson, Tom Perry, Edward Poulter
 * @version 1.0
 */
public class GameScreen implements Screen {

    /**
     * Tracks if physics debugging tools should be enabled at runtime
     */
    public final static boolean PHYSICSDEBUG = false;

    private static float maxSpeed = 4f;
    private static float accel = 0.1f;
    private static float shootingDelay = 0.5f;
    private float stateTime;
    private float timeTornado;

    public static PirateGame game;
    private final OrthographicCamera camera;
    public final FitViewport viewport;
    private final Stage stage;

    private final TmxMapLoader maploader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world;
    private final Box2DDebugRenderer b2dr;

    Player player;
    private static HashMap<CollegeMetadata, College> colleges = new HashMap<>();
    private static ArrayList<EnemyShip> ships = new ArrayList<>();
    private static ArrayList<SeaMonster> monsters = new ArrayList<>();
    private static ArrayList<Coin> Coins = new ArrayList<>();
    private static ArrayList<PowerUp> PowerUps = new ArrayList<>();
    private static ArrayList<Tornado> Tornados = new ArrayList<>();

    private final AvailableSpawn invalidSpawn = new AvailableSpawn();
    private final Hud hud;

    public static final int GAME_RUNNING = 0;
    public static final int GAME_PAUSED = 1;
    public static final int GOLD_SHOP = 2;
    public static int gameStatus;

    private final Texture tutorialTexture;
    private final Sprite tutorials;

    private final PathFinder pathFinder;

    private Table pauseTable;
    private Table table;

    public Random rand = new Random();

    private Integer attackingCollege;
    private final List<Integer> collegesLeft = new LinkedList<Integer>(Arrays.asList(1, 2, 3));
    private final Random collegeRand = new Random();

    private GoldShop goldShop;
    private static Label shopLabel;
    private final SaveLoader loadManager;

    public float difficulty;

    /**
     * Initialises the Game Screen,
     * generates the world data and data for entities that exist upon it,
     *
     * @param game passes game data to current class,
     */
    public GameScreen(PirateGame game, SaveLoader loadManager) {
        gameStatus = GAME_RUNNING;
        GameScreen.game = game;
        this.loadManager = loadManager;
        // Initialising camera and extendable viewport for viewing game
        camera = new OrthographicCamera();
        camera.zoom = 0.0155f;
        viewport = new FitViewport(1280, 720, camera);
        camera.position.set(viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 3, 0);

        // set the difficulty of the game
        difficulty = game.DIFFICULTY;

        // Initialize a hud
        hud = new Hud(game.batch);

        // Initialising box2d physics
        world = new World(new Vector2(0, 0), true);
        if (PHYSICSDEBUG) {
            b2dr = new Box2DDebugRenderer();
        } else {
            b2dr = null;
        }

        // making the Tiled tmx file render as a map
        maploader = new TmxMapLoader();
        map = maploader.load("map/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, getUnitScale());
        pathFinder = new PathFinder(this, 64);

        new WorldCreator(this);

        // stores tutorial texture
        tutorialTexture = new Texture("Tutorial.png");
        tutorials = new Sprite(tutorialTexture);

        // Setting up contact listener for collisions
        world.setContactListener(new WorldContactListener());

        // Spawning enemy ship and coin. x and y is spawn location
        colleges = new HashMap<>();

        ships = new ArrayList<>();
        monsters = new ArrayList<>();
        Coins = new ArrayList<>();

        loadManager.load(this);

        /*
        //Random tornado
        Tornados = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pos = getRandomLocation();
            //Add a tornado at the random coords
            Tornados.add(new Tornado(this, pos[0], pos[1]));
        }
        */

        //Setting stage
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Randomly generates x and y and checks if they are valid
     *
     * @return x, y
     */
    public int[] getRandomLocation() {
        Boolean validLoc = false;
        int x = 0, y = 0;
        while (!validLoc) {
            //Get random x and y coords
            x = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
            y = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;
            validLoc = checkGenPos(x, y);
        }
        return new int[]{x, y};
    }

    /**
     * Randomly positions power ups around the sea
     */
    public void addPowerUps() {
        //Random powerups
        PowerUps = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            //Add a powerup at the random coords
            int[] loc = getRandomLocation();

            int select = i % 5;
            // Iterates through to add each power up
            if (select == 0) {
                PowerUps.add(new AbsorptionHeart(this, loc[0], loc[1]));
            } else if (select == 1) {
                PowerUps.add(new SpeedBoost(this, loc[0], loc[1]));
            } else if (select == 2) {
                PowerUps.add(new FasterShooting(this, loc[0], loc[1]));
            } else if (select == 3) {
                PowerUps.add(new CoinMagnet(this, loc[0], loc[1]));
            } else {
                PowerUps.add(new FreezeEnemy(this, loc[0], loc[1]));
            }
        }
    }

    /**
     * Returns the player object
     *
     * @return player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the array of coins in the level
     *
     * @return coin array
     */
    public ArrayList<Coin> getCoins() {
        return Coins;
    }

    /**
     * Makes this the current screen for the game.
     * Generates the buttons to be able to interact with what screen is being displayed.
     * Creates the escape menu and pause button
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //GAME BUTTONS
        final TextButton pauseButton = new TextButton("Pause", skin);
        final TextButton skill = new TextButton("Skill Tree", skin);

        //PAUSE MENU BUTTONS
        final TextButton start = new TextButton("Resume", skin);
        final TextButton options = new TextButton("Options", skin);
        final TextButton save = new TextButton("Save", skin);
        TextButton exit = new TextButton("Exit", skin);

        //Create main table and pause tables
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        pauseTable = new Table();
        pauseTable.setFillParent(true);
        stage.addActor(pauseTable);

        //Set the visibility of the tables. Particularly used when coming back from options or skillTree
        if (gameStatus == GAME_PAUSED) {
            table.setVisible(false);
            pauseTable.setVisible(true);
        } else {
            pauseTable.setVisible(false);
            table.setVisible(true);
        }

        //ADD TO TABLES
        table.add(pauseButton);
        table.row().pad(10, 0, 10, 0);
        table.left().top();

        pauseTable.add(start).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(skill).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(save).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(options).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(exit).fillX().uniformX();
        pauseTable.center();


        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.setVisible(false);
                pauseTable.setVisible(true);
                pause();

            }
        });
        skill.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(false);
                game.changeScreen(PirateGame.SKILL);
            }
        });
        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(false);
                table.setVisible(true);
                resume();
            }
        });
        options.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    pauseTable.setVisible(false);
                                    game.setScreen(new OptionsScreen(game, game.getScreen()));
                                }
                            }
        );
        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Game File (.spice)", "spice");
                fc.setFileFilter(filter);
                int returnVal = fc.showSaveDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null){
                    File f = fc.getSelectedFile();
                    if(!f.getPath().endsWith(".spice")){
                        f = new File(f.getPath() + ".spice");
                    }
                    loadManager.save(GameScreen.this, f);
                }

            }
        });
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(PirateGame.MENU);
            }
        });
    }

    /**
     * Checks for input and performs an action
     * Applies to key "W" "A" "S" "D" "E" "Esc" "Left" "Right" "Up" "Down"
     * <p>
     * Caps player velocity
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void handleInput(float dt) {
        // Disable movement and firing if in the gold shop
        if (gameStatus == GAME_RUNNING && goldShop == null) {
            // Left physics impulse on 'A'
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.b2body.applyLinearImpulse(new Vector2(-accel, 0), player.b2body.getWorldCenter(), true);
            }
            // Right physics impulse on 'D'
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.b2body.applyLinearImpulse(new Vector2(accel, 0), player.b2body.getWorldCenter(), true);
            }
            // Up physics impulse on 'W'
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player.b2body.applyLinearImpulse(new Vector2(0, accel), player.b2body.getWorldCenter(), true);
            }
            // Down physics impulse on 'S'
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                player.b2body.applyLinearImpulse(new Vector2(0, -accel), player.b2body.getWorldCenter(), true);
            }
            // Checking if player at max velocity, and keeping them below max
            if (player.b2body.getLinearVelocity().x >= maxSpeed) {
                player.b2body.applyLinearImpulse(new Vector2(-accel, 0), player.b2body.getWorldCenter(), true);
            }
            if (player.b2body.getLinearVelocity().x <= -maxSpeed) {
                player.b2body.applyLinearImpulse(new Vector2(accel, 0), player.b2body.getWorldCenter(), true);
            }
            if (player.b2body.getLinearVelocity().y >= maxSpeed) {
                player.b2body.applyLinearImpulse(new Vector2(0, -accel), player.b2body.getWorldCenter(), true);
            }
            if (player.b2body.getLinearVelocity().y <= -maxSpeed) {
                player.b2body.applyLinearImpulse(new Vector2(0, accel), player.b2body.getWorldCenter(), true);
            }
            // Firing Code, when left mouse is pressed
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                player.fire(camera);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (gameStatus != GOLD_SHOP) {
                if (gameStatus == GAME_PAUSED) {
                    resume();
                    table.setVisible(true);
                    pauseTable.setVisible(false);
                } else {
                    table.setVisible(false);
                    pauseTable.setVisible(true);
                    pause();
                }
            }
        }

        Body body = getPlayer().b2body;
        Vector2 position = body.getPosition();
        shopLabel = new Label("Press \"E\" to enter the Gold Shop", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Table table1 = new Table(); // Shop text
        table1.add(shopLabel).padTop(20).top();
        table1.top();
        table1.setFillParent(true);
        for (Map.Entry<CollegeMetadata, College> college : colleges.entrySet()) {
            if (college.getValue().getMetaData().isPlayer()) {
                float distance = position.dst(college.getValue().getMetaData().getCentrePosition());
                if (distance < college.getValue().getMetaData().getDistance()) {
                    //System.out.println(distance);
                    shopLabel.setVisible(true);

                    if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                        if (gameStatus == GOLD_SHOP) {
                            closeShop();
                        } else if (gameStatus == GAME_RUNNING) {
                            openShop();
                        }
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                        if (gameStatus == GOLD_SHOP) {
                            closeShop();
                        }
                    }
                } else {

                    shopLabel.setVisible(false);
                }
            }
        }
        stage.addActor(table1);
    }

    /**
     * Updates the state of each object with delta time
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt) {
        stateTime += dt;
        handleInput(dt);
        // Stepping the physics engine by time of 1 frame
        world.step(1 / 60f, 6, 2);

        // Update all players and entities
        player.update(dt);

        for (CollegeMetadata college : CollegeMetadata.values()) {
            getCollege(college).update(dt);
        }

        // space bar removes tutorial screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            hideTutorial();
        }

        // Clears gold shop instance
        if (goldShop != null && !goldShop.display) {
            goldShop = null;
        }

        // centers tutorial screen
        tutorials.setPosition(camera.position.x - (tutorials.getWidth() / 2), camera.position.y - (tutorials.getHeight() / 2));
        // scales the sprite depending on window size divided by a constant
        tutorials.setSize(camera.viewportWidth / 100f, camera.viewportHeight / 100f);

        //Update ships
        for (int i = 0; i < ships.size(); i++) {
            ships.get(i).update(dt);
        }

        //Update ships
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).update(dt);
        }

        //Updates coin
        for (int i = 0; i < Coins.size(); i++) {
            Coins.get(i).update();
        }
        //Updates powerups
        for (int i = 0; i < PowerUps.size(); i++) {
            PowerUps.get(i).update();
        }

        //Updates tornados
        for (int i = 0; i < Tornados.size(); i++) {
            Tornados.get(i).update(dt);
        }

        //After a delay check if a college is destroyed. If not, if can fire
        if (stateTime > 1) {
            for (CollegeMetadata college : CollegeMetadata.values()) {
                if (!college.isPlayer() && !getCollege(college).destroyed) {
                    getCollege(college).fire();
                }
            }
            stateTime = 0;
        }

        timeTornado += dt;
        // Once it has been 60 seconds, release a tornado
        if (timeTornado > 60) {
            // Release a tornado
            int[] loc = getRandomLocation();
            // Add a tornado at the random location
            Tornados.add(new Tornado(this, loc[0], loc[1]));
            System.out.println("Tornado Released");
            // Reset timer to 0, every 60 seconds a tornado is released
            timeTornado = 0;
        }

        hud.update(dt);

        // Centre camera on player boat
        camera.position.x = player.b2body.getPosition().x;
        camera.position.y = player.b2body.getPosition().y;
        camera.update();
        renderer.setView(camera);
    }

    /**
     * Used to hide the tutorial screen
     */
    public void hideTutorial() {
        tutorials.setAlpha(0);
        tutorialTexture.dispose();
    }

    /**
     * Renders the visual data for all objects
     * Changes and renders new visual data for ships
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    @Override
    public void render(float dt) {
        if (gameStatus == GAME_RUNNING) {
            update(dt);
        } else {
            handleInput(dt);
        }

        Gdx.gl.glClearColor(46 / 255f, 204 / 255f, 113 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        if(PHYSICSDEBUG) {
            // b2dr is the hitbox shapes, can be commented out to hide
            b2dr.render(world, camera.combined);
        }

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // Order determines layering

        //Renders coins
        for (int i = 0; i < Coins.size(); i++) {
            Coins.get(i).draw(game.batch);
        }

        //Renders powerups
        for (int i = 0; i < PowerUps.size(); i++) {
            PowerUps.get(i).draw(game.batch);
        }

        //Renders tornados
        for (int i = 0; i < Tornados.size(); i++) {
            Tornados.get(i).draw(game.batch);
        }

        //Renders colleges
        player.draw(game.batch);
        for (Map.Entry<CollegeMetadata, College> college : colleges.entrySet()) {
            college.getValue().draw(game.batch);
        }
        /*
        // Set image
        Texture point = new Texture("entity/cannonball.png");
        Sprite circle = new Sprite(point);

        circle.setPosition(6200 / PirateGame.PPM, 1100 / PirateGame.PPM);
        // scales the sprite depending on window size divided by a constant
        circle.setSize(100 / PirateGame.PPM, 100 / PirateGame.PPM);

        circle.draw(game.batch);

         */

        //Updates all ships
        for (int i = 0; i < ships.size(); i++) {
            // if the ship is in a college
            if (ships.get(i).collegeMeta != null) {
                //Flips a colleges allegiance if their college is destroyed
                if (getCollege(ships.get(i).collegeMeta).destroyed) {

                    ships.get(i).updateTexture(0, "college/Ships/alcuin_ship.png");
                }
            }
            ships.get(i).draw(game.batch);
        }

        //Update ships
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).draw(game.batch);
        }

        // draw the gold shop if it is open
        if (goldShop != null && gameStatus == GOLD_SHOP) {

            goldShop.render(Gdx.graphics.getDeltaTime());
        }

        // show tutorial screen
        tutorials.draw(game.batch);

        game.batch.end();
        Hud.stage.draw();
        stage.act();
        stage.draw();
        //Checks game over conditions
        gameOverCheck();
    }

    /**
     * Changes the camera size, Scales the hud to match the camera
     *
     * @param width  the width of the viewable area
     * @param height the height of the viewable area
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
        Hud.resize(width, height);
        camera.update();
        renderer.setView(camera);
    }

    /**
     * Returns the map
     *
     * @return map : returns the world map
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * @return The tile map width in in-game pixels
     */
    public int getTileMapWidth() {
        MapProperties prop = getMap().getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);

        return mapWidth * tilePixelWidth;
    }

    /**
     * @return the width of a single tile on the tilemap
     */
    public int getTileWidth() {
        MapProperties prop = getMap().getProperties();
        int tilePixelWidth = prop.get("tilewidth", Integer.class);

        return tilePixelWidth;
    }

    /**
     * @return The tile map height in in-game pixels
     */
    public int getTileMapHeight() {
        MapProperties prop = getMap().getProperties();
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        return mapHeight * tilePixelHeight;
    }

    /**
     * Returns the world (map and objects)
     *
     * @return world : returns the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the college from the colleges hashmap
     *
     * @param collegeID uses a collegeID as an index
     * @return college : returns the college fetched from colleges
     * @deprecated use CollegeMetadata instead of collegeID
     */
    @Deprecated
    public College getCollege(Integer collegeID) {
        return getCollege(CollegeMetadata.getCollegeMetaFromId(collegeID));
    }

    /**
     * Returns the college from the colleges hashmap
     *
     * @param college uses the collegeMetadata to find the college
     * @return returns the college fetched from colleges
     */
    public College getCollege(CollegeMetadata college) {
        return colleges.get(college);
    }

    /**
     * When called, finds the next college that the player can attack
     */
    public void nextCollege() {
        // Selects, at random, an index from length of collegeLeft list
        Integer collegeIndex = collegeRand.nextInt(collegesLeft.size());
        // Gets the collegeID from the collegeLeft list
        attackingCollege = collegesLeft.get(collegeIndex);
        // Removes the college selected from the collegeLeft list
        collegesLeft.remove(collegeIndex);
    }

    /**
     * Returns the college which can currently be attacked
     *
     * @return integer : returns CollegeID
     */
    public Integer getAttackingCollege() {
        return attackingCollege;
    }

    /**
     * Checks if the game is over
     * i.e. goal reached (all colleges bar 0 are destroyed)
     */
    public void gameOverCheck() {
        //Lose game if ship on 0 health or Alcuin is destroyed
        if (Hud.getHealth() <= 0 || getCollege(CollegeMetadata.ALCUIN).destroyed) {
            game.changeScreen(PirateGame.DEATH);
            game.killGame();
            game.resetValues();
        }
        //Win game if all colleges destroyed
        else if (getCollege(CollegeMetadata.ANNELISTER).destroyed && getCollege(CollegeMetadata.CONSTANTINE).destroyed && getCollege(CollegeMetadata.GOODRICKE).destroyed) {
            game.changeScreen(PirateGame.VICTORY);
            game.killGame();
            game.resetValues();
        }
    }

    /**
     * Fetches the player's current position
     *
     * @return position vector : returns the position of the player
     */
    public Vector2 getPlayerPos() {
        return new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);
    }

    /**
     * Calculates the players position centered in the middle of the player
     *
     * @return The centered position of the player
     */
    public Vector2 getCenteredPlayerPos() {
        return getPlayerPos().add(player.getWidth(), player.getHeight());
    }

    /**
     * Updates acceleration by a given percentage. Accessed by skill tree and power ups
     *
     * @param percentage percentage increase
     */
    public static void changeAcceleration(Float percentage) {
        accel = accel * (1 + (percentage / 100));
    }

    /**
     * Sets acceleration to a given value
     *
     * @param value new acceleration value
     */
    public static void setAcceleration(Float value) {
        accel = value;
    }

    /**
     * Updates max speed by a given percentage. Accessed by skill tree
     *
     * @param percentage percentage increase
     */
    public static void changeMaxSpeed(Float percentage) {
        maxSpeed = maxSpeed * (1 + (percentage / 100));
    }

    /**
     * Sets max speed to a given value
     *
     * @param value new max speed value
     */
    public static void setMaxSpeed(Float value) {
        maxSpeed = value;
    }



    /**
     * Fetches the current shooting delay
     *
     * @return shooting delay : returns the current shooting delay value
     */
    public static float getShootingDelay() {
        return shootingDelay;
    }

    /**
     * Sets shooting delay to a given value
     *
     * @param value new shooting delay value
     */
    public static void setShootingDelay(Float value) {
        shootingDelay = value;
    }

    /**
     * Updates shooting delay by a given percentage. Accessed by power ups
     *
     * @param percentage percentage decrease
     */
    public static void changeShootingDelay(Float percentage) {
        shootingDelay = shootingDelay * (1 - (percentage / 100));
    }

    /**
     * Changes the amount of damage done by each hit. Accessed by skill tree
     *
     * @param value damage dealt
     */
    public static void changeDamage(int value) {
        for (int i = 0; i < ships.size(); i++) {
            ships.get(i).changeDamageReceived(value);
        }

        for (Map.Entry<CollegeMetadata, College> college : colleges.entrySet()) {
            college.getValue().changeDamageReceived(value);
        }

    }

    /**
     * Tests validity of randomly generated position
     *
     * @param x random x value
     * @param y random y value
     */
    private Boolean checkGenPos(int x, int y) {
        if (invalidSpawn.tileBlocked.containsKey(x)) {
            ArrayList<Integer> yTest = invalidSpawn.tileBlocked.get(x);
            return !yTest.contains(y);
        }
        return true;
    }

    /**
     * Pauses game
     */
    @Override
    public void pause() {
        this.gameStatus = GAME_PAUSED;
    }

    /**
     * Resumes game
     */
    @Override
    public void resume() {
        this.gameStatus = GAME_RUNNING;
    }

    /**
     * Opens gold shop
     */
    public void openShop() {


        goldShop = new GoldShop(GameScreen.game, camera, this);
        goldShop.show();
        //table.setVisible(false);
        gameStatus = GOLD_SHOP;

    }

    /**
     * Closes gold shop
     */

    public void closeShop() {
        goldShop = new GoldShop(GameScreen.game, camera, this);
        goldShop.dispose();
        resume();
        //table.setVisible(true);

    }

    /**
     * Finds the nearest Tornado to the Player
     *
     * @return The nearest Tornado
     */
    public static Tornado getNearestTornado() {
        int nearest = 0;
        double nearestDistance = 100000;
        for (int i = 0; i < Tornados.size(); i++) {
            double currentDistance = Tornados.get(i).getDistance();
            if (currentDistance < nearestDistance) {
                nearest = i;
                nearestDistance = currentDistance;
            }
        }
        return Tornados.get(nearest);
    }

    /**
     * (Not Used)
     * Hides game
     */
    @Override
    public void hide() {

    }

    /**
     * Disposes game data
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        hud.dispose();
        stage.dispose();
        if(PHYSICSDEBUG) {
            b2dr.dispose();
        }
    }


    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public float getUnitScale() {
        return 1 / PirateGame.PPM;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }

    public AvailableSpawn getInvalidSpawn() {
        return invalidSpawn;
    }

    public HashMap<CollegeMetadata, College> getColleges() {
        return colleges;

    }

    public ArrayList<EnemyShip> getEnemyShips(){
        return ships;
    }

    /**
     * Get Hud object
     * @return Hud object which handles scores, coins etc.
     */
    public Hud getHud(){
        return hud;
    }

    public List<SeaMonster> getMonsters() {
        return monsters;
    }
}
