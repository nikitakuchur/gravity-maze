package com.android.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Game {
    private Camera camera;
    private Map map;

    public Game() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.position.set((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 0);
        this.camera.update();

        map = new Map();
    }

    public Camera getCamera() {
        return camera;
    }

    public Map getMap() {
        return map;
    }
}
