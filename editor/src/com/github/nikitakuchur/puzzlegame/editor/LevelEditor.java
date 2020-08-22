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
import com.github.nikitakuchur.puzzlegame.editor.commands.AddBlocksCommand;
import com.github.nikitakuchur.puzzlegame.editor.commands.AddGameObjectCommand;
import com.github.nikitakuchur.puzzlegame.editor.commands.CommandHistory;
import com.github.nikitakuchur.puzzlegame.editor.commands.MoveGameObjectCommand;
import com.github.nikitakuchur.puzzlegame.editor.commands.RemoveBlocksCommand;
import com.github.nikitakuchur.puzzlegame.editor.commands.RemoveGameObjectCommand;
import com.github.nikitakuchur.puzzlegame.editor.utils.GameObjectType;
import com.github.nikitakuchur.puzzlegame.editor.utils.Option;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;
import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LevelEditor extends Group implements Disposable {

    private Level level;
    private GameObjectManager gameObjectManager;

    private final List<Consumer<Level>> levelChangeListeners = new ArrayList<>();
    private final List<Consumer<GameObject>> gameObjectSelectListeners = new ArrayList<>();
    private final List<Runnable> levelPlayListeners = new ArrayList<>();
    private final List<Runnable> levelStopListeners = new ArrayList<>();

    private GameObjectType gameObjectType = GameObjectType.BALL;
    private GameObject selectedGameObject;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public LevelEditor() {
        super();
        setLevel(new Level());
    }

    public void setLayer(Option option) {
        clearListeners();
        resetSelection();
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
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        gameObjectManager = level.getGameObjectManager();
        clearChildren();
        addActor(level);
        level.setPause(true);
        levelChangeListeners.forEach(listener -> listener.accept(level));
    }

    private void setSelectedGameObject(GameObject gameObject) {
        selectedGameObject = gameObject;
        gameObjectSelectListeners.forEach(listener -> listener.accept(gameObject));
    }

    private void resetSelection() {
        setSelectedGameObject(null);
    }

    private boolean hasSelectedObject() {
        if (selectedGameObject == null) return false;
        if (gameObjectManager.find(selectedGameObject) == null) {
            selectedGameObject = null;
            return false;
        }
        return true;
    }

    public void addLevelChangeListener(Consumer<Level> consumer) {
        levelChangeListeners.add(consumer);
    }

    public void addGameObjectSelectListener(Consumer<GameObject> consumer) {
        gameObjectSelectListeners.add(consumer);
    }

    public void addLevelPlayListener(Runnable runnable) {
        levelPlayListeners.add(runnable);
    }

    public void addLevelStopListener(Runnable runnable) {
        levelStopListeners.add(runnable);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (level.onPause() && hasSelectedObject()) {
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
        gameObjectManager.getGameObjects().forEach(gameObject -> gameObject.initialize(level));
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

        private final CommandHistory commandHistory = CommandHistory.getInstance();
        private AddBlocksCommand addCommand;
        private RemoveBlocksCommand removeCommand;

        private boolean emptyCell;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            GameMap map = level.getMap();
            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;
            if (map.isOutside(px, py)) return true;
            if (map.isEmpty(px, py)) {
                map.setCellType(px, py, CellType.FILLED);
                if (addCommand == null) {
                    addCommand = new AddBlocksCommand(map);
                }
                addCommand.addBlock(px, py);
                emptyCell = false;
            } else if (map.isFilled(px, py)) {
                map.setCellType(px, py, CellType.EMPTY);
                if (removeCommand == null) {
                    removeCommand = new RemoveBlocksCommand(map);
                }
                removeCommand.addBlock(px, py);
                emptyCell = true;
            }
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            GameMap map = level.getMap();

            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            if (map.isOutside(px, py)) return;

            if (!emptyCell && map.isEmpty(px, py)) {
                map.setCellType(px, py, CellType.FILLED);
                addCommand.addBlock(px, py);
            } else if (emptyCell && map.isFilled(px, py)) {
                map.setCellType(px, py, CellType.EMPTY);
                removeCommand.addBlock(px, py);
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            commandHistory.add(addCommand);
            commandHistory.add(removeCommand);
            addCommand = null;
            removeCommand = null;
        }
    }

    private class GameObjectsEditorInputListener extends InputListener {

        private final CommandHistory commandHistory = CommandHistory.getInstance();
        private MoveGameObjectCommand moveCommand;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            GameMap map = level.getMap();

            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            GameObject currentGameObject = gameObjectManager.getGameObjects().stream().
                    filter(object -> (int) object.getX() == px
                            && (int) object.getY() == py)
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

            if (map.isOutside(px, py)) return true;

            GameObject gameObject = gameObjectType.getGameObject();
            gameObject.setX(px);
            gameObject.setY(py);
            commandHistory.addAndExecute(new AddGameObjectCommand(gameObject, gameObjectManager));
            setSelectedGameObject(gameObject);
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            GameMap map = level.getMap();

            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            if (map.isOutside(px, py)) return;

            if (selectedGameObject != null) {
                if (moveCommand == null) {
                    moveCommand = new MoveGameObjectCommand(selectedGameObject);
                }
                selectedGameObject.setPosition(px, py);
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Vector2 position = screenToMapCoordinates(x, y);
            if (moveCommand != null) {
                moveCommand.setTarget((int) position.x, (int) position.y);
                commandHistory.add(moveCommand);
                moveCommand = null;
            }
            setSelectedGameObject(selectedGameObject);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (selectedGameObject != null && keycode == Input.Keys.FORWARD_DEL) {
                commandHistory.addAndExecute(new RemoveGameObjectCommand(selectedGameObject, gameObjectManager));
                resetSelection();
            }
            return true;
        }
    }
}
