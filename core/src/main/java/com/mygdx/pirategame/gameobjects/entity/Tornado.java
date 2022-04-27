package com.mygdx.pirategame.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.pathfinding.Checkpoint;
import com.mygdx.pirategame.pathfinding.pathManager.AttackPath;
import com.mygdx.pirategame.pathfinding.pathManager.PathManager;
import com.mygdx.pirategame.save.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Tornado
 * Creates an object for each tornado
 * Extends the entity class to define tornado as an entity
 *
 *@author Robert Murphy
 *@version 1.0
 */
public class Tornado extends Entity {
    private Texture tornado;
    private Sound tornadoSound;
    private Player player;
    public Body b2bodyTornado;
    public boolean movement = false;
    float angle;
    //public static boolean active = false;
    private float timer;
    private float moveTimer;

    public static final int COLLISIONRADIUS = 55;
    public static final int COLLISIONOFFSET = 15;
    private PathManager pathManager = null;
    private List<Checkpoint> path;
    private int updateDelay = 0;

    /**
     * Instantiates a new Tornado.
     *
     * @param screen the screen it's going onto
     * @param x      the x value to be placed at
     * @param y      the y value to be placed at
     */
    public Tornado(GameScreen screen, float x, float y) {
        super(screen, x, y);

        // Set tornado image
        tornado = new Texture("entity/tornado.png");
        // Set the position and size of the tornado
        setBounds(0, 0, 144 / PirateGame.PPM, 144 / PirateGame.PPM);
        // Set the texture
        setRegion(tornado);
        // Sets origin of the tornado
        setOrigin(24 / PirateGame.PPM, 24 / PirateGame.PPM);
        tornadoSound = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/coin-pickup.mp3")); // CHANGE

        player = screen.getPlayer();

        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
    }

    /**
     * Updates the tornado state.
     */
    public void update(float dt) {
        timer += dt;
        moveTimer += dt;
        // Once tornado has existed for a certain amount of time it is removed
        if (timer > 20) {
            setToDestroyed = true;
        }

        /*
        if (destroyed) {
            return;
        }

        if (setToDestroy) {
            //Play death noise
            if (GameScreen.game.getPreferences().isEffectsEnabled()) {
                destroy.play(GameScreen.game.getPreferences().getEffectsVolume());
            }
            world.destroyBody(b2body);
            destroyed = true;
            //Change player coins and points
            Hud.changePoints(50);
            Hud.changeCoins(50);
        }
        */

        if (getDistance() < 5) {
            if (moveTimer > 1) {
                System.out.println(getDistance());
                Player.inTornadoRange = true;
                moveTimer = 0;
            }
        }

        // Update position and angle of sea monster
        // Sprite is off center when moving animation is playing, so it is offset
        if (movement) {
            // rotate sea monster when in moving animation
            setPosition(b2body.getPosition().x - getWidth() / 4f, b2body.getPosition().y - getHeight() / 2f);
            angle = (float) Math.atan2(b2body.getLinearVelocity().y, b2body.getLinearVelocity().x);
            b2body.setTransform(b2body.getWorldCenter(), angle - ((float) Math.PI) / 2.0f);
            setRotation((float) (b2body.getAngle() * 180 / Math.PI) + 90);
        }
        else {
            // Reset velocity and rotation when in idle animation
            setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
            b2body.setTransform(b2body.getWorldCenter(), 0);
            setRotation(0);
        }

        if (updateDelay > 0) {
            updateDelay--;
            return;
        }

        // If out of player range stop moving and play idle animation by setting movement to false
        if (!inPlayerRange() && movement) {
            movement = false;
            b2body.setLinearVelocity(0, 0);
            return;
        }
        // Attack the player and follow them if in range, play moving animation by setting movement to true
        else if (inPlayerRange() && !movement){
            setPathManager(new AttackPath(pathManager, this, screen));
            movement = true;
        }

        // If there is no path set, generate a new one
        if ((path == null || path.isEmpty()) && pathManager != null) {
            generateNewPath();
            return;
        }
        else {
            if (pathManager == null) return;

            // updating the pathing manager
            pathManager.update(dt);

            if (path  == null) return;

            if (path.isEmpty()) return;
            Checkpoint cp = path.get(0);

            if (cp.getVector2().dst(b2body.getPosition().scl(PirateGame.PPM)) < PirateGame.PPM / 2) {
                path.remove(cp);
                if (path.isEmpty()) {
                    generateNewPath();
                }
            }

            final float speed;
            if (movement) {
                speed = 100f * dt;
            } else {
                speed = 0;
            }
            Vector2 v = travelToCheckpoint(speed, cp);
            b2body.setLinearVelocity(v);
        }
    }

    /**
     * Defines all the parts of the tornado physical model. Sets it up for collisions
     */
    @Override
    protected void defineEntity() {
        // set the body definition for the default body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        //set the body definition for the tornado body
        b2bodyTornado = world.createBody(bodyDef);

        //Sets collision boundaries
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30 / PirateGame.PPM);

        // setting BIT identifier
        fixtureDef.filter.categoryBits = PirateGame.TORNADO_BIT;
        // determining what this BIT can collide with
        fixtureDef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        b2body.createFixture(fixtureDef).setUserData(this);

        // Create fixture for larger collision box whilst coin magnet is active
        // Disabled until coin magnet power up is collected
        fixtureDef.shape.setRadius(250 / PirateGame.PPM);
        b2bodyTornado.createFixture(fixtureDef).setUserData(this);
        //b2bodyTornado.setActive(false);
    }

    /**
     * What happens when an entity collides with the tornado.
     */
    @Override
    public void entityContact() {
        updateDelay = 50;
        if (pathManager != null) generateNewPath();

        //System.out.println("near");
        //Player.inTornadoRange = true;
        /*
        if (active) {
            //inTornadoRange = true;
            Player.inTornadoRange = true;
            System.out.println("active");
        }

         */
    }

    /**
     * Calculates the distance from the player to the tornado
     *
     * @return The distance from the player to the tornado
     */
    public double getDistance() {
        // Position of player
        float playerX = player.b2body.getPosition().x;
        float playerY = player.b2body.getPosition().y;

        // Calculate distance from player to tornado
        double distance = Math.sqrt(Math.pow(playerX - b2body.getWorldCenter().x, 2) + Math.pow(playerY - b2body.getWorldCenter().y, 2));
        return distance;
    }

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
        // dumping old path
        path = new ArrayList<>();
        if (pathManager != null) generateNewPath();
    }

    /**
     * Used to generate a new path from the current location to a random point on the map
     */

    public void generateNewPath() {

        Vector2 destination = pathManager.generateDestination();
        if (destination == null) {
            // destination will be regenerated next update
            return;
        }
        path = screen.getPathFinder().getPath((b2body.getPosition().x * PirateGame.PPM), (b2body.getPosition().y * PirateGame.PPM), destination.x, destination.y, COLLISIONRADIUS + COLLISIONOFFSET, COLLISIONRADIUS + COLLISIONOFFSET);
        if (path != null && path.size() > 1) {
            // removing the start node from the path as ship is already at it
            path.remove(0);
        }
    }

    /**
     * Checks if the ship should pathfind or just sit still (used to reduce needless load)
     *
     * @return If the ship is in range of the player
     */
    public boolean inPlayerRange() {
        return screen.getPlayerPos().dst(b2body.getPosition()) < 7;
    }

    /**
     * Used to check if the set location is traversable by a ship of this size
     *
     * @param x The x location of the proposed location
     * @param y The y location of the proposed location
     * @return If the ship can go there
     */
    public boolean isTraversable(float x, float y) {
        return screen.getPathFinder().isTraversable(x, y, COLLISIONRADIUS + COLLISIONOFFSET, COLLISIONRADIUS + COLLISIONOFFSET);
    }

    /**
     * @param maxDistance The max distance that can be travelled
     * @param cp          The checkpoint to travel towards
     * @return The vector that needs to be applied to travel towards the checkpoint
     */

    private Vector2 travelToCheckpoint(float maxDistance, Checkpoint cp) {
        Vector2 v = new Vector2(cp.x - (b2body.getPosition().x * PirateGame.PPM) - getWidth() / 2, cp.y - (b2body.getPosition().y * PirateGame.PPM) - getHeight() / 2);

        return v.limit(maxDistance);
    }


    /**
     * Draws the tornado using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
        }
    }
}
