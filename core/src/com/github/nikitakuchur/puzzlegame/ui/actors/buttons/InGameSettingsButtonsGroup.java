package com.github.nikitakuchur.puzzlegame.ui.actors.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.level.LevelLoader;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.GameUIActors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InGameSettingsButtonsGroup extends ButtonsGroup {
    private final GameScreen gameScreen;
    private final GameUIActors actors;
    private List<TextButton> buttons = new ArrayList<>();

    private final BitmapFont font;

    public InGameSettingsButtonsGroup(GameScreen gameScreen, GameUIActors actors) {
        this.gameScreen = gameScreen;
        this.actors = actors;

        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Back button
        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actors.popFromStage();
            }
        });
        this.addActor(backButton);

        // Return button
        TextButton returnButton = new TextButton("Return", textButtonStyle);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.setMainMenuScreen();
            }
        });
        this.addActor(returnButton);

        // Exit button
        TextButton exitButton = new TextButton("Exit", textButtonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        this.addActor(exitButton);

        // Reset button
        TextButton resetButton = new TextButton("Reset", textButtonStyle);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Restart the level
                try {
                    Level level = LevelLoader.load(Gdx.files.internal("levels/sample.json"));
                    gameScreen.setLevel(level);
                } catch (IOException e) {
                    Gdx.app.error("GameUI", e.getMessage());
                }

                actors.popFromStage();
            }
        });
        this.addActor(resetButton);
        ;

        this.buttons.add(backButton);
        this.buttons.add(exitButton);
        this.buttons.add(resetButton);
        this.buttons.add(returnButton);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);


        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPosition(-buttons.get(i).getWidth() / 2,
                    0 + buttons.get(0).getHeight() * (buttons.size() / 2) - buttons.get(0).getHeight() * i - 10 * i);
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public InGameSettingsButtonsGroup copy() {
        return new InGameSettingsButtonsGroup(gameScreen, actors);
    }
}
