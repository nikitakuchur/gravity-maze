package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level extends Group implements Disposable {

    private final LevelInputHandler inputController;

    private final GameMap map;

    private GravityDirection gravityDirection = GravityDirection.BOTTOM;
    private int score;
    private boolean pause;

    private Level(Background background, GameMap map, List<GameObject> gameObjects) {
        inputController = new LevelInputHandler(this);

        if (background != null) {
            this.addActor(background);
        }

        this.map = map;
        map.setWidth(100);
        map.setHeight(map.getWidth() / map.getCellsWidth() * map.getCellsHeight());
        this.addActor(map);

        gameObjects.forEach(this::addActor);

        addListener(inputController.getInputListener());
    }

    @Override
    public void act(float delta) {
        map.setWidth(Gdx.graphics.getWidth());
        map.setHeight(Gdx.graphics.getWidth() / (float) map.getCellsWidth() * map.getCellsHeight());
        super.act(delta);
        inputController.act(delta);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return this;
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

    public boolean getPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    @Override
    public void dispose() {
        map.dispose();
        getGameObjects(GameObject.class).forEach(GameObject::dispose);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Background background;
        private GameMap map = new GameMap(new CellType[][] {{CellType.EMPTY}});
        private List<GameObject> gameObjects = new ArrayList<>();

        public Builder background(Background background) {
            this.background = background;
            return this;
        }

        public Builder map(GameMap map) {
            this.map = map;
            return this;
        }

        public Builder addGameObject(GameObject gameObject) {
            gameObjects.add(gameObject);
            return this;
        }

        public Builder addGameObjects(GameObject... gameObjects) {
            addGameObjects(Arrays.asList(gameObjects));
            return this;
        }

        public Builder addGameObjects(List<GameObject> gameObjects) {
            this.gameObjects.addAll(gameObjects);
            return this;
        }

        public Level build() {
            return new Level(background, map, gameObjects);
        }
    }
}
