package com.mygdx.pirategame.gameobjects.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.pirategame.save.GameScreen;

public abstract class PowerUp extends Entity {
    private Sound pickupSound;
    public boolean active = false;
    public float timer = 0;
    protected float timeLeft;
    public float duration;

    /**
     * x
     * Instantiates an entity
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

    public abstract void update();

    public Sound getSound() {
        return pickupSound;
    }

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

