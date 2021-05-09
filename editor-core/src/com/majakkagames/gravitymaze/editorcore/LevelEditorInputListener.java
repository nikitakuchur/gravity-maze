package com.majakkagames.gravitymaze.editorcore;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.majakkagames.gravitymaze.core.game.GameMap;
import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.editorcore.config.Configuration;
import com.majakkagames.gravitymaze.editorcore.config.Configurator;
import com.majakkagames.gravitymaze.editorcore.config.GameObjectCreator;

import java.util.Comparator;

public class LevelEditorInputListener extends InputListener {

    private final Configurator configurator;

    private final LevelEditor editor;
    private final LevelManager levelManager;
    private final GameObjectSelector selector;

    private boolean moving;

    public LevelEditorInputListener(Configurator configurator, LevelEditor editor, GameObjectSelector selector) {
        this.configurator = configurator;
        this.editor = editor;
        this.levelManager = editor.getLevelManager();
        this.selector = selector;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        GameObjectStore store = getGameObjectStore();
        GameMap<?> gameMap = getGameMap();
        Vector2 position = gameMap.toMapCoords(x, y);
        int px = (int) position.x;
        int py = (int) position.y;

        GameObject currentGameObject = store.getGameObjects().stream().
                filter(object -> (int) object.getX() == px
                        && (int) object.getY() == py)
                .filter(object -> !Configuration.isGameObjectTransient(object.getClass())
                        && !Configuration.isGameObjectLocked(configurator, object.getClass()))
                .max(Comparator.comparingInt(GameObject::getLayer))
                .orElse(null);

        if (currentGameObject != null) {
            selector.select(currentGameObject);
            return true;
        }

        if (selector.hasSelectedObject()) {
            selector.resetSelection();
            return true;
        }

        if (gameMap.isOutside(px, py)) return false;

        GameObjectCreator gameObjectCreator = editor.getGameObjectCreator();
        if (gameObjectCreator != null) {
            GameObject gameObject = gameObjectCreator.create();
            gameObject.setX(px);
            gameObject.setY(py);
            gameObject.initialize(levelManager.getLevel());
            store.add(gameObject);
            selector.select(gameObject);
            editor.getLevelManager().makeSnapshot();
        }
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        GameMap<?> gameMap = getGameMap();
        Vector2 position = gameMap.toMapCoords(x, y);
        int px = (int) position.x;
        int py = (int) position.y;

        if (gameMap.isOutside(px, py)) return;

        if (selector.hasSelectedObject()) {
            selector.getSelectedObject().setPosition(px, py);
            moving = true;
        }
    }

    private GameObjectStore getGameObjectStore() {
        return levelManager.getLevel().getGameObjectStore();
    }

    private GameMap<?> getGameMap() {
        return getGameObjectStore().getAnyGameObject(GameMap.class);
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (moving) {
            levelManager.makeSnapshot();
            moving = false;
        }
    }
}
