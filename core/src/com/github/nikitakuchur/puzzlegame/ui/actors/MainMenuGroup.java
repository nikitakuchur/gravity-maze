package com.github.nikitakuchur.puzzlegame.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetScreenCommand;

public class MainMenuGroup extends Group implements Disposable {
    private final BitmapFont font;

    private final Texture menuBg = new Texture(Gdx.files.internal("game/menu/bg1.png"), true);

    public MainMenuGroup(SetScreenCommand setScreenCommand) {
        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Start button
        TextButton startButton = new TextButton("Start", textButtonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setScreenCommand.setScreen(new GameScreen(setScreenCommand));
                setScreenCommand.execute();
            }
        });
        this.addActor(startButton);

        startButton.setPosition(-startButton.getWidth() / 2, 0);
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
