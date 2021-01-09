package com.github.nikitakuchur.puzzlegame.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.screens.LevelScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;

public class GameUI extends Menu<LevelScreen> implements Disposable {

    private final BitmapFont font;
    private final TextButton levelMenuButton;

    private final Label fpsLabel;
    private final Label scoreLabel;

    public GameUI(MenuStack menuStack, LevelScreen levelScreen) {
        super(menuStack, levelScreen);

        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Level menu button
        levelMenuButton = new TextButton("#", textButtonStyle);
        levelMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuStack().push(new LevelMenu(getMenuStack(), getGameScreen()));
            }
        });
        this.addActor(levelMenuButton);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // FPS label
        fpsLabel = new Label("FPS: 0", labelStyle);
        fpsLabel.setFontScale(0.5f);
        fpsLabel.setAlignment(Align.bottomLeft);
        this.addActor(fpsLabel);

        // Score label
        scoreLabel = new Label("", labelStyle);
        scoreLabel.setAlignment(Align.center);
        this.addActor(scoreLabel);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        levelMenuButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 2 * levelMenuButton.getWidth() - 20,
                (float) Gdx.graphics.getHeight() / 2 - 1.5f * levelMenuButton.getHeight());

        fpsLabel.setPosition(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2);
        scoreLabel.setPosition(0, (float) Gdx.graphics.getHeight() / 2 - (float) Gdx.graphics.getHeight() / 20);

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        scoreLabel.setText(getGameScreen().getLevel().getScore());
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
