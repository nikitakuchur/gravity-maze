package com.android.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Camera camera;
    private Map map;
    private List<Button> buttons;

    public Game() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.position.set((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 0);
        this.camera.update();

        map = new Map();

        // Buttons
        buttons = new ArrayList<Button>();

        Button backButton = new Button(
                new Vector2((float) Gdx.graphics.getWidth() / 3 - 50, (float) Gdx.graphics.getHeight() / 10),
                new Vector2((float) Gdx.graphics.getWidth() / 4, (float) Gdx.graphics.getWidth() / 8), "Back");
        backButton.setColor(new Color(1, 0, 0, 1));

        Button resetButton = new Button(
                new Vector2(2 * (float) Gdx.graphics.getWidth() / 3 - 50, (float) Gdx.graphics.getHeight() / 10),
                new Vector2((float) Gdx.graphics.getWidth() / 4, (float) Gdx.graphics.getWidth() / 8), "Reset");
        resetButton.setColor(new Color(0, 0, 1, 1));

        buttons.add(backButton);
        buttons.add(resetButton);
    }

    /**
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * @return the map
     */
    public Map getMap() {
        return map;
    }

    public List<Button> getButtons() {
        return buttons;
    }
}
