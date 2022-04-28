package com.mygdx.pirategame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.pirategame.Hud;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.gameobjects.Player;
import com.mygdx.pirategame.gameobjects.enemy.College;
import com.mygdx.pirategame.gameobjects.enemy.CollegeMetadata;
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.save.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.mygdx.pirategame.save.GameScreen.GAME_RUNNING;
import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.mygdx.pirategame.save.GameScreen.game;

/**
 * Dan Wade, Charlie Crosley, Robert Murphy
 */
public class GoldShop implements Screen {

    private final PirateGame parent;
    private final Stage stage;

    public boolean display = false;
    private OrthographicCamera camera;
    private GameScreen gameScreen;
    private Sound purchaseSound;


    private static TextButton fasterCannonBtn;
    private TextButton healthBoostBtn;
    private TextButton increaseCannonDamageBtn;
    private TextButton closeButton;

    // Updating these values here will automatically update buttons, labels and tests
    public final int fasterCannonPrice = 50;
    public final int healthBoostPrice = 75;
    public final int increaseCannonDamagePrice = 150;
    public final float fasterCannonMultiplier = 1.2f;
    public final int healthBoostValue = 50;
    public final float increaseCannonDamageMultiplier = 1.2f;

    /**
     * Create a new gold shop with the specified parameters
     * @param pirateGame The PirateGame controlling runtime
     * @param camera The camera controlling rendering
     * @param gameScreen The GameScreen that this ship is associated with
     */
    public GoldShop(PirateGame pirateGame, OrthographicCamera camera, GameScreen gameScreen) {
        this.parent = pirateGame;
        this.camera = camera;
        this.gameScreen = gameScreen;
        stage = new Stage(new ScreenViewport());

        // Coins handling sound effect https://mixkit.co/free-sound-effects/money/
        purchaseSound = Gdx.audio.newSound(Gdx.files.internal("sfx_and_music/coin-purchase.wav"));

    }

    /**
     * Called when this screen becomes the current screen
     */
    @Override
    public void show() {
        // Renders the shop in GameScreen if display is true
        display = true;

        //The skin for the actors
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //Set the input processor
        Gdx.input.setInputProcessor(stage);
        // Create a table that fills the screen
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Create buttons
        fasterCannonBtn = new TextButton("Cannon ball speed +" + multiplierToPercent(fasterCannonMultiplier), skin);
        healthBoostBtn = new TextButton("Health Boost +" + healthBoostValue, skin);
        increaseCannonDamageBtn = new TextButton("Increase Cannon Damage +" + multiplierToPercent(increaseCannonDamageMultiplier), skin);
        closeButton = new TextButton("Close", skin);

        // Item price labels
        final Label fasterCannonPriceLabel = new Label(fasterCannonPrice + " gold",skin);
        final Label healthBoostPriceLabel = new Label(healthBoostPrice + " gold",skin);
        final Label increaseCannonDamageLabel = new Label(increaseCannonDamagePrice + " gold",skin);
        final Label goldShop = new Label("Gold Shop",skin);
        goldShop.setFontScale(1.2f);


        // Add listener for close shop button
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                display = false; // Stop the shop from being rendered
                dispose();
                GameScreen.gameStatus = GAME_RUNNING;
                gameScreen.closeShop();
            }
        });

        fasterCannonBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Purchase faster cannon
                purchaseFasterCannon();
            }
        });

        healthBoostBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                purchaseHealthBoost();
            }
        });

        increaseCannonDamageBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                purchaseIncreaseCannonDamage();
            }
        });

        //add buttons and labels to main table
        table.row().pad(100, 0, 10, 0);
        table.add(goldShop);
        table.row().pad(10, 0, 10, 0);
        table.add(fasterCannonBtn).width(stage.getCamera().viewportWidth / 5f).height(stage.getCamera().viewportHeight / 9f);
        table.add(fasterCannonPriceLabel);
        table.row().pad(10, 0, 10, 0);
        table.add(healthBoostBtn).width(stage.getCamera().viewportWidth / 5f).height(stage.getCamera().viewportHeight / 9f);
        table.add(healthBoostPriceLabel);
        table.row().pad(10, 0, 10, 0);
        table.add(increaseCannonDamageBtn).width(stage.getCamera().viewportWidth / 5f).height(stage.getCamera().viewportHeight / 9f);
        table.add(increaseCannonDamageLabel);
        table.row().pad(10, 0, 10, 0);
        table.add(closeButton).width(stage.getCamera().viewportWidth / 5f).height(stage.getCamera().viewportHeight / 9f);
        table.top();
    }

    /**
     * Plays sound when player purchases from the shop
     */
    public void playPurchaseSound(){
        if (parent.getPreferences().isEffectsEnabled()) {
            purchaseSound.play(parent.getPreferences().getEffectsVolume());
        }
    }

    /**
     * Display a pop up JOptionPane message to the user
     * @param title The title of the pop up message which the user sees
     * @param msg The message you want to show the user
     * @param msgType The type of message (either "error" or "info"), this determines the style of the JOptionPane
     */
    private void displayMsg(String title, String msg, String msgType){
        // If camera is null, then it is likely we are testing headlessly so do not need popup messages
        if (camera != null){
            if (msgType == "error"){
                JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
            } else if (msgType == "info"){
                JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    /**
     * Method which handles purchase of Faster cannon
     */
    public void purchaseFasterCannon(){
        Player player = getPlayer();
        // Check player has enough coins
        if (Hud.getCoins() >= fasterCannonPrice){
            int currentVelocity = player.getCannonVelocity();
            // Limit max velocity of cannon to 12, as players can
            // purchase this powerup multiple times
            if (currentVelocity * fasterCannonMultiplier <= 12) {
                Hud.setCoins(Hud.getCoins() - fasterCannonPrice);
                Hud.updateCoins();
                int newVelocity = ceil(currentVelocity * fasterCannonMultiplier);
                player.setCannonVelocity(newVelocity);
                playPurchaseSound();
                displayMsg("Success", "Your cannon now fires" + multiplierToPercent(fasterCannonMultiplier) + " faster!","info");

            } else {

                displayMsg("Error", "Cannot purchase again: you have maximised this powerup","error");
            }
        } else {
            displayMsg("Error","You do not have enough coins to purchase this powerup","error");
        }

    }

    /**
     * Method which handles purchase of health boost (i.e. repairs ship)
     */
    public void purchaseHealthBoost(){

        //Check if player has enough coins
        if (Hud.getCoins() >= healthBoostPrice){
            Hud.setCoins(Hud.getCoins() - healthBoostPrice);
            Hud.updateCoins();
            Hud.changeHealth(healthBoostValue);
            playPurchaseSound();
            displayMsg("Success","You have received a health boost of " + healthBoostValue + "!","info");
        } else {
            displayMsg("Error","You do not have enough coins to purchase this boost!","error");
        }

    }

    /**
     * Used to purchase the increase cannonball damage upgrade
     */
    public void purchaseIncreaseCannonDamage(){

        if (Hud.getCoins() >= increaseCannonDamagePrice){
            Hud.setCoins(Hud.getCoins() - increaseCannonDamagePrice);
            Hud.updateCoins();

            /**
             * Note we use the round function  when increasing damage.
             * Increasing by 20% will likely result in a decimal value,
             * so we round to give us an int (which is the variable type
             * of damage)
             */

            // Iterate through each college and increase damage
            for (College col : getColleges().values()){
                col.damage = Math.round(col.damage * increaseCannonDamageMultiplier);
            }

            // Iterate through each enemy ship and increase damage
            for (EnemyShip ship: getEnemyShips()){
                ship.damage = Math.round(ship.damage * increaseCannonDamageMultiplier);
            }

            playPurchaseSound();
            displayMsg("Success","Cannon damage has been increased by " + multiplierToPercent(increaseCannonDamageMultiplier),"info");
        } else {
            displayMsg("Error","You do not have enough coins to purchase this powerup", "error");
        }
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getViewport().getCamera().update();
    }

    /**
     * (Not Used)
     * Pauses game
     */
    @Override
    public void pause() {

    }

    /**
     * (Not Used)
     * Resumes game
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        // Disables all the buttons
        fasterCannonBtn.setDisabled(true);
        healthBoostBtn.setDisabled(true);
        increaseCannonDamageBtn.setDisabled(true);
        closeButton.setDisabled(true);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        stage.dispose();
        fasterCannonBtn.remove();
        healthBoostBtn.remove();
        increaseCannonDamageBtn.remove();
        closeButton.remove();
        //shapeRenderer.dispose();
    }

    /**
     * Get player object from game screen
     * This method is primarily to aid with testing when mocking
     * i.e. Mockito catches when this method is called and returns test values instead
     * @return Player object
     */
    public Player getPlayer(){
        return gameScreen.getPlayer();
    }

    /**
     * Get the HashMap of colleges
     * This method is primarily to aid with testing when mocking
     * i.e. Mockito catches when this method is called and returns test values instead
     * @return HashMap of colleges
     */
    public HashMap<CollegeMetadata, College> getColleges(){ return gameScreen.getColleges();}

    /**
     *  Get the list of all enemy ships
     *  This method is primarily to aid with testing when mocking
     *  i.e. Mockito catches when this method is called and returns test values instead
     * @return List of enemy ships
     */
    public ArrayList<EnemyShip> getEnemyShips() { return gameScreen.getEnemyShips();}

    /**
     * Converts a multipler (e.g 1.2f) to a percentage (e.g. 20%)
     * @param multiplier
     * @return String of percentage value of multiplier
     */
    private String multiplierToPercent(float multiplier){
        return (int) (multiplier * 100) - 100 + "%";
    }
}
