package com.github.nikitakuchur.puzzlegame.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;

public class Context {

    private final Game game;
    private final AssetManager assetManager;
    private final GameScreen gameScreen;

    private Context(Builder builder) {
        this.game = builder.game;
        this.assetManager = builder.assetManager;
        this.gameScreen = builder.gameScreen;
    }

    public Game getGame() {
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

    public static class Builder {
        private Game game;
        private AssetManager assetManager;
        private GameScreen gameScreen;

        public Builder fill(Context context) {
            this.game = context.game;
            this.assetManager = context.assetManager;
            this.gameScreen = context.gameScreen;
            return this;
        }

        public Builder game(Game game) {
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
