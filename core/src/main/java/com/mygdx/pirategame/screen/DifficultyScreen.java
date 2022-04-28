package com.mygdx.pirategame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.pirategame.PirateGame;

/**
 * Screen to give player option to select game difficulty
 * @author Charlie Crosley, Dan Wade
 */
public class DifficultyScreen implements Screen {

    private final PirateGame parent;
    private final Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;

    /**
     * Instantiates a new difficulty screen.
     *
     * @param PirateGame the main starting body of the game. Where screen swapping is carried out.
     */
    public DifficultyScreen(PirateGame PirateGame){
        parent = PirateGame;
        stage = new Stage(new ScreenViewport());
    }

    /**
     * What should be displayed on the difficulty screen
     */
    @Override
    public void show() {
        backgroundTexture = new Texture("map_blurred.png");

        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setScaling(Scaling.stretch);
        stage.addActor(backgroundImage);

        //Set the input processor
        Gdx.input.setInputProcessor(stage);
        // Create a table for the buttons
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //The skin for the actors
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //create buttons
        TextButton easy = new TextButton("Easy", skin);
        TextButton normal = new TextButton("Normal", skin);
        TextButton hard = new TextButton("Hard", skin);

        //add buttons to table
        table.add(easy).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(normal).fillX().uniformX();
        table.row();
        table.add(hard).fillX().uniformX();
        table.row();

        //add listeners to the buttons

        //Start a game
        easy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.changeDifficulty(PirateGame.EASY);
                parent.changeScreen(PirateGame.GAME);
            }
        });
        //Help Screen
        normal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.changeDifficulty(PirateGame.NORMAL);
                parent.changeScreen(PirateGame.GAME);
            }
        });

        //Go to edit options
        hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.changeDifficulty(PirateGame.HARD);
                parent.changeScreen(PirateGame.GAME);
            }
        });
    }

    /**
     * Renders the visual data for all objects
     * @param delta Delta Time
     */
    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.draw();
    }

    /**
     * Changes the camera size, Scales the hud to match the camera
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
     * Disposes game data
     */
    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}




