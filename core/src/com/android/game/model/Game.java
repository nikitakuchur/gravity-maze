package com.android.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Camera camera;
    private Level level;
    private List<Button> buttons;

    /**
     * Creates a new game
     */
    public Game() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.position.set((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 0);
        this.camera.update();

        level = new Level();

        // Buttons
        buttons = new ArrayList<Button>();

        Vector2 size = new Vector2((float) Gdx.graphics.getWidth() / 4, (float) Gdx.graphics.getWidth() / 8);
        Vector2 position = new Vector2((float) Gdx.graphics.getWidth() / 3 - size.x / 2,
                (float) Gdx.graphics.getHeight() / 10);

        Button backButton = new Button(position, size, "Back");
        backButton.setOnAction(() -> Gdx.app.exit());

        position = new Vector2(2 * (float) Gdx.graphics.getWidth() / 3 - size.x / 2,
                (float) Gdx.graphics.getHeight() / 10);

        Button resetButton = new Button(position, size, "Reset");
        resetButton.setOnAction(() -> level.reset());

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
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @return the list of the buttons
     */
    public List<Button> getButtons() {
        return buttons;
    }
}
