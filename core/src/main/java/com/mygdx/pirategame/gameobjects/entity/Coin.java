package com.mygdx.pirategame.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.save.GameScreen;

/**
 * Coin
 * Creates an object for each coin
 * Extends the entity class to define coin as an entity
 *
 *@author Joe Dickinson, Charlie Crosley, Marc Perales Salomo, James McNair, Robert Murphy, Dan Wade
 *@version 1.0
 */
public class Coin extends Entity {
    private Texture coin;
    private Sound coinPickup;
    private Player player;
    public boolean coinMagnetActive = false, inMagnetRange = false;
    public Body b2bodyMagnet;

    /**
     * Instantiates a new Coin.
     *
     * @param screen the screen the coin will display one
     * @param x      the x coordinate where the coin will be placed
     * @param y      the y coordinate where the coin will be placed
     */
    public Coin(GameScreen screen, float x, float y) {
        super(screen, x, y);
        //Set coin image
        coin = new Texture("entity/coin.png");
        //Set the position and size of the coin
        setBounds(0,0,48 / PirateGame.PPM, 48 / PirateGame.PPM);
        //Set the texture
        setRegion(coin);
        //Sets origin of the coin
        setOrigin(24 / PirateGame.PPM,24 / PirateGame.PPM);
        coinPickup = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/coin-pickup.mp3"));

        // keep reference to the player object for coin magnet power up
        player = screen.getPlayer();

        setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 2f);
    }

    /**
     * Updates the coins state. If needed, deletes the coin if picked up.
     */
    public void update() {
        // If coin is set to destroy and isnt, destroy it
        if (setToDestroyed && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
        }
        //Update position of coin
        if (!destroyed) {
            if (inMagnetRange) {
                // move coin towards player if in range

                // position of player
                //float targetX = player.getX();
                float targetX = screen.getPlayerPos().x;
                //float targetY = player.getY();
                float targetY = screen.getPlayerPos().y;
                // position of coin
                float sourceX = b2body.getPosition().x;
                float sourceY = b2body.getPosition().y;

                // If the coin is close to the player, collect it
                double distance = Math.sqrt(Math.pow(targetX - b2body.getWorldCenter().x, 2) + Math.pow(targetY - b2body.getWorldCenter().y, 2));
                if (distance < 1) {
                    coinMagnetActive = false;
                    entityContact();
                }
                // Uses a triangle to calculate the new trajectory
                double angle = Math.atan2(targetY - sourceY, targetX - sourceX);
                float velocity = 0.1f;
                float velX = (float) (Math.cos(angle) * velocity);
                float velY = (float) (Math.sin(angle) * velocity);

                // move the sprite then the collider
                setPosition(sourceX + velX, sourceY + velY);
                Vector2 newPos = new Vector2(sourceX, sourceY).add(new Vector2(velX, velY));
                b2body.setTransform(newPos, 0);
            }
        }
    }

    /**
     * Defines all the parts of the coins physical model. Sets it up for collisons
     */
    @Override
    protected void defineEntity() {
        // set the body definition for the default body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        //set the body definition for the coin magnet body
        b2bodyMagnet = world.createBody(bodyDef);

        //Sets collision boundaries
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(24 / PirateGame.PPM);
        // setting BIT identifier
        fixtureDef.filter.categoryBits = PirateGame.COIN_BIT;
        // determining what this BIT can collide with
        fixtureDef.filter.maskBits = PirateGame.DEFAULT_BIT | PirateGame.PLAYER_BIT | PirateGame.ENEMY_BIT;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        b2body.createFixture(fixtureDef).setUserData(this);

        // Create fixture for larger collision box whilst coin magnet is active
        // Disabled until coin magnet power up is collected
        fixtureDef.shape.setRadius(450 / PirateGame.PPM);
        b2bodyMagnet.createFixture(fixtureDef).setUserData(this);
        b2bodyMagnet.setActive(false);
    }

    /**
     * Enables and disables the larger collision circle for the coin magnet as well as some flags
     */
    public void toggleCoinMagnet() {
        // disabling/enabling the default collision circle at the same time crashes the game
        if (b2bodyMagnet.isActive()) {
            // disable coin magnet
            b2bodyMagnet.setActive(false);
            inMagnetRange = false;
            coinMagnetActive = false;
        }
        else {
            // enable coin magnet
            b2bodyMagnet.setActive(true);
            coinMagnetActive = true;
        }
    }

    /**
     * What happens when an entity collides with the coin. The only entity that is able to do so is the player ship
     */
    @Override
    public void entityContact() {
        // only attract coins if coin collides with the players larger hit box
        if (coinMagnetActive) {
            inMagnetRange = true;
        }
        // Move coin towards player if coin magnet is activated, otherwise collect instantly
        else {
            //Add a coin
            Hud.changeCoins(1);
            //Set to destroy
            setToDestroyed = true;

            //Play pickup sound
            // Checking if game is null allows testing of this function
            if (screen.game != null && screen.game.getPreferences().isEffectsEnabled()) {
                coinPickup.play(screen.game.getPreferences().getEffectsVolume());
            }
        }
    }

    /**
     * Draws the coin using batch
     *
     * @param batch The batch of the program
     */
    public void draw(Batch batch) {
        if(!destroyed) {
            super.draw(batch);
        }
    }
}
