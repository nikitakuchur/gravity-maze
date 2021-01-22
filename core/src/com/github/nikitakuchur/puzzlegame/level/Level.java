package com.github.nikitakuchur.puzzlegame.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.actors.Background;
import com.github.nikitakuchur.puzzlegame.actors.GameMap;
import com.github.nikitakuchur.puzzlegame.serialization.Parameterizable;
import com.github.nikitakuchur.puzzlegame.utils.Direction;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.GameObjectStore;
import com.github.nikitakuchur.puzzlegame.physics.Physics;
import com.github.nikitakuchur.puzzlegame.serialization.Parameters;

import java.util.EnumMap;
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
     * Updates width and height
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

    public void endGame() {
        clearListeners();
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

    public void setMaxMoves(int maxMoves) {
        this.maxMoves = maxMoves;
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
        Stream.of(gameObjects).forEach(store::add);
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
        store.getGameObjects(Disposable.class).forEach(Disposable::dispose);
    }
}
