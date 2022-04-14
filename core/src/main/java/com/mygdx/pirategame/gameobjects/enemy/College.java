package com.mygdx.pirategame.gameobjects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.CollegeFire;
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.world.AvailableSpawn;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Random;

/**
 * College
 * Class to generate the enemy entity college
 * Instantiates colleges
 * Instantiates college fleets
 *
 * @author Ethan Alabaster, Edward Poulter
 * @version 1.0
 */

public class College extends Enemy {
    private final Texture enemyCollege;
    public Random rand = new Random();
    private final Array<CollegeFire> cannonBalls;
    private final AvailableSpawn noSpawn;
    public ArrayList<EnemyShip> fleet = new ArrayList<>();
    private final Sound cannonballHitSound;
    private final CollegeMetadata collegeMeta;
    private final String flag;
    private final String ship;

    /**
     * @param screen       Visual data
     * @param collegeMeta  To identify college, used for fleet assignment
     * @param ship_no      Number of college ships to produce
     * @param invalidSpawn Spawn data to check spawn validity when generating ships
     */
    public College(GameScreen screen, CollegeMetadata collegeMeta, int ship_no, AvailableSpawn invalidSpawn) {
        super(screen, collegeMeta.getX(), collegeMeta.getY());
        this.screen = screen;
        this.collegeMeta = collegeMeta;

        String college = collegeMeta.getFilePath();
        flag = "college/Flags/" + college + "_flag.png";
        ship = "college/Ships/" + college + "_ship.png";

        noSpawn = invalidSpawn;
        enemyCollege = new Texture(flag);
        //Set the position and size of the college
        setBounds(0, 0, 64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(enemyCollege);
        setOrigin(32 / PirateGame.PPM, 55 / PirateGame.PPM);
        damage = 5 * screen.difficulty;
        cannonBalls = new Array<>();
        int ranX = 0;
        int ranY = 0;
        boolean spawnIsValid;

        //Generates college fleet
        for (int i = 0; i < ship_no; i++) {
            spawnIsValid = false;
            while (!spawnIsValid) {
                ranX = rand.nextInt(2000) - 1000;
                ranY = rand.nextInt(2000) - 1000;
                ranX = (int) Math.floor(collegeMeta.getX() + (ranX / PirateGame.PPM));
                ranY = (int) Math.floor(collegeMeta.getY() + (ranY / PirateGame.PPM));
                spawnIsValid = getCoord(ranX, ranY);
            }
            fleet.add(new EnemyShip(screen, ranX, ranY, ship, collegeMeta));
        }

        // explosion sound effect
        cannonballHitSound = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/explode.mp3"));
    }

    /**
     * Used to load college information from file
     *
     * @param screen       Visual data
     * @param collegeMeta  To identify college, used for fleet assignment
     * @param element      The element storing college information
     * @param invalidSpawn Spawn data to check spawn validity when generating ships
     */
    public College(GameScreen screen, CollegeMetadata collegeMeta, Element element, AvailableSpawn invalidSpawn) {
        super(screen, element);
        this.screen = screen;
        this.collegeMeta = collegeMeta;

        String college = collegeMeta.getFilePath();
        flag = "college/Flags/" + college + "_flag.png";
        ship = "college/Ships/" + college + "_ship.png";

        noSpawn = invalidSpawn;
        enemyCollege = new Texture(flag);
        //Set the position and size of the college
        setBounds(0, 0, 64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(enemyCollege);
        setOrigin(32 / PirateGame.PPM, 55 / PirateGame.PPM);
        damage = 5 * screen.difficulty;
        cannonBalls = new Array<>();

        //Generates college fleet
        NodeList shipList = element.getElementsByTagName("ship");
        for (int i = 0; i < shipList.getLength(); i++) {
            fleet.add(new EnemyShip(screen, ship, ((Element) shipList.item(i)), collegeMeta));
        }

        // explosion sound effect
        cannonballHitSound = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/explode.mp3"));
    }

    /**
     * Checks ship spawning in at a valid location
     *
     * @param x x position to test
     * @param y y position to test
     * @return isValid : returns the validity of the proposed spawn point
     */
    public boolean getCoord(int x, int y) {
        if (x < AvailableSpawn.xBase || x >= AvailableSpawn.xCap || y < AvailableSpawn.yBase || y >= AvailableSpawn.yCap) {
            return false;
        } else if (noSpawn.tileBlocked.containsKey(x)) {
            return !noSpawn.tileBlocked.get(x).contains(y);
        }
        return true;
    }

    /**
     * Updates the state of each object with delta time
     * Checks for college destruction
     * Checks for cannon fire
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt) {
        //If college is set to destroy and isn't, destroy it
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;

            //If it is the player ally college, end the game for the player
            if (collegeMeta == CollegeMetadata.ALCUIN) {
                screen.gameOverCheck();
            } else {
                //Award the player coins and points for destroying a college
                Hud.changePoints(100);
                Hud.changeCoins(rand.nextInt(10));
            }
        }
        //If not destroyed, update the college position
        else if (!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);

        }
        if (health <= 0) {
            setToDestroy = true;
        }
        bar.update();
        if (health <= 0) {
            setToDestroy = true;
        }
        //Update cannon balls
        for (CollegeFire ball : cannonBalls) {
            ball.update(dt);
            if (ball.isDestroyed())
                cannonBalls.removeValue(ball, true);
        }
    }

    @Override
    protected void saveChild(Document document, Element element) {

        for(EnemyShip ship : fleet) {
            Element shipEle = document.createElement("ship");
            ship.save(document, shipEle);
            element.appendChild(shipEle);
        }

    }

    /**
     * Draws the batch of cannonballs
     */
    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
            //Render health bar
            bar.render(batch);
            //Render balls
            for (CollegeFire ball : cannonBalls)
                ball.draw(batch);
        }
    }

    /**
     * Sets the data to define a college as an enemy
     */
    @Override
    protected void defineEnemy() {
        //sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        //Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(55 / PirateGame.PPM);
        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.COLLEGE_SENSOR_BIT;
        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.PLAYER_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Contact detection
     * Allows for the college to take damage
     */
    @Override
    public void onContact() {
        //Damage the college and lower health bar
        Gdx.app.log("enemy", "collision");
        health -= damage;
        bar.changeHealth(damage);

        // Plays explosion sound effect
        if (GameScreen.game.getPreferences().isEffectsEnabled()) {
            cannonballHitSound.play(GameScreen.game.getPreferences().getEffectsVolume());
        }
    }

    @Override
    public void onContactOther() {
        // nothing to do here
    }

    /**
     * Fires cannonballs
     */
    public void fire() {
        cannonBalls.add(new CollegeFire(screen, b2body.getPosition().x, b2body.getPosition().y));
    }

    public CollegeMetadata getMetaData() {
        return collegeMeta;
    }
}

