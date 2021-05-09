package com.majakkagames.gravitymaze.editorcore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.majakkagames.gravitymaze.core.events.EventHandler;
import com.majakkagames.gravitymaze.core.events.EventHandlerManager;
import com.majakkagames.gravitymaze.core.events.GameObjectEvent;
import com.majakkagames.gravitymaze.core.game.GameMap;
import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.core.game.Level;

public class GameObjectSelector implements Disposable {

    private static final Color SELECTION_COLOR = Color.GREEN.cpy();

    private final LevelManager levelManager;
    private GameObject selectedObject;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final EventHandlerManager<EventType, GameObjectEvent> eventHandlerManager = new EventHandlerManager<>();

    public GameObjectSelector(LevelManager levelManager) {
        this.levelManager = levelManager;
        levelManager.addEventHandler(LevelManager.EventType.CHANGED, e -> resetSelection());
    }

    public void select(GameObject gameObject) {
        selectedObject = gameObject;
        eventHandlerManager.fire(EventType.SELECTED, new GameObjectEvent(gameObject));
    }

    public void resetSelection() {
        select(null);
    }

    public void removeSelectedObject() {
        GameObjectStore store = levelManager.getLevel().getGameObjectStore();
        store.remove(selectedObject);
        resetSelection();
    }

    public boolean hasSelectedObject() {
        if (selectedObject == null) return false;
        GameObjectStore store = levelManager.getLevel().getGameObjectStore();
        return store.contains(selectedObject);
    }

    public GameObject getSelectedObject() {
        return selectedObject;
    }

    public void draw(Batch batch) {
        Level level = levelManager.getLevel();
        if (level.onPause() && hasSelectedObject()) {
            GameMap<?> gameMap = level.getGameObjectStore().getAnyGameObject(GameMap.class);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.translate(gameMap.getX(), gameMap.getY(), 0);
            shapeRenderer.setColor(SELECTION_COLOR);
            Gdx.gl20.glLineWidth(4);
            shapeRenderer.rect(selectedObject.getX() * selectedObject.getWidth(),
                    selectedObject.getY() * selectedObject.getHeight(),
                    selectedObject.getWidth(), selectedObject.getHeight());
            shapeRenderer.end();
        }
    }

    public void addEventHandler(EventType type, EventHandler<GameObjectEvent> handler) {
        eventHandlerManager.add(type, handler);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    public enum EventType {
        SELECTED
    }
}
