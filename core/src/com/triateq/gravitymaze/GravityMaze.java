package com.triateq.gravitymaze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.triateq.gravitymaze.screens.MenuScreen;
import com.triateq.gravitymaze.utils.AssetLoader;
import com.triateq.gravitymaze.utils.Context;

/**
 * This is the main game class.
 */
public class GravityMaze extends Game {

    private Context context;

    public void create() {
        AssetManager assetManager = new AssetManager();
        AssetLoader.load(assetManager);
        context = Context.builder()
                .game(this)
                .assetManager(assetManager)
                .build();
        setScreen(new MenuScreen(context));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        if (delta > 0.05f) delta = 0.05f;
        screen.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        context.getAssetManager().dispose();
    }
}
