package com.mygdx.pirategame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

/**
 * Death Screen
 * Tells the player that they have died and the game is over when player has no health remaining
 *
 *@author Sam Pearson
 *@version 1.0
 */
public class DeathScreen implements Screen {

    private final PirateGame parent;
    private final Stage stage;

    /**
     * Creates a new screen
     *
     * @param pirateGame Game data
     */
    public DeathScreen(PirateGame pirateGame){
        parent = pirateGame;
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Shows new screen
     */
    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Table for "you died" text and points scored
        Table textTable = new Table();
        textTable.center();
        textTable.setFillParent(true);

        // Table for buttons
        Table buttonsTable = new Table();
        buttonsTable.center();
        buttonsTable.setFillParent(true);


        Label deathMsg = new Label("YOU  DIED", new Label.LabelStyle(new BitmapFont(), Color.RED));
        deathMsg.setFontScale(4f);
        textTable.add(deathMsg).center();
        textTable.row().pad(10, 0, 10, 0);

        Integer points = Hud.getPoints();
        Label pointsMsg = new Label("You scored " + points.toString() + " points!", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        pointsMsg.setFontScale(3f);
        textTable.add(pointsMsg).center();

        stage.addActor(textTable);

        // Create buttons
        TextButton replayButton = new TextButton("Replay", skin);
        TextButton backButton = new TextButton("Return To Menu", skin);

        // Add buttons to table to display on screen
        buttonsTable.add(replayButton).fillX().uniformX();
        buttonsTable.row().pad(10, 0, 10, 0);
        buttonsTable.add(backButton).fillX().uniformX();
        buttonsTable.bottom();

        // Add buttons table to stage
        stage.addActor(buttonsTable);

        // Handle when buttons are clicked
        replayButton.addListener(new ChangeListener() {
             @Override
             public void changed(ChangeEvent event, Actor actor) {
                 // Start new game
                parent.changeScreen(PirateGame.GAME);
                parent.killEndScreen();
             }
        });

        backButton.addListener(new ChangeListener() {
            /**
             * Switches screen
             * Returns to menu
             *
             * @param event Updates system event state to meny
             * @param actor updates scene
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Go to main menu screen
                parent.changeScreen(PirateGame.MENU);
                parent.killEndScreen();
            }
        });


    }

    /**
     * (Not Used)
     * Updates the state of each object
     */
    public void update(){
    }

    /**
     * Renders visual data with delta time
     *
     * @param dt Delta time (elapsed time since last game tick)
     */
    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    /**
     * Changes the camera size
     *
     * @param width the width of the viewable area
     * @param height the height of the viewable area
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
     * (Not Used)
     * Hides game
     */
    @Override
    public void hide() {
    }

    /**
     * (Not Used)
     * Disposes game data
     */
    @Override
    public void dispose() {
    }
}
