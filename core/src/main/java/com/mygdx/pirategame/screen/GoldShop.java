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
import com.mygdx.pirategame.gameobjects.enemy.EnemyShip;
import com.mygdx.pirategame.save.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


import static com.mygdx.pirategame.save.GameScreen.GAME_RUNNING;
import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.mygdx.pirategame.save.GameScreen.game;


public class GoldShop implements Screen {

    private final PirateGame parent;
    private final Stage stage;
    /*private ShapeRenderer shapeRenderer;
    private Rectangle rectangle;*/
    public boolean display = false;
    private OrthographicCamera camera;
    private GameScreen gameScreen;
    private Sound purchaseSound;

    //To store whether buttons are enabled or disabled
    private static final List<Integer> states = new ArrayList<Integer>();

    /**
     * adding default states
     */
    static {
        //0 = enabled, 1 = disabled
        states.add(0);
        states.add(0);
        states.add(0);
        states.add(1);
    }

    private static TextButton fasterCannonBtn;
    private TextButton healthBoostBtn;
    private TextButton increaseCannonDamageBtn;
    private TextButton item4;

    public final int fasterCannonPrice = 50;
    public final int healthBoostPrice = 75;
    public final int increaseCannonDamagePrice = 150;

    public final int healthBoostValue = 50; // The amount health is increased by when health boost is purchased

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

        /*// Shop background
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        rectangle = new Rectangle(stage.getCamera().position.x - stage.getCamera().viewportWidth / 5f, stage.getCamera().position.y - stage.getCamera().viewportHeight / 2.4f,
                stage.getCamera().viewportWidth / 2.5f, stage.getCamera().viewportHeight / 1.2f);*/


        //create skill tree buttons
        fasterCannonBtn = new TextButton("Cannon ball speed +20%", skin);

        //Sets enabled or disabled
        if (states.get(0) == 1){
            fasterCannonBtn.setDisabled(true);
        }
        healthBoostBtn = new TextButton("Health Boost +50", skin);
        if (states.get(1) == 1){
            healthBoostBtn.setDisabled(true);
        }
        increaseCannonDamageBtn = new TextButton("Increase Cannon Damage +20%", skin);
        if (states.get(2) == 1){
            increaseCannonDamageBtn.setDisabled(true);
        }

        item4 = new TextButton("????????????", skin);

        if (states.get(3) == 1){
            item4.setDisabled(true);

        }

        // Item price labels
        final Label unlock100 = new Label("50 gold",skin);
        final Label unlock200 = new Label("75 gold",skin);
        final Label unlock300 = new Label("150 gold",skin);
        final Label unlock400 = new Label("400 gold",skin);
        final Label goldShop = new Label("Gold Shop",skin);
        goldShop.setFontScale(1.2f);

        //Return Button
        TextButton closeButton = new TextButton("Close", skin);

        // Close the gold shop
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
        table.add(unlock100);
        table.row().pad(10, 0, 10, 0);
        table.add(healthBoostBtn).width(stage.getCamera().viewportWidth / 5f).height(stage.getCamera().viewportHeight / 9f);
        table.add(unlock200);
        table.row().pad(10, 0, 10, 0);
        table.add(increaseCannonDamageBtn).width(stage.getCamera().viewportWidth / 5f).height(stage.getCamera().viewportHeight / 9f);
        table.add(unlock300);
        table.row().pad(10, 0, 10, 0);
        table.add(item4).width(stage.getCamera().viewportWidth / 5f).height(stage.getCamera().viewportHeight / 9f);
        table.add(unlock400);
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

    private void displayMsg(String title, String msg, String msgType){
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
            if (currentVelocity * 1.2f <= 12) {
                Hud.setCoins(Hud.getCoins() - fasterCannonPrice);
                Hud.updateCoins();
                int newVelocity = ceil(currentVelocity * 1.2f);
                player.setCannonVelocity(newVelocity);
                playPurchaseSound();
                displayMsg("Success", "Your cannon now fires 20% faster!","info");

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
            for (College col : gameScreen.getColleges().values()){

                col.damage = Math.round(col.damage * 1.2f);
            }

            // Iterate through each enemy ship and increase damage
            for (EnemyShip ship: gameScreen.getEnemyShips()){
                ship.damage = Math.round(ship.damage * 1.2f);
            }

            playPurchaseSound();
            JOptionPane.showMessageDialog(null, "Cannon damage has been increased by 20%", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "You do not have enough coins to purchase this powerup", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        /*// Render background color of shop
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        shapeRenderer.end();

        // Render border around background
        shapeRenderer.begin();
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth()+1, rectangle.getHeight()+1);
        shapeRenderer.end();*/


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

    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        stage.dispose();
        //shapeRenderer.dispose();
    }

    public Player getPlayer(){
        return gameScreen.getPlayer();
    }
}
