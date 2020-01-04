package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.utils.Properties;
import com.github.nikitakuchur.puzzlegame.utils.PropertiesHolder;

import java.util.ArrayList;
import java.util.List;

public class Level extends Group implements PropertiesHolder, Disposable {

    private final LevelInputHandler inputController;

    private Background background;
    private GameMap map;

    private GravityDirection gravityDirection = GravityDirection.BOTTOM;
    private int score;
    private boolean pause;

    public Level() {
        this(new Background(), new GameMap());
    }

    public Level(Background background, GameMap map) {
        inputController = new LevelInputHandler(this);

        this.background = background;
        addActor(background);

        this.map = map;
        map.setWidth(100);
        map.setHeight(map.getWidth() / map.getCellsWidth() * map.getCellsHeight());
        addActor(map);

        addListener(inputController.getInputListener());
    }

    @Override
    public void act(float delta) {
        map.setWidth(Gdx.graphics.getWidth());
        map.setHeight(Gdx.graphics.getWidth() / (float) map.getCellsWidth() * map.getCellsHeight());
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

    public List<GameObject> getGameObjects() {
        return getGameObjects(GameObject.class);
    }

    public <T extends GameObject> List<T> getGameObjects(Class<T> type) {
        List<T> result = new ArrayList<>();
        getChildren().forEach(actor -> {
            T gameObject = actor.firstAscendant(type);
            if (gameObject != null) {
                result.add(gameObject);
            }
        });
        return result;
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

    public boolean isPaused() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
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
        gameObjects.forEach(gameObject -> addActor((GameObject) gameObject));
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
        getGameObjects(GameObject.class).forEach(GameObject::dispose);
    }
}
