package com.github.nikitakuchur.puzzlegame.ui.menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.screens.LevelScreen;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.utils.Context;

public class MainMenu extends Menu implements Disposable {
    private final BitmapFont font;

    public MainMenu(Context context, MenuStack menuStack) {
        super(context, menuStack);

        Image background = new Image(new Texture(Gdx.files.internal("ui/menu/bg1.png"), true));
        background.setPosition(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2);
        background.setWidth(Gdx.graphics.getWidth());
        background.setHeight(Gdx.graphics.getHeight());

        this.addActor(background);

        AssetManager assetManager = context.getAssetManager();
        font = assetManager.get("ui/fonts/Roboto.ttf", BitmapFont.class);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Start button
        TextButton startButton = new TextButton("Start", textButtonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Game game = context.getGame();
                game.setScreen(new LevelScreen(context));
            }
        });
        this.addActor(startButton);

        startButton.setPosition(-startButton.getWidth() / 2, 0);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
