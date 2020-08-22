package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.physics.GravityDirection;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectStore;
import com.github.nikitakuchur.puzzlegame.physics.Physics;
import com.github.nikitakuchur.puzzlegame.utils.Layer;
import com.github.nikitakuchur.puzzlegame.utils.Parameters;

import java.util.EnumMap;
import java.util.stream.Stream;

public class Level extends Group implements Parameterizable, Disposable {

    private final LevelInputHandler inputController = new LevelInputHandler(this);

    private Background background;
    private GameMap map;

    private final GameObjectStore manager = new GameObjectStore(this);
    private final EnumMap<Layer, Group> groups = new EnumMap<>(Layer.class);

    private final Physics physics = new Physics(this);

    private GravityDirection gravityDirection = GravityDirection.BOTTOM;
    private int score;
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

        manager.addGameObjectAddListener(gameObject -> {
            Layer layer = gameObject.getLayer();
            groups.get(layer).addActor(gameObject);
        });

        groups.values().forEach(this::addActor);

        this.map = map;
        addActor(map);

        manager.addGameObjectRemoveListener(gameObject -> {
            Layer layer = gameObject.getLayer();
            groups.get(layer).removeActor(gameObject);
        });
        addListener(inputController.getInputListener());
    }

    @Override
    public void act(float delta) {
        update();
        manager.getGameObjects().forEach(GameObject::update);
        if (!pause) {
            physics.update(delta);
            super.act(delta);
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

    public GameObjectStore getGameObjectManager() {
        return manager;
    }

    public GravityDirection getGravityDirection() {
        return gravityDirection;
    }

    public void setGravityDirection(GravityDirection gravityDirection) {
        this.gravityDirection = gravityDirection;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
        parameters.put("background", Background.class, background);
        parameters.put("map", GameMap.class, map);
        GameObject[] gameObjects = manager.getGameObjects().toArray(new GameObject[0]);
        parameters.put("gameObjects", gameObjects.getClass(), gameObjects);
        return parameters;
    }

    @Override
    public void setParameters(Parameters parameters) {
        background = parameters.getValue("background");
        map = parameters.getValue("map");
        GameObject[] gameObjects = parameters.getValue("gameObjects");

        clearChildren();
        addActor(background);
        groups.values().forEach(this::addActor);
        addActor(map);
        Stream.of(gameObjects).forEach(manager::add);
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
        manager.getGameObjects(Disposable.class).forEach(Disposable::dispose);
    }
}
