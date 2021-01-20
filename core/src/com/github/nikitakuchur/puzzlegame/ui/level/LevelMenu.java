package com.github.nikitakuchur.puzzlegame.ui.level;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.screens.LevelScreen;
import com.github.nikitakuchur.puzzlegame.screens.MenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.ui.Menu;
import com.github.nikitakuchur.puzzlegame.utils.Context;

import java.util.ArrayList;
import java.util.List;

public class LevelMenu extends Menu implements Disposable {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public LevelMenu(Context context, MenuStack menuStack) {
        super(context, menuStack);

        AssetManager assetManager = context.getAssetManager();
        BitmapFont font = assetManager.get("ui/fonts/Roboto.ttf", BitmapFont.class);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Back button
        TextButton backButton = new TextButton("Back", textButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getMenuStack().pop();
                ((LevelScreen) context.getGameScreen()).getLevel().setPause(false);
            }
        });
        this.addActor(backButton);

        // Return button
        TextButton returnButton = new TextButton("Return", textButtonStyle);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Back to the main menu
                Game game = context.getGame();
                game.setScreen(new MenuScreen(Context.builder()
                        .game(context.getGame())
                        .assetManager(context.getAssetManager())
                        .build()));
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
                // Reset the level
                ((LevelScreen) context.getGameScreen()).resetLevel();
                getMenuStack().pop();
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
                    buttons.get(0).getHeight() * ((float) buttons.size() / 2) - buttons.get(0).getHeight() * i - 10 * i
            );
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.f, 0.f, 0.f, 0.5f));
        shapeRenderer.rect(
                -(float) Gdx.graphics.getWidth() / 2.f,
                -(float) Gdx.graphics.getHeight() / 2.f,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
        shapeRenderer.identity();
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
