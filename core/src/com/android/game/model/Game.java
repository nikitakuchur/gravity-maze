package com.android.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
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

        Vector2 size = new Vector2((float) Gdx.graphics.getWidth() / 4, (float) Gdx.graphics.getWidth() / 8);
        Vector2 position = new Vector2((float) Gdx.graphics.getWidth() / 3 - size.x / 2,
                                       (float) Gdx.graphics.getHeight() / 10);

        Button backButton = new Button(position, size, "Back");

        position = new Vector2(2 * (float) Gdx.graphics.getWidth() / 3 - size.x / 2,
                (float) Gdx.graphics.getHeight() / 10);

        Button resetButton = new Button(position, size, "Reset");

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
