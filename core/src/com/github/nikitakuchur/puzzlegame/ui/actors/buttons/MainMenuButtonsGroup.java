package com.github.nikitakuchur.puzzlegame.ui.actors.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;

public class MainMenuButtonsGroup extends ButtonsGroup {
    private final BitmapFont font;
    private final TextButton startButton;
    private final MainMenuScreen screen;

    public MainMenuButtonsGroup(MainMenuScreen screen) {
        this.screen = screen;
        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Start button
        startButton = new TextButton("Start", textButtonStyle);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.setGameScreen();
            }
        });
        this.addActor(startButton);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        startButton.setPosition(-startButton.getWidth()/2, 0);
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public Object copy() {
        return new MainMenuButtonsGroup(screen);
    }
}
