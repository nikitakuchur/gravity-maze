package com.majakkagames.gravitymaze.editorcore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.majakkagames.gravitymaze.core.events.EventHandler;
import com.majakkagames.gravitymaze.core.events.EventHandlerManager;
import com.majakkagames.gravitymaze.core.events.LevelEvent;
import com.majakkagames.gravitymaze.core.game.*;
import com.majakkagames.gravitymaze.editorcore.config.Configurator;
import com.majakkagames.gravitymaze.editorcore.config.GameObjectCreator;

public class LevelEditor extends Group implements Disposable {

    private final LevelManager levelManager;

    private boolean editable = true;
    private boolean playing;

    private final LevelEditorInputListener inputListener;

    private GameObjectCreator gameObjectCreator;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final EventHandlerManager<EventType, LevelEvent> eventHandlerManager = new EventHandlerManager<>();

    public LevelEditor(Configurator configurator, LevelManager levelManager, GameObjectSelector selector) {
        super();
        this.levelManager = levelManager;
        levelManager.addEventHandler(LevelManager.EventType.CHANGED, e -> {
            clearChildren();
            addActor(e.getLevel());
            e.getLevel().setPause(true);
        });
        Level level = levelManager.getLevel();
        addActor(level);
        level.setPause(true);
        inputListener = new LevelEditorInputListener(configurator, this, selector);
        addListener(inputListener);
    }

    public void setGameObjectCreator(GameObjectCreator gameObjectCreator) {
        this.gameObjectCreator = gameObjectCreator;
    }

    public GameObjectCreator getGameObjectCreator() {
        return gameObjectCreator;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Level level = levelManager.getLevel();
        level.setWidth(Gdx.graphics.getWidth());
        level.setHeight(Gdx.graphics.getHeight());
        super.draw(batch, parentAlpha);
    }

    public void setEditable(boolean editable) {
        if (playing || this.editable == editable) return;
        this.editable = editable;
        if (!editable) {
            removeListener(inputListener);
        } else {
            addListener(inputListener);
        }
    }

    public void play() {
        Level level = levelManager.getLevel();
        level.setPause(false);
        level.initialize();
        setEditable(false);
        playing = true;
        eventHandlerManager.fire(EventType.PLAY, new LevelEvent(level));
    }

    public void stop() {
        Level level = levelManager.getLevel();
        level.setPause(true);
        playing = false;
        setEditable(true);
        eventHandlerManager.fire(EventType.STOP, new LevelEvent(level));
    }

    @Override
    public void dispose() {
        levelManager.dispose();
        shapeRenderer.dispose();
    }

    public void addEventHandler(EventType type, EventHandler<LevelEvent> handler) {
        eventHandlerManager.add(type, handler);
    }

    public enum EventType {
        PLAY, STOP
    }
}
