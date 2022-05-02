package com.mygdx.pirategame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.pirategame.screen.SkillTreeScreen;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Hud
 * Produces a hud for the player
 * This shows the number of coins, health, points the player has
 * Also displays time remaining for any powerups collected
 *
 * @author Ethan Alabaster, Sam Pearson, Dan Wade, Robert Crosley, Marc Perales Salomo, James McNair
 * @version 1.0
 */
public class Hud implements Disposable {
    public static Stage stage;
    private final Viewport viewport;

    private float timeCount;
    private static Integer score;
    public static Integer health;
    private final Texture hp;
    private final Texture boxBackground;
    private final Texture coinPic;

    private static Label scoreLabel;
    private static Label healthLabel;
    private static Label coinLabel;
    private static Label pointsText;
    private static Integer coins;
    private static Integer coinMulti;
    private final Image hpImg;
    private final Image box;
    private final Image coin;

    private static Label coinMagnetLabel;
    private static Label speedBoostLabel;
    private static Label absorptionHeartLabel;
    private static Label fasterShootingLabel;
    private static Label freezeEnemyLabel;
    private final Texture magnet;
    private final Texture bolt;
    private final Texture heart;
    private final Texture gun;
    private final Texture ice;
    private final Image magnetImg;
    private final Image boltImg;
    private final Image heartImg;
    private final Image gunImg;
    private final Image iceImg;

    private static float coinMagnet;
    private static float speedBoost;
    private static float absorptionHeart;
    private static float fasterShooting;
    private static float freezeEnemy;

    private Table table4;
    private Table table5;


    /**
     * Retrieves information and displays it in the hud
     * Adjusts hud with viewport
     *
     * @param sb Batch of images used in the hud
     */
    public Hud(SpriteBatch sb, boolean headlessmode) {
        health = 100;
        score = 0;
        coins = 0;
        coinMulti = 1;
        //Set images
        hp = new Texture("hp.png");
        boxBackground = new Texture("hudBG.png");
        coinPic = new Texture("entity/coin.png");

        hpImg = new Image(hp);
        box = new Image(boxBackground);
        coin = new Image(coinPic);

        viewport = new ScreenViewport();
        if (!headlessmode) {
            stage = new Stage(viewport, sb);
        }

        //Creates tables
        Table table1 = new Table(); //Counters
        Table table2 = new Table(); //Pictures or points label
        Table table3 = new Table(); //Background

        table1.top().right();
        table1.setFillParent(true);
        table2.top().right();
        table2.setFillParent(true);
        table3.top().right();
        table3.setFillParent(true);

        scoreLabel = new Label(String.format("%03d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label(String.format("%03d", health), new Label.LabelStyle(new BitmapFont(), Color.RED));
        coinLabel = new Label(String.format("%03d", coins), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        pointsText = new Label("Points:", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table3.add(box).width(140).height(140).padBottom(15).padLeft(30);
        table2.add(hpImg).width(32).height(32).padTop(16).padRight(90);
        table2.row();
        table2.add(coin).width(32).height(32).padTop(8).padRight(90);
        table2.row();
        table2.add(pointsText).width(32).height(32).padTop(6).padRight(90);
        table1.add(healthLabel).padTop(20).top().right().padRight(40);
        table1.row();
        table1.add(coinLabel).padTop(20).top().right().padRight(40);
        table1.row();
        table1.add(scoreLabel).padTop(22).top().right().padRight(40);

        if (!headlessmode) {
            stage.addActor(table3);
            stage.addActor(table2);
            stage.addActor(table1);
        }

        // Set images
        magnet = new Texture("entity/magnet.png");
        bolt = new Texture("entity/bolt.png");
        heart = new Texture("entity/heart.png");
        gun = new Texture("entity/gun.png");
        ice = new Texture("entity/ice.png");

        magnetImg = new Image(magnet);
        boltImg = new Image(bolt);
        heartImg = new Image(heart);
        gunImg = new Image(gun);
        iceImg = new Image(ice);

        table4 = new Table(); // Power up counter
        table5 = new Table(); // Power up images

        table4.bottom().right();
        table4.setFillParent(true);
        table5.bottom().right();
        table5.setFillParent(true);

        coinMagnetLabel = new Label(String.format("%03f", coinMagnet), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        speedBoostLabel = new Label(String.format("%03d", (Math.round(speedBoost))), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        absorptionHeartLabel = new Label(String.format("%03f", absorptionHeart), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        fasterShootingLabel = new Label(String.format("%03f", fasterShooting), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        freezeEnemyLabel = new Label(String.format("%03f", freezeEnemy), new Label.LabelStyle(new BitmapFont(), Color.YELLOW));

        if (!headlessmode) {
            stage.addActor(table5);
            stage.addActor(table4);
        }
    }

    /**
     * Updates the state of the hud
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    public void update(float dt) {
        timeCount += dt;
        coinLabel.setText(String.format("%03d", coins));
        if (timeCount >= 1) {

            // Gain point every second
            score += 1;
            scoreLabel.setText(String.format("%03d", score));
            timeCount = 0;

            // Checks if each power up has been collected and adds it to display
            if (coinMagnet > 0) {
                table4.add(coinMagnetLabel).padBottom(25).top().right().padRight(40);
                table4.row();
                table5.add(magnetImg).width(32).height(32).padBottom(16).padRight(90);
                table5.row();
                coinMagnetLabel.setText(String.format("%01d", (Math.round(coinMagnet))));
            }
            if (coinMagnet < 1) {
                table4.removeActor(coinMagnetLabel);
                table5.removeActor(magnetImg);
            }
            if (speedBoost > 0) {
                table4.add(speedBoostLabel).padBottom(25).top().right().padRight(40);
                table4.row();
                table5.add(boltImg).width(32).height(32).padBottom(16).padRight(90);
                table5.row();
                speedBoostLabel.setText(String.format("%01d", (Math.round(speedBoost))));
            }
            if (speedBoost < 1) {
                table4.removeActor(speedBoostLabel);
                table5.removeActor(boltImg);
            }
            if (absorptionHeart > 0) {
                table4.add(absorptionHeartLabel).padBottom(25).top().right().padRight(40);
                table4.row();
                table5.add(heartImg).width(32).height(32).padBottom(16).padRight(90);
                table5.row();
                absorptionHeartLabel.setText(String.format("%01d", (Math.round(absorptionHeart))));
            }
            if (absorptionHeart < 1) {
                table4.removeActor(absorptionHeartLabel);
                table5.removeActor(heartImg);
            }
            if (fasterShooting > 0) {
                table4.add(fasterShootingLabel).padBottom(25).top().right().padRight(40);
                table4.row();
                table5.add(gunImg).width(32).height(32).padBottom(16).padRight(90);
                table5.row();
                fasterShootingLabel.setText(String.format("%01d", (Math.round(fasterShooting))));
            }
            if (fasterShooting < 1) {
                table4.removeActor(fasterShootingLabel);
                table5.removeActor(gunImg);
            }
            if (freezeEnemy > 0) {
                table4.add(freezeEnemyLabel).padBottom(25).top().right().padRight(40);
                table4.row();
                table5.add(iceImg).width(32).height(32).padBottom(16).padRight(90);
                table5.row();
                freezeEnemyLabel.setText(String.format("%01d", (Math.round(freezeEnemy))));
            }
            if (freezeEnemy < 1) {
                table4.removeActor(freezeEnemyLabel);
                table5.removeActor(iceImg);
            }

            //Check if a points' boundary is met
            SkillTreeScreen.pointsCheck(score);
        }
    }

    /**
     * Update label which displays number of coins
     * Called from GoldShop, when something has been purchased, to show new balance
     */
    public static void updateCoins(){
        coinLabel.setText(String.format("%03d", coins));
    }


    /**
     * Returns value of timer for coin magnet power up
     *
     * @return coinMagnet : returns timer value
     */
    public static float getCoinMagnetTimer() {
        return coinMagnet;
    }

    /**
     * Sets timer for coin magnet power up
     *
     * @param value Value for timer
     */
    public static void setCoinMagnetTimer(float value) {
        coinMagnet = value;
    }

    /**
     * Returns value of timer for speed boost power up
     *
     * @return speedBoost : returns timer value
     */
    public static float getSpeedBoostTimer() {
        return speedBoost;
    }

    /**
     * Sets timer for speed boost power up
     *
     * @param value Value for timer
     */
    public static void setSpeedBoostTimer(float value) {
        speedBoost = value;
    }

    /**
     * Returns value of timer for absorption heart power up
     *
     * @return absorptionHeart : returns timer value
     */
    public static float getAbsorptionHeartTimer() {
        return absorptionHeart;
    }

    /**
     * Sets timer for absorption heart power up
     *
     * @param value Value for timer
     */
    public static void setAbsorptionHeartTimer(float value) {
        absorptionHeart = value;
    }

    /**
     * Returns value of timer for faster shooting power up
     *
     * @return fasterShooting : returns timer value
     */
    public static float getFasterShootingTimer() {
        return fasterShooting;
    }

    /**
     * Sets timer for faster shooting power up
     *
     * @param value Value for timer
     */
    public static void setFasterShootingTimer(float value) {
        fasterShooting = value;
    }

    /**
     * Returns value of timer for freeze enemy power up
     *
     * @return freezeEnemy : returns timer value
     */
    public static float getFreezeEnemyTimer() {
        return freezeEnemy;
    }

    /**
     * Sets timer for freeze enemy power up
     *
     * @param value Value for timer
     */
    public static void setFreezeEnemyTimer(float value) {
        freezeEnemy = value;
    }

    /**
     * Resets hud timers for all power ups
     */
    public static void resetPowerUpTimers() {
        coinMagnet = (float) 0;
        speedBoost = (float) 0;
        absorptionHeart = (float) 0;
        fasterShooting = (float) 0;
        freezeEnemy = (float) 0;
    }

    /**
     * Changes health by value increase
     *
     * @param value Increase to health
     */
    public static void changeHealth(int value) {
        health += value;
        if (healthLabel != null) {
            healthLabel.setText(String.format("%02d", health));
        }
    }

    /**
     * Sets health to specific value
     * @param value The amount of health that the player's health is set to
     */
    public static void setHealth(int value){
        if (value > 0){
            health = value;
        }
    }

    /**
     * Changes coins by value increase
     *
     * @param value Increase to coins
     */
    public static void changeCoins(int value) {
        if (value > 0) {
            coins += value * coinMulti;
            coinLabel.setText(String.format("%03d", coins));
        }
    }


    /**
     * Changes points by value increase
     *
     * @param value Increase to points
     */
    public static void changePoints(int value) {
        score += value;
        scoreLabel.setText(String.format("%03d", score));
        //Check if a points boundary is met
        SkillTreeScreen.pointsCheck(score);
    }

    /**
     * Set the nubmer of points
     * @param value The number of points
     */
    public static void setPoints(int value) {
        score = value;
        scoreLabel.setText(String.format("%03d", score));
    }

    /**
     * Changes health by value factor
     *
     * @param value Factor of coin increase
     */
    public static void changeCoinsMulti(int value) {
        coinMulti = coinMulti * value;
    }

    /**
     * Set the number of coins
     * @param value The number of coins
     */
    public static void setCoins (int value){
        coins = value;
    }

    /**
     * Changes the camera size, Scales the hud to match the camera
     *
     * @param width  the width of the viewable area
     * @param height the height of the viewable area
     */
    public static void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Returns health value
     *
     * @return health : returns health value
     */
    public static Integer getHealth() {
        return health;
    }

    /**
     * (Not Used)
     * Returns coins value
     *
     * @return health : returns coins value
     */
    public static Integer getCoins() {
        return coins;
    }

    public static Integer getPoints() {
        return score;
    }

    /**
     * Sets the value of the timer
     */
    public void setTimeCount(float time) {
        timeCount = time;
    }

    /**
     * Used to save the values for the hud from save
     * @param document The document mananging the save
     * @param hud The elemnent created for the hud
     */
    public static void save(Document document, Element hud) {
        Element gold = document.createElement("gold");
        gold.appendChild(document.createTextNode(Integer.toString(coins)));
        hud.appendChild(gold);

        Element score = document.createElement("score");
        score.appendChild(document.createTextNode(Integer.toString(Hud.score)));
        hud.appendChild(score);

        Element health = document.createElement("health");
        health.appendChild(document.createTextNode(Integer.toString(Hud.health)));
        hud.appendChild(health);

        Element coinMulti = document.createElement("coinMulti");
        coinMulti.appendChild(document.createTextNode(Integer.toString(Hud.coinMulti)));
        hud.appendChild(coinMulti);
    }

    /**
     * Used to load the values for the hud from save
     * @param hud The element storing the information
     */
    public static void load(Element hud) {
        setCoins(Integer.parseInt(hud.getElementsByTagName("gold").item(0).getTextContent()));
        health = (Integer.parseInt(hud.getElementsByTagName("health").item(0).getTextContent()));
        // changing health so the health displayed to the player is accurate
        changeHealth(0);
        coinMulti = (Integer.parseInt(hud.getElementsByTagName("coinMulti").item(0).getTextContent()));
    }

    /**
     * Disposes game data
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

