package com.mygdx.pirategame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.save.GameScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Creates the class of the player. Everything that involves actions coming from the player boat
 * @author Ethan Alabaster, Edward Poulter
 * @version 1.0
 */
public class Player extends Sprite {
    private final GameScreen screen;
    private Texture ship;
    public World world;
    public Body b2body;
    private Sound breakSound, cannonballHitSound;
    private Array<CannonFire> cannonBalls;
    private float timeFired = 0;
    private int cannonVelocity = 5;

    public static boolean inTornadoRange = false;

    /**
     * Instantiates a new Player. Constructor only called once per game
     *
     * @param screen visual data
     */
    public Player(GameScreen screen) {
        // Retrieves world data and creates ship texture
        this.screen = screen;
        ship = new Texture("college/Ships/player_ship.png");
        this.world = screen.getWorld();

        // Defines a player, and the players position on screen and world
        definePlayer();
        setBounds(0,0,64 / PirateGame.PPM, 110 / PirateGame.PPM);
        setRegion(ship);
        setOrigin(32 / PirateGame.PPM,55 / PirateGame.PPM);

        // Sound effect for terrain collision
        breakSound = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/wood-bump.mp3"));
        // Sound effect for cannonball hit
        cannonballHitSound = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/ship-hit.wav"));

        // Sets cannonball array
        cannonBalls = new Array<>();
        // Decrease health if playing on hard mode
        System.out.println(screen.difficulty);
        if (screen.difficulty < 1){
            Hud.changeHealth(-(int) (Hud.getHealth() * screen.difficulty / 4));
        }
        System.out.println(Hud.getHealth());
    }

    /**
     * used to load player information from file
     * @param element The element that stores the player information
     */
    public Player(GameScreen screen, Element element) {
        this(screen);
        setX(Float.parseFloat(element.getElementsByTagName("x").item(0).getTextContent()) * PirateGame.PPM);
        setY(Float.parseFloat(element.getElementsByTagName("y").item(0).getTextContent()) * PirateGame.PPM);
        b2body.setTransform(getX()/PirateGame.PPM, getY()/PirateGame.PPM, 0);
    }

    /**
     * Update the position of the player. Also updates any cannonballs the player generates
     *
     * @param dt Delta Time
     */
    public void update(float dt) {
        // Updates position and orientation of player
        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
        float angle = (float) Math.atan2(b2body.getLinearVelocity().y, b2body.getLinearVelocity().x);
        b2body.setTransform(b2body.getWorldCenter(), angle - ((float)Math.PI) / 2.0f);
        setRotation((float) (b2body.getAngle() * 180 / Math.PI));

        // Updates cannonball data
        for(CannonFire ball : cannonBalls) {
            ball.update(dt);
            if(ball.isDestroyed())
                cannonBalls.removeValue(ball, true);
        }

        if (inTornadoRange) {
            System.out.println("close");
            // move player towards tornado if in range

            // position of target to move towards
            float targetX = 0;
            float targetY = 0;

            // position of player
            float sourceX = b2body.getPosition().x;
            float sourceY = b2body.getPosition().y;

            // If the tornado is close to the player, move away from it
            double distance = Math.sqrt(Math.pow(targetX - b2body.getWorldCenter().x, 2) + Math.pow(targetY - b2body.getWorldCenter().y, 2));

            double tornadoDistance = GameScreen.getNearestTornado().getDistance();

            if (tornadoDistance > 3) {
                //System.out.println("far");
                Player.inTornadoRange = false;
                //Entity.tornadoContact();
            }
            // Uses a triangle to calculate the new trajectory
            double newAngle = Math.atan2(targetY - sourceY, targetX - sourceX);
            float velocity = 0.1f;
            float velX = (float) (Math.cos(newAngle) * velocity);
            float velY = (float) (Math.sin(newAngle) * velocity);

            // move the sprite then the collider
            setPosition(sourceX + velX, sourceY + velY);
            Vector2 newPos = new Vector2(sourceX, sourceY).add(new Vector2(velX, velY));
            b2body.setTransform(newPos, 0);
        }

        // Add delay timer between shots
        timeFired += dt;
    }

    /**
     * Plays the break sound when a boat takes damage
     */
    public void playBreakSound() {
        // Plays terrain collision sound effect
        if (screen.game.getPreferences().isEffectsEnabled()) {
            breakSound.play(screen.game.getPreferences().getEffectsVolume());
        }
    }

    /**
     * Plays the explosion sound when a boat is hit by a cannonball
     */
    public void playCannonballHitSound() {
        // Plays cannonball hit sound effect
        if (screen.game.getPreferences().isEffectsEnabled()) {
            cannonballHitSound.play(screen.game.getPreferences().getEffectsVolume());
        }
    }

    /**
     * Defines all the parts of the player's physical model. Sets it up for collisons
     */
    private void definePlayer() {
        // Defines a players position
        BodyDef bdef = new BodyDef();
        bdef.position.set(1200  / PirateGame.PPM, 2500 / PirateGame.PPM); // Default Pos: 1200,2500
        bdef.type = BodyDef.BodyType.DynamicBody;
        // linear damping slows the player if no movement key is pressed
        bdef.linearDamping = 1f;
        b2body = world.createBody(bdef);

        // Defines a player's shape and contact borders
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(55 / PirateGame.PPM);

        // setting BIT identifier
        fdef.filter.categoryBits = PirateGame.PLAYER_BIT;

        // determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.COIN_BIT
                | PirateGame.COIN_MAGNET_BIT | PirateGame.SPEED_BOOST_BIT
                | PirateGame.ABSORPTION_HEART_BIT | PirateGame.FASTER_SHOOTING_BIT
                | PirateGame.FREEZE_ENEMY_BIT | PirateGame.ENEMY_BIT
                | PirateGame.COLLEGE_BIT | PirateGame.COLLEGE_SENSOR_BIT
                | PirateGame.COLLEGE_FIRE_BIT | PirateGame.TORNADO_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Called when left button is pressed.
     * One cannonball is launched towards the mouse position from the centre of the player.
     *
     * @param camera The camera that is in charge of scaling
     */
    public void fire(OrthographicCamera camera) {
        // Fires cannon if specified delay time has passed
        if (timeFired > GameScreen.getShootingDelay()) {
            cannonBalls.add(new CannonFire(screen, b2body, camera, cannonVelocity));
            timeFired = 0;
        }
    }

    /**
     * Set velocity of cannon balls
     * @param velocity The speed of the cannon ball when it is fired
     */
    public void setCannonVelocity(int velocity){
        this.cannonVelocity = velocity;
    }

    /**
     * Get velocity of cannon balls
     * @return the speed of the cannon ball when it is fired
     */
    public int getCannonVelocity(){
        return this.cannonVelocity;
    }

    /**
     * Draws the player using batch
     * Draws cannonballs using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch){
        // Draws player and cannonballs
        super.draw(batch);
        for(CannonFire ball : cannonBalls)
            ball.draw(batch);
    }

    /**
     * Used to save the player information into the given element
     * @param element The given element
     */
    public void save(Document document, Element element) {
        Element xCoord = document.createElement("x");
        xCoord.appendChild(document.createTextNode(Float.toString(getX())));
        element.appendChild(xCoord);

        Element yCoord = document.createElement("y");
        yCoord.appendChild(document.createTextNode(Float.toString(getY())));
        element.appendChild(yCoord);
    }
}
