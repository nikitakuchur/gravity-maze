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
import com.github.nikitakuchur.puzzlegame.editor.panels.GameObjectType;
import com.github.nikitakuchur.puzzlegame.editor.utils.Layer;
import com.github.nikitakuchur.puzzlegame.game.GameMap;
import com.github.nikitakuchur.puzzlegame.game.Level;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class LevelEditor extends Group implements Disposable {

    private Level level;

    private List<Runnable> levelChangeListeners = new ArrayList<>();
    private List<Runnable> selectGameObjectListeners = new ArrayList<>();

    private GameObjectType gameObjectType = GameObjectType.BALL;
    private GameObject selectedGameObject;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public LevelEditor() {
        super();
        level = new Level();
        level.clearListeners();
        level.setPause(true);
        addActor(level);
    }

    public void setLayer(Layer layer) {
        clearListeners();
        selectedGameObject = null;
        switch (layer) {
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
        selectedGameObject = null;
        selectGameObjectListeners.forEach(Runnable::run);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        clearChildren();
        level.setPause(true);
        selectedGameObject = null;
        addActor(level);
        levelChangeListeners.forEach(Runnable::run);
    }

    public GameObject getSelectedGameObject() {
        return selectedGameObject;
    }

    public void addLevelChangeListener(Runnable runnable) {
        levelChangeListeners.add(runnable);
    }

    public void addSelectGameObjectListener(Runnable runnable) {
        selectGameObjectListeners.add(runnable);
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

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return this;
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
            if (selectedGameObject != null) {
                selectedGameObject = null;
                return true;
            }

            Vector2 position = screenToMapCoordinates(x, y);

            selectedGameObject = level.getGameObjects().stream().
                    filter(object -> (int) object.getX() == (int) position.x &&
                                     (int) object.getY() == (int) position.y)
                    .findAny()
                    .orElse(null);

            if (selectedGameObject != null) {
                selectGameObjectListeners.forEach(Runnable::run);
                return true;
            }

            GameObject gameObject = gameObjectType.getGameObject();
            if (gameObject == null) return true;
            gameObject.setX((int) position.x);
            gameObject.setY((int) position.y);
            gameObject.setColor(Color.BLUE);
            level.addActor(gameObject);
            selectedGameObject = gameObject;
            selectGameObjectListeners.forEach(Runnable::run);
            return true;
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (selectedGameObject != null && keycode == Input.Keys.FORWARD_DEL) {
                level.removeActor(selectedGameObject);
                selectedGameObject.dispose();
                selectedGameObject = null;
                selectGameObjectListeners.forEach(Runnable::run);
            }
            return true;
        }
    }
}
