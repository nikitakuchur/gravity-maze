package com.puzzlegame.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject extends Actor implements Disposable {

    private final Level level;

    public GameObject(Level level) {
        this.level = level;
        update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        update();
    }

    private void update() {
        setWidth(level.getMap().getWidth() / level.getMap().getCellsWidth());
        setHeight(level.getMap().getHeight() / level.getMap().getCellsHeight());
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }
}
