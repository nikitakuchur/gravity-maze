package com.triateq.gravitymaze.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.triateq.gravitymaze.GravityMaze;
import com.triateq.gravitymaze.screens.GameScreen;

public class Context {

    private final GravityMaze game;
    private final AssetManager assetManager;
    private final GameScreen gameScreen;

    private Context(Builder builder) {
        this.game = builder.game;
        this.assetManager = builder.assetManager;
        this.gameScreen = builder.gameScreen;
    }

    public GravityMaze getGame() {
        return game;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder from(Context context) {
        Builder builder = new Builder();
        builder.game = context.game;
        builder.assetManager = context.assetManager;
        builder.gameScreen = context.gameScreen;
        return builder;
    }

    public static class Builder {
        private GravityMaze game;
        private AssetManager assetManager;
        private GameScreen gameScreen;

        public Builder game(GravityMaze game) {
            this.game = game;
            return this;
        }

        public Builder assetManager(AssetManager assetManager) {
            this.assetManager = assetManager;
            return this;
        }

        public Builder gameScreen(GameScreen gameScreen) {
            this.gameScreen = gameScreen;
            return this;
        }

        public Context build() {
            return new Context(this);
        }
    }
}
