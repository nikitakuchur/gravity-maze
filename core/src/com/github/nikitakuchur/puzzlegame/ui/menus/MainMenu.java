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
import com.github.nikitakuchur.puzzlegame.screens.LevelScreen;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;

public class MainMenu extends Menu<MainMenuScreen> implements Disposable {
    private final BitmapFont font;

    public MainMenu(MenuStack menuStack, MainMenuScreen mainMenuScreen) {
        super(menuStack, mainMenuScreen);

        Image background = new Image(new Texture(Gdx.files.internal("ui/menu/bg1.png"), true));
        background.setPosition(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2);
        background.setWidth(Gdx.graphics.getWidth());
        background.setHeight(Gdx.graphics.getHeight());

        this.addActor(background);

        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Start button
        TextButton startButton = new TextButton("Start", textButtonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game game = getGameScreen().getGame();
                game.setScreen(new LevelScreen(game));
            }
        });
        this.addActor(startButton);

        startButton.setPosition(-startButton.getWidth() / 2, 0);
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
