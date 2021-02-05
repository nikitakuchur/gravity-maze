package com.triateq.gravitymaze.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.triateq.gravitymaze.actors.Background;
import com.triateq.gravitymaze.actors.GameMap;
import com.triateq.gravitymaze.actors.gameobjects.Ball;
import com.triateq.gravitymaze.serialization.Parameterizable;
import com.triateq.gravitymaze.utils.Direction;
import com.triateq.gravitymaze.actors.gameobjects.GameObject;
import com.triateq.gravitymaze.actors.gameobjects.GameObjectStore;
import com.triateq.gravitymaze.physics.Physics;
import com.triateq.gravitymaze.serialization.Parameters;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

public class Level extends Group implements Parameterizable, Disposable {

    private final LevelInputHandler inputController = new LevelInputHandler(this);

    private Background background;
    private GameMap map;

    private final GameObjectStore store = new GameObjectStore(this);
    private final EnumMap<Layer, Group> groups = new EnumMap<>(Layer.class);

    private final Physics physics = new Physics(this);

    private Direction gravityDirection = Direction.BOTTOM;

    private int maxMoves;
    private int moves;

    private boolean pause;
    private boolean gameEnded;
    private boolean failed;

    private final List<IntConsumer> gameEndListeners = new ArrayList<>();

    public Level() {
        this(new Background(), new GameMap());
    }

    public Level(Background background, GameMap map) {
        this.background = background;
        addActor(background);

        Stream.of(Layer.values()).forEach(layer -> {
            Group group = new Group();
            groups.put(layer, group);
            addActor(group);
        });

        store.addGameObjectAddListener(gameObject -> {
            Layer layer = gameObject.getLayer();
            groups.get(layer).addActor(gameObject);
        });

        groups.values().forEach(this::addActor);

        this.map = map;
        addActor(map);

        store.addGameObjectRemoveListener(gameObject -> {
            Layer layer = gameObject.getLayer();
            groups.get(layer).removeActor(gameObject);
        });
        addListener(inputController.getInputListener());
    }

    @Override
    public void act(float delta) {
        update();
        store.getGameObjects().forEach(GameObject::update);
        if (!pause) {
            super.act(delta);
            physics.update(delta);
            inputController.act(delta);
        }
        if (!gameEnded && !failed && store.getGameObjects(Ball.class).isEmpty()) {
            endGame(false);
            gameEnded = true;
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return this;
    }

    public Background getBackground() {
        return background;
    }

    public GameMap getMap() {
        return map;
    }

    /**
     * Updates width and height.
     */
    public void update() {
        float w = Gdx.graphics.getWidth();
        if (w > Gdx.graphics.getHeight()) {
            w = Gdx.graphics.getHeight();
        }
        setWidth(w);
        setHeight(w / (float) map.getCellsWidth() * map.getCellsHeight());
        map.setWidth(getWidth());
        map.setHeight(getHeight());
        groups.values().forEach(group -> {
            group.setWidth(getWidth());
            group.setHeight(getHeight());
        });
    }

    public void endGame(boolean failed) {
        clearListeners();
        this.failed = failed;
        int stars = 1;
        if (failed) {
            stars = 0;
        } else if (maxMoves >= moves) {
            stars = 3;
        } else if (maxMoves * 1.3f >= moves) {
            stars = 2;
        }
        int finalStars = stars;
        gameEndListeners.forEach(c -> c.accept(finalStars));
    }

    public GameObjectStore getGameObjectStore() {
        return store;
    }

    public Direction getGravityDirection() {
        return gravityDirection;
    }

    public void setGravityDirection(Direction gravityDirection) {
        this.gravityDirection = gravityDirection;
    }

    public int getMaxMoves() {
        return maxMoves;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
        if (pause) {
            clearListeners();
        } else {
            addListener(inputController.getInputListener());
        }
    }

    public boolean onPause() {
        return pause;
    }

    @Override
    public Parameters getParameters() {
        Parameters parameters = new Parameters();
        parameters.put("maxMoves", Integer.class, maxMoves);
        parameters.put("background", Background.class, background);
        parameters.put("map", GameMap.class, map);
        GameObject[] gameObjects = store.getGameObjects().toArray(new GameObject[0]);
        parameters.put("gameObjects", gameObjects.getClass(), gameObjects);
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        maxMoves = parameters.getValueOrDefault("maxMoves", 0);
        background = parameters.getValue("background");
        map = parameters.getValue("map");
        GameObject[] gameObjects = parameters.getValue("gameObjects");

        clearChildren();
        addActor(background);
        groups.values().forEach(this::addActor);
        addActor(map);

        store.clear();
        Stream.of(gameObjects).forEach(store::add);
    }

    public void addGameEndListener(IntConsumer consumer) {
        gameEndListeners.add(consumer);
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
        store.getGameObjects(Disposable.class).forEach(Disposable::dispose);
    }
}
