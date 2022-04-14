package com.mygdx.pirategame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.pirategame.save.GameScreen;
import com.mygdx.pirategame.save.XmlSaveLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main menu is the first screen the player sees. Allows them to navigate where they want to go to
 * @author Sam Pearson
 * @version 1.0
 */
public class MainMenu implements Screen {

    private final PirateGame parent;
    private final Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;

    /**
     * Instantiates a new Main menu.
     *
     * @param PirateGame the main starting body of the game. Where screen swapping is carried out.
     */
    public MainMenu(PirateGame PirateGame){
        parent = PirateGame;
        stage = new Stage(new ScreenViewport());
    }

    /**
     * What should be displayed on the options screen
     *
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
        TextButton newGame = new TextButton("New Game", skin);
        TextButton loadGame = new TextButton("Load Game", skin);
        TextButton help = new TextButton("Help", skin);
        TextButton options = new TextButton("Options", skin);
        TextButton exit = new TextButton("Exit", skin);

        //add buttons to table
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(loadGame).fillX().uniformX();
        table.row();
        table.add(help).fillX().uniformX();
        table.row();
        table.add(options).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        //add listeners to the buttons

        //Start a game
        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.setScreen(new DifficultyScreen(parent));
            }
        });

        // load a game
        loadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Game File (.spice)", "spice");
                fc.setFileFilter(filter);
                int returnVal = fc.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null){
                    XmlSaveLoader xmlLoader = new XmlSaveLoader(fc.getSelectedFile());
                    new GameScreen(parent, xmlLoader);
                    if(xmlLoader.success()) {
                        parent.changeScreen(new GameScreen(parent, new XmlSaveLoader(fc.getSelectedFile())));
                    }
                }

            }
        });

        //Help Screen
        help.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.changeScreen(PirateGame.HELP);
            }
        });

        //Go to edit options
        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                parent.setScreen(new OptionsScreen(parent,parent.getScreen()));
            }
        });


        //Quit game
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
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




