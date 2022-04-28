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
 * Heals the player when picked up
 * @author Robert Murphy, Charlie Crosley, James McNair, Dan Wade
 */
public class AbsorptionHeart extends PowerUp {
    private Texture absorptionHeart;

    /**
     * Instantiates Absorption Heart entity
     * Sets position in world which the heart shows and can be picked up for the player
     *
     * @param screen Visual data
     * @param x      x position of absorption heart
     * @param y      y position of absorption hear
     */
    public AbsorptionHeart(GameScreen screen, float x, float y) {
        super(screen, x, y);

        // Set speed boost image
        absorptionHeart = new Texture("entity/heart.png");
        //Set the position and size of the speed boost
        setBounds(0,0,48 / PirateGame.PPM, 48 / PirateGame.PPM);
        //Set the texture
        setRegion(absorptionHeart);
        //Sets origin of the speed boost
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);

        duration = 20;
    }

    /**
     * Updates the absorption heart state. If needed, deletes the absorption heart if picked up
     */
    public void update() {
        //If coin is set to destroy and isnt, destroy it
        if(setToDestroyed && !destroyed) {
            world.destroyBody(b2body); 
            destroyed = true;
        }
        //Update position of power up
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
            Hud.setAbsorptionHeartTimer(timeLeft);
        }
    }

    @Override
    public void endPowerUp() {
        // nothing needs to be done here
    }

    /**
     * Defines all the parts of the absorption heart physical model. Sets it up for collisions
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
        fdef.filter.categoryBits = PirateGame.ABSORPTION_HEART_BIT;

        // Determining what this BIT can collide with
        fdef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT;
        fdef.shape = shape;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Handle when the absorption heart comes into contact with the player
     * (i.e. the player picks up the powerup)
     */
    @Override
    public void entityContact() {
        if (!destroyed) {
            active = true;
            timeLeft += (duration / 2);
            // Heal player
            Hud.changeHealth(10);

            // Set to destroy
            setToDestroyed = true;
            // Play pickup sound
            // Checking if game is null allows testing of this function 
            if (screen.game != null && screen.game.getPreferences().isEffectsEnabled()) {
                getSound().play(screen.game.getPreferences().getEffectsVolume());
            }
        }
    }
}
