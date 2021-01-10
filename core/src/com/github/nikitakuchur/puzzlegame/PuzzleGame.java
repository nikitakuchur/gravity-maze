package com.github.nikitakuchur.puzzlegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.utils.AssetLoader;
import com.github.nikitakuchur.puzzlegame.utils.Context;

/**
 * This is the main game class.
 */
public class PuzzleGame extends Game {

    private Context context;

    public void create() {
        AssetManager assetManager = new AssetManager();
        AssetLoader.load(assetManager);
        context = Context.builder()
                .game(this)
                .assetManager(assetManager)
                .build();
        setScreen(new MainMenuScreen(context));
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
