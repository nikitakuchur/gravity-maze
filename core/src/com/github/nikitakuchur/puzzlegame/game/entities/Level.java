package com.github.nikitakuchur.puzzlegame.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.github.nikitakuchur.puzzlegame.game.GravityDirection;
import com.github.nikitakuchur.puzzlegame.game.LevelInputHandler;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectsManager;
import com.github.nikitakuchur.puzzlegame.utils.Properties;

import java.util.List;

public class Level extends Group implements Entity {

    private final LevelInputHandler inputController = new LevelInputHandler(this);

    private Background background;
    private GameMap map;

    private final GameObjectsManager manager = new GameObjectsManager();

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

        manager.addGameObjectAddListener(gameObject -> {
            if (gameObject.getClass() == Ball.class) {
                ballsGroup.addActor(gameObject);
            } else {
                gameObjectsGroup.addActor(gameObject);
            }
        });
        manager.addGameObjectRemoveListener(gameObject -> {
            if (gameObject.getClass() == Ball.class) {
                ballsGroup.removeActor(gameObject);
            } else {
                gameObjectsGroup.removeActor(gameObject);
            }
        });

        addListener(inputController.getInputListener());
    }

    @Override
    public void act(float delta) {
        update();
        manager.getGameObjects().forEach(gameObject -> gameObject.update(this));
        super.act(delta);
        if (!pause) {
            manager.getGameObjects().forEach(gameObject -> gameObject.act(this, delta));
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

    public GameObjectsManager getGameObjectsManager() {
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

    @Override
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("background", Background.class, background);
        properties.put("map", GameMap.class, map);
        List<GameObject> gameObjects = manager.getGameObjects();
        properties.put("gameObjects", gameObjects.getClass(), gameObjects);
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
        gameObjects.forEach(gameObject -> manager.add((GameObject) gameObject));
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
        manager.getGameObjects(GameObject.class).forEach(GameObject::dispose);
    }
}
