package com.github.nikitakuchur.puzzlegame.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.level.LevelLoader;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetActorCommand;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetLevelCommand;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetScreenCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InGameSettingsGroup extends Group implements Disposable {
    private List<TextButton> buttons = new ArrayList<>();

    private final BitmapFont font;
    private final Texture menuBg = new Texture(Gdx.files.internal("game/menu/bg1.png"), true);

    public InGameSettingsGroup(SetScreenCommand screenCommand, SetActorCommand setActorCommand, SetLevelCommand setLevelCommand) {

        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Back button
        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setActorCommand.unexecute();
            }
        });
        this.addActor(backButton);

        // Return button
        TextButton returnButton = new TextButton("Return", textButtonStyle);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenCommand.setScreen(new MainMenuScreen(screenCommand, new Stage(new ScreenViewport())));
                screenCommand.execute();
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

                    setLevelCommand.setLevel(level);
                    setLevelCommand.execute();
                    setActorCommand.unexecute();
                } catch (IOException e) {
                    Gdx.app.error("GameUI", e.getMessage());
                }
            }
        });
        this.addActor(resetButton);

        this.buttons.add(backButton);
        this.buttons.add(exitButton);
        this.buttons.add(resetButton);
        this.buttons.add(returnButton);

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPosition(-buttons.get(i).getWidth() / 2,
                    0 + buttons.get(0).getHeight() * ((float) buttons.size() / 2) - buttons.get(0).getHeight() * i - 10 * i);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        batch.draw(menuBg, -(float) Gdx.graphics.getWidth() / 4, -(float) Gdx.graphics.getHeight() / 4, (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
