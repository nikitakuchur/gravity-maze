package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.github.nikitakuchur.puzzlegame.game.GravityDirection;
import com.github.nikitakuchur.puzzlegame.game.LevelInputHandler;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Level extends Group implements Entity {

    private final LevelInputHandler inputController = new LevelInputHandler(this);

    private Background background;
    private GameMap map;
    private Group gameObjectsGroup = new Group();
    private Group ballsGroup = new Group();

    private GravityDirection gravityDirection = GravityDirection.BOTTOM;
    private int score;
    private boolean pause;

    public Level() {
        this(new Background(), new GameMap());
    }

    public Level(Background background, GameMap map) {
        this.background = background;
        addActor(background);

        this.map = map;
        map.setWidth(100);
        map.setHeight(map.getWidth() / map.getCellsWidth() * map.getCellsHeight());
        addActor(map);
        addActor(gameObjectsGroup);
        addActor(ballsGroup);

        addListener(inputController.getInputListener());
    }

    @Override
    public void act(float delta) {
        update();
        getGameObjects().forEach(gameObject -> gameObject.update(this));
        super.act(delta);
        if (!pause) {
            getGameObjects().forEach(gameObject -> gameObject.act(this, delta));
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
        gameObjectsGroup.setWidth(getWidth());
        gameObjectsGroup.setHeight(getHeight());
        ballsGroup.setWidth(getWidth());
        ballsGroup.setHeight(getHeight());
    }

    public void addGameObject(GameObject gameObject) {
        if (gameObject instanceof Ball) {
            ballsGroup.addActor(gameObject);
        } else {
            gameObjectsGroup.addActor(gameObject);
        }
    }

    public List<GameObject> getGameObjects() {
        Actor[] gameObjects = gameObjectsGroup.getChildren().items;
        Actor[] balls = ballsGroup.getChildren().items;
        return Stream.concat(Arrays.stream(gameObjects), Arrays.stream(balls))
                .filter(Objects::nonNull)
                .map(GameObject.class::cast)
                .collect(Collectors.toList());
    }

    public <T extends GameObject> List<T> getGameObjects(Class<T> type) {
        List<T> result = new ArrayList<>();
        getGameObjects().forEach(actor -> {
            T gameObject = actor.firstAscendant(type);
            if (gameObject != null) {
                result.add(gameObject);
            }
        });
        return result;
    }

    public <T extends GameObject> T findGameObject(String name) {
        T gameObject = gameObjectsGroup.findActor(name);
        if (gameObject != null) return gameObject;
        return ballsGroup.findActor(name);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjectsGroup.removeActor(gameObject);
        ballsGroup.removeActor(gameObject);
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

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("background", Background.class, background);
        properties.put("map", GameMap.class, map);
        properties.put("gameObjects", getGameObjects().getClass(), getGameObjects());
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        background = (Background) properties.getValue("background");
        map = (GameMap) properties.getValue("map");
        List<?> gameObjects = (List<?>) properties.getValue("gameObjects");

        clearChildren();
        addActor(background);
        addActor(map);
        addActor(gameObjectsGroup);
        addActor(ballsGroup);
        gameObjects.forEach(gameObject -> addGameObject((GameObject) gameObject));
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
        getGameObjects(GameObject.class).forEach(GameObject::dispose);
    }
}
