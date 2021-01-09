package com.github.nikitakuchur.puzzlegame.ui.menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.level.LevelLoader;
import com.github.nikitakuchur.puzzlegame.screens.LevelScreen;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelMenu extends Menu<LevelScreen> implements Disposable {
    private final BitmapFont font;

    public LevelMenu(MenuStack menuStack, LevelScreen levelScreen) {
        super(menuStack, levelScreen, "bg1.png");

        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Back button
        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuStack().pop();
            }
        });
        this.addActor(backButton);

        // Return button
        TextButton returnButton = new TextButton("Return", textButtonStyle);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Back to the main menu
                Game game = getGameScreen().getGame();
                game.setScreen(new MainMenuScreen(game));
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
                    getGameScreen().setLevel(level);
                } catch (IOException e) {
                    Gdx.app.error("GameMenu", e.getMessage());
                }
            }
        });
        this.addActor(resetButton);

        List<TextButton> buttons = new ArrayList<>();
        buttons.add(backButton);
        buttons.add(exitButton);
        buttons.add(resetButton);
        buttons.add(returnButton);

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPosition(
                    -buttons.get(i).getWidth() / 2,
                    0 + buttons.get(0).getHeight() * ((float) buttons.size() / 2) - buttons.get(0).getHeight() * i - 10 * i
            );
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);

        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
