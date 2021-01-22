package com.github.nikitakuchur.puzzlegame.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.github.nikitakuchur.puzzlegame.level.Level;
import com.github.nikitakuchur.puzzlegame.screens.LevelScreen;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.ui.Menu;
import com.github.nikitakuchur.puzzlegame.utils.Context;

public class LevelUI extends Menu {

    private final BitmapFont font;

    private final Label fpsLabel;
    private final Label scoreLabel;

    public LevelUI(Context context, MenuStack menuStack) {
        super(context, menuStack);

        AssetManager assetManager = context.getAssetManager();
        font = assetManager.get("ui/fonts/Roboto.ttf", BitmapFont.class);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Level menu button
        TextButton levelMenuButton = new TextButton("#", textButtonStyle);
        levelMenuButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 2 * levelMenuButton.getWidth() - 20,
                (float) Gdx.graphics.getHeight() / 2 - 1.5f * levelMenuButton.getHeight());
        levelMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((LevelScreen) context.getGameScreen()).getLevel().setPause(true);
                getMenuStack().push(new LevelMenu(context, getMenuStack()));
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
        fpsLabel.setPosition(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2);
        this.addActor(fpsLabel);

        // Score label
        scoreLabel = new Label("", labelStyle);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition(0, (float) Gdx.graphics.getHeight() / 2 - (float) Gdx.graphics.getHeight() / 20);
        this.addActor(scoreLabel);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        Level level = ((LevelScreen) getContext().getGameScreen()).getLevel();
        scoreLabel.setText(level.getMoves());
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
