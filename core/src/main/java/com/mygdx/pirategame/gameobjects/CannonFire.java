package com.mygdx.pirategame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.save.GameScreen;

/**
 * Cannon Fire
 * Combat related cannon fire
 * Used by player and colleges,
 * Use should extend to enemy ships when implementing ship combat
 *
 *@author Ethan Alabaster, Marc Perales Salomo, James McNair, Robert Murphy, Dan Wade
 *@version 1.0
 */
public class CannonFire extends Sprite {
    private World world;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private Body b2body;
    private double angle;
    private float velocity;
    private Sound fireNoise;
    private final Texture texture;
    private float sourceX, sourceY;
    private float x, y;
    private Vector2 mouse;
    private float targetX, targetY;

    /**
     * Instantiates cannon fire
     * Determines general cannonball data
     * Determines firing sound
     *
     * For assessment 2, the method of firing cannons for the player has changed from using the keyboard to mouse click.
     * Additionally, the cannon now fires in the direction of the mouse, not just from the side of the ship like before.
     * We changed this because we felt it makes the game easier to play.
     *
     * @param screen visual data
     * @param body body of origin
     * @param camera game scaling camera
     * @param velocity velocity of the cannonball
     */
    public CannonFire(GameScreen screen, Body body, OrthographicCamera camera, float velocity) {
        this.velocity = velocity;
        this.world = screen.getWorld();

        // Get values of mouse location, scaled to size of screen
        mouse = PirateGame.getScaledMouseLocation(camera);
        targetX = mouse.x;
        targetY = mouse.y;
        System.out.println(targetX);
        System.out.println(targetY);


        // Value of player source used to calculate angle
        sourceX = camera.viewportWidth /2;
        sourceY = camera.viewportHeight /2;

        // Value of player source used to set where the cannonball comes from
        x = body.getPosition().x;
        y = body.getPosition().y;

        // Uses a triangle to calculate the new trajectory
        angle = Math.atan2(targetY - sourceY, targetX - sourceX);

        //set cannonBall dimensions for the texture
        this.texture = new Texture("entity/cannonball.png");
        setRegion(texture);
        setBounds(x, y, 10 / PirateGame.PPM, 10 / PirateGame.PPM);
        //set collision bounds
        defineCannonBall();
        //set sound for fire and play if on
        fireNoise = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/explosion.wav"));
        if (screen.game.getPreferences().isEffectsEnabled()) {
            fireNoise.play(screen.game.getPreferences().getEffectsVolume());
        }
    }

    /**
     * Defines the existence, direction, shape and size of a cannonball
     */
    public void defineCannonBall() {
        //sets the body definitions
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        //Sets collision boundaries
        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / PirateGame.PPM);

        // setting BIT identifier
        fDef.filter.categoryBits = PirateGame.CANNON_BIT;
        // determining what this BIT can collide with
        fDef.filter.maskBits = PirateGame.ENEMY_BIT | PirateGame.PLAYER_BIT | PirateGame.COLLEGE_BIT;
        fDef.shape = shape;
        fDef.isSensor = true;
        b2body.createFixture(fDef).setUserData(this);

        float velX = (float) (Math.cos(angle) * velocity);
        float velY = (float) (Math.sin(angle) * velocity);

        b2body.applyLinearImpulse(new Vector2(velX, velY), b2body.getWorldCenter(), true);
    }

    /**
     * Updates state with delta time
     * Defines range of cannon fire
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta){
        stateTime += delta;
        //Update position of ball
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        //If ball is set to destroy and isn't, destroy it
        if((setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        // determines cannonball range
        if(stateTime > 0.98f) {
            setToDestroy();
        }
    }

    /**
     * Changes destruction state
     */
    public void setToDestroy(){
        setToDestroy = true;
    }

    /**
     * Returns destruction status
     */
    public boolean isDestroyed(){
        return destroyed;
    }
}
