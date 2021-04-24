package com.majakkagames.gravitymaze.editor;

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
import com.majakkagames.gravitymaze.core.game.Context;
import com.majakkagames.gravitymaze.core.game.*;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.editor.commands.AddBlocksCommand;
import com.majakkagames.gravitymaze.editor.commands.AddGameObjectCommand;
import com.majakkagames.gravitymaze.editor.commands.CommandHistory;
import com.majakkagames.gravitymaze.editor.commands.MoveGameObjectCommand;
import com.majakkagames.gravitymaze.editor.commands.RemoveBlocksCommand;
import com.majakkagames.gravitymaze.editor.commands.RemoveGameObjectCommand;
import com.majakkagames.gravitymaze.editor.utils.GameObjectType;
import com.majakkagames.gravitymaze.editor.utils.Option;
import com.majakkagames.gravitymaze.game.gameobjects.Background;
import com.majakkagames.gravitymaze.game.gameobjects.Maze;
import com.majakkagames.gravitymaze.game.cells.CellType;
import com.majakkagames.gravitymaze.game.utils.LevelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LevelEditor extends Group implements Disposable {

    private Level level;
    private GameObjectStore store;
    private Background background;
    private Maze maze;

    private final List<Consumer<Level>> levelChangeListeners = new ArrayList<>();
    private final List<Consumer<GameObject>> gameObjectSelectListeners = new ArrayList<>();
    private final List<Runnable> levelPlayListeners = new ArrayList<>();
    private final List<Runnable> levelStopListeners = new ArrayList<>();

    private GameObjectType gameObjectType = GameObjectType.BALL;
    private GameObject selectedGameObject;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public LevelEditor(Context context) {
        super();
        Level level = new LevelBuilder()
                .context(context)
                .build();
        setLevel(level);
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
        store = level.getGameObjectStore();
        background = store.getAnyGameObjectOrThrow(Background.class,
                () -> new IllegalStateException("Cannot find the background object"));
        maze = store.getAnyGameObjectOrThrow(Maze.class,
                () -> new IllegalStateException("Cannot find the game map object"));
        clearChildren();
        addActor(level);
        level.setPause(true);
        levelChangeListeners.forEach(listener -> listener.accept(level));
    }

    public Background getBackground() {
        return background;
    }

    public Maze getMaze() {
        return maze;
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
        if (!store.contains(selectedGameObject)) {
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
        updateSize();
        super.draw(batch, parentAlpha);
        if (level.onPause() && hasSelectedObject()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
            shapeRenderer.translate(-maze.getWidth() / 2, -maze.getHeight() / 2, 0);
            Color color = background.getColor();
            shapeRenderer.setColor(new Color(1 - color.r, 1 - color.g, 1 - color.b, 1));
            Gdx.gl20.glLineWidth(2);
            shapeRenderer.rect(selectedGameObject.getX() * selectedGameObject.getWidth(),
                               selectedGameObject.getY() * selectedGameObject.getHeight(),
                                  selectedGameObject.getWidth(), selectedGameObject.getHeight());
            shapeRenderer.end();
        }
    }

    private void updateSize() {
        level.setWidth(Gdx.graphics.getWidth());
        level.setHeight(Gdx.graphics.getHeight());
    }

    public void play() {
        level.setPause(false);
        clearListeners();
        level.initialize();
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
        float cellWidth = maze.getWidth() / maze.getCellsWidth();
        return new Vector2((int) x / cellWidth + (float) maze.getCellsWidth() / 2,
                y / cellWidth + (float) maze.getCellsHeight() / 2);
    }

    private class MapEditorInputListener extends InputListener {

        private final CommandHistory commandHistory = CommandHistory.getInstance();
        private AddBlocksCommand addCommand;
        private RemoveBlocksCommand removeCommand;

        private boolean emptyCell;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            if (maze.isOutside(px, py)) return false;

            if (maze.isEmpty(px, py)) {
                maze.setCellType(px, py, CellType.FILLED);
                if (addCommand == null) {
                    addCommand = new AddBlocksCommand(maze);
                }
                addCommand.addBlock(px, py);
                emptyCell = false;
            } else if (maze.isFilled(px, py)) {
                maze.setCellType(px, py, CellType.EMPTY);
                if (removeCommand == null) {
                    removeCommand = new RemoveBlocksCommand(maze);
                }
                removeCommand.addBlock(px, py);
                emptyCell = true;
            }
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            if (maze.isOutside(px, py)) return;

            if (!emptyCell && maze.isEmpty(px, py)) {
                maze.setCellType(px, py, CellType.FILLED);
                addCommand.addBlock(px, py);
            } else if (emptyCell && maze.isFilled(px, py)) {
                maze.setCellType(px, py, CellType.EMPTY);
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
            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            GameObject currentGameObject = store.getGameObjects().stream().
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

            if (maze.isOutside(px, py)) return false;

            GameObject gameObject = gameObjectType.newInstance();
            gameObject.setX(px);
            gameObject.setY(py);
            commandHistory.addAndExecute(new AddGameObjectCommand(gameObject, store));
            setSelectedGameObject(gameObject);
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            Vector2 position = screenToMapCoordinates(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            if (maze.isOutside(px, py)) return;

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
                commandHistory.addAndExecute(new RemoveGameObjectCommand(selectedGameObject, store));
                resetSelection();
            }
            return true;
        }
    }
}
