package com.triateq.gravitymaze.game.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.triateq.gravitymaze.game.GravityMaze;
import com.triateq.gravitymaze.core.ui.Menu;
import com.triateq.gravitymaze.core.ui.MenuStack;
import com.triateq.gravitymaze.core.game.Context;

public class LevelResult extends Menu {

    private static final Color BACKGROUND_COLOR = new Color(0.f, 0.f, 0.f, 0.5f);

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public LevelResult(Context context, MenuStack menuStack, int stars) {
        super(context, menuStack);

        AssetManager assetManager = context.getAssetManager();
        BitmapFont font = assetManager.get("ui/fonts/ReemKufi.ttf", BitmapFont.class);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        Label label = new Label("Stars: " + stars, labelStyle);
        label.setPosition(-label.getWidth() / 2, label.getHeight() / 2);

        Button button = new TextButton("Back to menu", textButtonStyle);
        button.setPosition(-button.getWidth() / 2, -button.getHeight() / 2);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                back();
            }
        });

        addActor(label);
        addActor(button);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(BACKGROUND_COLOR);
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
    public void back() {
        // Back to the main menu
        GravityMaze game = (GravityMaze) getContext().getGame();
        game.toMenu();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
