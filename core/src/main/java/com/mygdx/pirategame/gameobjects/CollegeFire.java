package com.mygdx.pirategame.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.save.GameScreen;

import java.util.Random;

/**
 * College Fire
 * Defines college attack method
 * Defines college cannonball projectiles
 *
 *@author Ethan Alabaster, Marc Perales Salomo, Charlie Crosley, Robert Murphy
 *@version 1.0
 */

public class CollegeFire extends Sprite {
    private World world;
    private Texture cannonBall;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private Body b2body;
    private Vector2 playerPos;
    private float collisionRadius;

    /**
     * Defines player position
     * Defines cannonballs
     *
     * @param screen Visual data
     * @param x starting x position of projectile
     * @param y starting y position of projectile
     */
    public CollegeFire(GameScreen screen, float x, float y) {
        this.world = screen.getWorld();
        playerPos = screen.getCenteredPlayerPos();

        cannonBall = new Texture("entity/cannonball.png");
        //Set the position and size of the ball
        setRegion(cannonBall);
        setBounds(x, y, 10 / PirateGame.PPM, 10 / PirateGame.PPM);
        collisionRadius = 5;
        defineCannonBall();
    }

    /**
     * Defines player position
     * Defines custom projectile
     *
     * @param screen Visual data
     * @param texture The texture of the bullet
     * @param x starting x position of projectile
     * @param y starting y position of projectile
     * @param width width of sprite
     * @param height height of sprite
     * @param angle angle of sprite
     */
    public CollegeFire(GameScreen screen, Texture texture, float x, float y, float width, float height, float angle) {
        this.world = screen.getWorld();
        playerPos = screen.getCenteredPlayerPos();

        cannonBall = texture;
        //Set the position and size of the ball
        setRegion(cannonBall);
        setBounds(x, y, width / PirateGame.PPM, height / PirateGame.PPM);
        collisionRadius = 50;
        defineCannonBall();

        // Set origin to center of sprite
        setOriginCenter();

        // Rotate projectile to match direction of movement
        angle = (float) Math.atan2(b2body.getLinearVelocity().y, b2body.getLinearVelocity().x);
        b2body.setTransform(b2body.getWorldCenter(), angle - ((float) Math.PI) / 2.0f) ;
        setRotation((float) (b2body.getAngle() * 180 / Math.PI));
    }

    /**
     * Defines cannonball data
     * Defines cannonball shape
     */
    public void defineCannonBall() {
        //sets the body definitions
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);
        MassData mass = new MassData();
        mass.mass = (float) 0.01;
        b2body.setMassData(mass);
        //Sets collision boundaries
        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(collisionRadius / PirateGame.PPM);
        // setting BIT identifier
        fDef.filter.categoryBits = PirateGame.COLLEGE_FIRE_BIT;
        // determining what this BIT can collide with
        fDef.filter.maskBits = PirateGame.PLAYER_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);

        // Math for firing the cannonball at the player
        playerPos.sub(b2body.getPosition());

        // adding randomness to cannon firing
        Random rnd = new Random();
        float rndX = (float) (rnd.nextInt(2) - 1 + rnd.nextDouble());
        float rndY = (float) (rnd.nextInt(2) - 1 + rnd.nextDouble());
        playerPos.sub(rndX, rndY);

        playerPos.nor();
        float speed = 5f;
        b2body.setLinearVelocity(playerPos.scl(speed));
    }

    /**
     * Defines water slash data
     * Defines water slash shape
     */
    private void defineWaterSlash() {
    //sets the body definitions
        BodyDef bDef = new BodyDef();
        bDef.position.set(getX(), getY());
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);
        MassData mass = new MassData();
        mass.mass = (float) 0.01;
        b2body.setMassData(mass);
        //Sets collision boundaries
        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        //Rectangle shape = new Rectangle();
        shape.setRadius(collisionRadius / PirateGame.PPM);
        // setting BIT identifier
        fDef.filter.categoryBits = PirateGame.COLLEGE_FIRE_BIT;
        // determining what this BIT can collide with
        fDef.filter.maskBits = PirateGame.PLAYER_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef).setUserData(this);

        // Math for firing the cannonball at the player
        playerPos.sub(b2body.getPosition());

        // adding randomness to cannon firing
        Random rnd = new Random();
        float rndX = (float) (rnd.nextInt(2) - 1 + rnd.nextDouble());
        float rndY = (float) (rnd.nextInt(2) - 1 + rnd.nextDouble());
        playerPos.sub(rndX, rndY);

        playerPos.nor();
        float speed = 5f;
        b2body.setLinearVelocity(playerPos.scl(speed));
    }

    /**
     * Updates state with delta time
     * Defines range of cannon fire
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt){
        stateTime += dt;
        //If college is set to destroy and isn't, destroy it
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        if((setToDestroy) && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        // determines cannonball range
        if(stateTime > 2f) {
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
     * @return destruction status
     */
    public boolean isDestroyed(){
        return destroyed;
    }
}
