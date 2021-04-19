package com.triateq.gravitymaze.game.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.triateq.gravitymaze.core.game.Level;
import com.triateq.gravitymaze.game.GravityMaze;
import com.triateq.gravitymaze.game.gameobjects.LevelProperties;
import com.triateq.gravitymaze.game.screens.LevelScreen;
import com.triateq.gravitymaze.core.ui.MenuStack;
import com.triateq.gravitymaze.core.ui.Menu;
import com.triateq.gravitymaze.game.ui.utils.MenuUtils;
import com.triateq.gravitymaze.core.game.Context;

public class LevelUI extends Menu {

    private final BitmapFont font;

    private final Label fpsLabel;
    private final Label scoreLabel;

    public LevelUI(Context context, MenuStack menuStack) {
        super(context, menuStack);

        AssetManager assetManager = context.getAssetManager();
        font = assetManager.get("ui/fonts/ReemKufi.ttf", BitmapFont.class);

        addActor(MenuUtils.createBackButton(assetManager, menuStack));

        Button resetButton = MenuUtils.createButton(assetManager.get("ui/menu/reset.png"), assetManager.get("ui/menu/reset_down.png"));
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Reset the level
                ((LevelScreen) context.getGameScreen()).resetLevel();
            }
        });
        resetButton.setPosition(
                (float) Gdx.graphics.getWidth() / 2 - resetButton.getWidth() - resetButton.getWidth() / 3,
                (float) Gdx.graphics.getHeight() / 2 - 1.4f * resetButton.getHeight()
        );
        addActor(resetButton);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // FPS label
        fpsLabel = new Label("FPS: 0", labelStyle);
        fpsLabel.setFontScale(0.5f);
        fpsLabel.setAlignment(Align.bottomLeft);
        fpsLabel.setPosition(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2);
        addActor(fpsLabel);

        // Score label
        scoreLabel = new Label("", labelStyle);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition(0, (float) Gdx.graphics.getHeight() / 2 - (float) Gdx.graphics.getHeight() / 25);
        addActor(scoreLabel);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        Level level = ((LevelScreen) getContext().getGameScreen()).getLevel();

        LevelProperties properties = level.getGameObjectStore()
                .getAnyGameObjectOrThrow(LevelProperties.class, () -> new IllegalStateException("Cannot find the properties object"));
        scoreLabel.setText(properties.getMoves());
    }

    @Override
    public void back() {
        // Back to the main menu
        // TODO: Are you sure you want to exit?
        GravityMaze game = (GravityMaze) getContext().getGame();
        game.toMenu();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
