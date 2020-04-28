package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.editor.utils.GameObjectType;
import com.github.nikitakuchur.puzzlegame.editor.utils.Option;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class LevelEditor extends Group implements Disposable {

    private Level level;
    private GameObjectsManager manager;

    private final List<Consumer<Level>> levelChangeListeners = new ArrayList<>();
    private final List<Consumer<GameObject>> gameObjectSelectListeners = new ArrayList<>();
    private final List<Runnable> levelPlayListeners = new ArrayList<>();
    private final List<Runnable> levelStopListeners = new ArrayList<>();

    private GameObjectType gameObjectType = GameObjectType.BALL;
    private GameObject selectedGameObject;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final Random rand = new Random();

    public LevelEditor() {
        super();
        setLevel(new Level());
    }

    public void setLayer(Option option) {
        clearListeners();
        selectedGameObject = null;
        switch (option) {
            case BACKGROUND:
                break;
            case MAP:
                addListener(new MapEditorInputListener());
                break;
            case GAME_OBJECTS:
                addListener(new GameObjectsEditorInputListener());
                break;
        }
    }

    public void setGameObjectType(GameObjectType gameObjectType) {
        this.gameObjectType = gameObjectType;
        setSelectedGameObject(null);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        manager = level.getGameObjectsManager();
        clearChildren();
        addActor(level);
        level.act(0);
        level.setPause(true);
        setSelectedGameObject(null);
        levelChangeListeners.forEach(listener -> listener.accept(level));
    }

    public GameObject getSelectedGameObject() {
        return selectedGameObject;
    }

    public void setSelectedGameObject(GameObject gameObject) {
        selectedGameObject = gameObject;
        gameObjectSelectListeners.forEach(listener -> listener.accept(gameObject));
    }

    public void addLevelChangeListener(Consumer<Level> consumer) {
        levelChangeListeners.add(consumer);
    }

    public void clearLevelChangeListeners() {
        levelChangeListeners.clear();
    }

    public void addGameObjectSelectListener(Consumer<GameObject> consumer) {
        gameObjectSelectListeners.add(consumer);
    }

    public void clearGameObjectSelectListeners() {
        gameObjectSelectListeners.clear();
    }

    public void addLevelPlayListener(Runnable runnable) {
        levelPlayListeners.add(runnable);
    }

    public void clearLevelPlayListeners() {
        levelPlayListeners.clear();
    }

    public void addLevelStopListener(Runnable runnable) {
        levelStopListeners.add(runnable);
    }

    public void clearLevelStopListeners() {
        levelStopListeners.clear();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (selectedGameObject != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.translate(-level.getWidth() / 2, -level.getHeight() / 2, 0);
            Color color = level.getBackground().getColor();
            shapeRenderer.setColor(new Color(1 - color.r, 1 - color.g, 1 - color.b, 1));
            Gdx.gl20.glLineWidth(2);
            shapeRenderer.rect(selectedGameObject.getX() * selectedGameObject.getWidth(),
                               selectedGameObject.getY() * selectedGameObject.getHeight(),
                                  selectedGameObject.getWidth(), selectedGameObject.getHeight());
            shapeRenderer.end();
        }
    }

    public void play() {
        level.setPause(false);
        clearListeners();
        setSelectedGameObject(null);
        levelPlayListeners.forEach(Runnable::run);
    }

    public void stop() {
        level.setPause(true);
        levelStopListeners.forEach(Runnable::run);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return level.hit(x, y, touchable);
    }

    @Override
    public void dispose() {
        level.dispose();
        shapeRenderer.dispose();
    }

    private Vector2 screenToMapCoordinates(float x, float y) {
        GameMap map = level.getMap();
        float cellWidth = map.getWidth() / map.getCellsWidth();
        return new Vector2((int) x / cellWidth + (float) map.getCellsWidth() / 2,
                y / cellWidth + (float) map.getCellsHeight() / 2);
    }

    private class MapEditorInputListener extends InputListener {

        private boolean emptyCell;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            GameMap map = level.getMap();
            Vector2 position = screenToMapCoordinates(x, y);
            if (map.getCellType((int) position.x, (int) position.y) == CellType.EMPTY) {
                map.setCellType((int) position.x, (int) position.y, CellType.BLOCK);
                emptyCell = false;
            } else {
                map.setCellType((int) position.x, (int) position.y, CellType.EMPTY);
                emptyCell = true;
            }
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            GameMap map = level.getMap();
            Vector2 position = screenToMapCoordinates(x, y);
            if (!emptyCell && map.getCellType((int) position.x, (int) position.y) == CellType.EMPTY) {
                map.setCellType((int) position.x, (int) position.y, CellType.BLOCK);
            } else if (emptyCell) {
                map.setCellType((int) position.x, (int) position.y, CellType.EMPTY);
            }
        }
    }

    private class GameObjectsEditorInputListener extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Vector2 position = screenToMapCoordinates(x, y);

            GameObject currentGameObject = manager.getGameObjects().stream().
                    filter(object -> (int) object.getX() == (int) position.x &&
                                     (int) object.getY() == (int) position.y)
                    .findAny()
                    .orElse(null);

            if (currentGameObject != null) {
                setSelectedGameObject(currentGameObject);
                return true;
            }

            if (selectedGameObject != null) {
                setSelectedGameObject(null);
                return true;
            }

            GameObject gameObject = gameObjectType.getGameObject();
            if (gameObject == null) return true;
            gameObject.setX((int) position.x);
            gameObject.setY((int) position.y);
            gameObject.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1));
            gameObject.act(level, 0);
            manager.add(gameObject);
            setSelectedGameObject(gameObject);
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            Vector2 position = screenToMapCoordinates(x, y);
            if (selectedGameObject != null) {
                selectedGameObject.setX((int) position.x);
                selectedGameObject.setY((int) position.y);
                selectedGameObject.initialize();
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            setSelectedGameObject(selectedGameObject);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (selectedGameObject != null && keycode == Input.Keys.FORWARD_DEL) {
                manager.remove(selectedGameObject);
                selectedGameObject.dispose();
                setSelectedGameObject(null);
            }
            return true;
        }
    }
}
