package com.github.nikitakuchur.puzzlegame.ui.actors;

import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.level.LevelLoader;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.actors.buttons.Copyable;

import java.io.IOException;

public class GameLabels extends Group implements Disposable, Copyable {

    private final GameScreen gameScreen;

    private final BitmapFont font;

    private final Label fpsLabel;
    private final Label scoreLabel;

    /**
     * Creates a new ui for the game screen.
     *
     * @param gameScreen the game screen
     */
    public GameLabels(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

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
        fpsLabel.setPosition(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2);
        scoreLabel.setPosition(0, (float) Gdx.graphics.getHeight() / 2 - (float) Gdx.graphics.getHeight() / 20);

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        scoreLabel.setText(gameScreen.getLevel().getScore());
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public GameLabels copy() {
        return new GameLabels(gameScreen);
    }
}
