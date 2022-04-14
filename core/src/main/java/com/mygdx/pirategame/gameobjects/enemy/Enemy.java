package com.mygdx.pirategame.gameobjects.enemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.pirategame.HealthBar;
import com.mygdx.pirategame.save.GameScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Enemy
 * Class to generate enemies
 * Instantiates enemies
 *
 *@author Ethan Alabaster
 *@version 1.0
 */
public abstract class Enemy extends Sprite {
    protected World world;
    protected GameScreen screen;
    public Body b2body;
    public boolean setToDestroy;
    public boolean destroyed;
    public int health;
    public float damage;
    protected HealthBar bar;

    /**
     * Instantiates an enemy
     *
     * @param screen Visual data
     * @param x x position of entity
     * @param y y position of entity
     */
    public Enemy(GameScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        this.setToDestroy = false;
        this.destroyed = false;
        this.health = 100;

        defineEnemy();
        bar = new HealthBar(this);
    }

    public Enemy(GameScreen screen, Element element){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(Float.parseFloat(element.getElementsByTagName("x").item(0).getTextContent()), Float.parseFloat(element.getElementsByTagName("y").item(0).getTextContent()));
        this.setToDestroy = false;
        this.destroyed = false;
        this.health = Integer.parseInt(element.getElementsByTagName("health").item(0).getTextContent());

        defineEnemy();
        bar = new HealthBar(this);
        bar.changeHealth(100 - health);
    }

    /**
     * Defines enemy
     */
    protected abstract void defineEnemy();

    /**
     * Defines contact
     */
    public abstract void onContact();

    public abstract void onContactOther();
    public abstract void update(float dt);

    /**
     * Used to save any information about the enemy
     * @param document The document controlling the saving
     * @param element the element to save to
     */
    public void save(Document document, Element element){
        Element xCoord = document.createElement("x");
        xCoord.appendChild(document.createTextNode(Float.toString(getX())));
        element.appendChild(xCoord);

        Element yCoord = document.createElement("y");
        yCoord.appendChild(document.createTextNode(Float.toString(getY())));
        element.appendChild(yCoord);

        Element healthele = document.createElement("health");
        healthele.appendChild(document.createTextNode(Integer.toString(health)));
        element.appendChild(healthele);

        saveChild(document, element);
    }

    /**
     * Used to save any information required by the child class
     * @param document The document controlling the saving
     * @param element The element to save to
     */
    protected abstract void saveChild(Document document, Element element);


    /**
     * Checks recieved damage
     * Increments total damage by damage received
     * @param value Damage received
     */
    public void changeDamageReceived(int value){
        damage += value;
    }
}
