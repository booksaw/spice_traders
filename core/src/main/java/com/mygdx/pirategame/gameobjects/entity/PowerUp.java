package com.mygdx.pirategame.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.pirategame.save.GameScreen;

/**
 * Abstract class to define characteristics of power ups
 */
public abstract class PowerUp extends Entity {
    private Sound pickupSound;
    public boolean active = false;
    public float timer = 0;
    protected float timeLeft;
    public float duration;

    /**
     *
     * Instantiates powerup
     * Sets position in world
     *
     * @param screen Visual data
     * @param x      x position of entity
     * @param y      y position of entity
     */
    public PowerUp(GameScreen screen, float x, float y) {
        super(screen, x, y);
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEntity();

        // Sets pickup sound
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/coin-pickup.mp3"));
    }

    /**
     * Handle updates to power up
     */
    public abstract void update();

    /**
     * Get sound played when the powerup is picked up
     * @return Sound object which can be used to play the sound
     */
    public Sound getSound() {
        return pickupSound;
    }

    /**
     * Handle powerup expiring
     */
    public abstract void endPowerUp();

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

