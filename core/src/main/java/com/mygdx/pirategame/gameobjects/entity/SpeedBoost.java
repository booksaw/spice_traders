package com.mygdx.pirategame.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.save.GameScreen;

/**
 * Speed boost powerup which makes the player able to move faster
 * @author Robert Murphy, Charlie Crosley
 */
public class SpeedBoost extends PowerUp {
    private Texture speedBoost;

    /**
     * x
     * Instantiates the powerup and icon on map
     * Sets position in world
     *
     * @param screen Visual data
     * @param x      x position of entity
     * @param y      y position of entity
     */
    public SpeedBoost(GameScreen screen, float x, float y) {
        super(screen, x, y);

        // Set speed boost image
        speedBoost = new Texture("entity/bolt.png");
        //Set the position and size of the speed boost
        setBounds(0,0,48 / PirateGame.PPM, 48 / PirateGame.PPM);
        //Set the texture
        setRegion(speedBoost);
        //Sets origin of the speed boost
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);

        // Set duration of power up
        duration = 20;
    }

    /**
     * Updates the speed boost state. If needed, deletes the speed boost if picked up
     */
    @Override
    public void update() {
        //If power up is set to destroy and isn't, destroy it
        if(setToDestroyed && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        //Update power up of coin
        else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
        }
        // Ability lasts for a specified duration
        if (timer > duration) {
            endPowerUp();
            timer = 0;
            timeLeft = 0;
        }
        else if (active) {
            timer += Gdx.graphics.getDeltaTime();
            timeLeft -= Gdx.graphics.getDeltaTime();
            Hud.setSpeedBoostTimer(timeLeft);
        }
    }

    /**
     * Handle powerup expiration
     */
    @Override
    public void endPowerUp() {
        // Reset speed and acceleration variables
        GameScreen.setMaxSpeed(4f);
        GameScreen.setAcceleration(0.1f);

        active = false;
        Gdx.app.log("speedBoost", "ended");
    }

    /**
     * Defines all the parts of the speed boost physical model. Sets it up for collisions
     */
    @Override
    protected void defineEntity() {
        // Sets the body definitions
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        // Sets collision boundaries
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(24 / PirateGame.PPM);

        // Setting BIT identifier
        fdef.filter.categoryBits = PirateGame.SPEED_BOOST_BIT;

        // Determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * What happens when an entity collides with the speed boost, only the player ship can
     */
    @Override
    public void entityContact() {
        if (!destroyed) {
            active = true;
            timeLeft += (duration / 2);
            // Increase speed and acceleration variables (by percentage)
            GameScreen.changeMaxSpeed((float) 10);
            GameScreen.changeAcceleration((float) 15);

            // Set to destroy
            setToDestroyed = true;
            Gdx.app.log("speedBoost", "collision");
            // Play pickup sound
            if (screen.game.getPreferences().isEffectsEnabled()) {
                getSound().play(screen.game.getPreferences().getEffectsVolume());
            }
        }
    }
}
