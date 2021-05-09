package com.majakkagames.gravitymaze.editorcore;

import com.badlogic.gdx.utils.Disposable;
import com.majakkagames.gravitymaze.core.events.EventHandler;
import com.majakkagames.gravitymaze.core.events.EventHandlerManager;
import com.majakkagames.gravitymaze.core.events.LevelEvent;
import com.majakkagames.gravitymaze.core.game.Level;
import com.majakkagames.gravitymaze.core.serialization.LevelLoader;
import com.majakkagames.gravitymaze.editorcore.config.Configurator;

import java.util.ArrayList;
import java.util.List;

public class LevelManager implements Disposable {

    private final Configurator configurator;

    private Level level;
    private LevelLoader levelLoader;

    private final List<String> snapshots = new ArrayList<>();
    private int currentSnapshot = -1;

    private final EventHandlerManager<EventType, LevelEvent> eventHandlerManager = new EventHandlerManager<>();

    public LevelManager(Configurator configurator, Level level) {
        this.configurator = configurator;
        setLevel(level);
    }

    /**
     * Makes a new snapshot of the level.
     */
    public void makeSnapshot() {
        while (snapshots.size() > currentSnapshot + 1) {
            snapshots.remove(snapshots.size() - 1);
        }
        snapshots.add(levelLoader.toJson(level));
        currentSnapshot++;
        eventHandlerManager.fire(EventType.SNAPSHOT, new LevelEvent(level));
    }

    public void removeLastSnapshot() {
        snapshots.remove(snapshots.size() - 1);
        if (currentSnapshot >= snapshots.size()) {
            undo();
        }
    }

    /**
     * Returns true if you the undo operation can be invoked and false otherwise.
     */
    public boolean canUndo() {
        return currentSnapshot > 0;
    }

    /**
     * Undos the last command.
     */
    public void undo() {
        currentSnapshot--;
        restoreLevel();
    }

    /**
     * Returns true if you the redo operation can be invoked and false otherwise.
     */
    public boolean canRedo() {
        return currentSnapshot + 1 < snapshots.size();
    }

    /**
     * Redos the command.
     */
    public void redo() {
        currentSnapshot++;
        restoreLevel();
    }

    private void restoreLevel() {
        String snapshot = snapshots.get(currentSnapshot);
        level = levelLoader.fromJson(snapshot);
        configurator.getLevelPreparer().accept(level);
        level.initialize();
        eventHandlerManager.fire(EventType.CHANGED, new LevelEvent(level));
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
        this.levelLoader = new LevelLoader(level.getContext());
        snapshots.clear();
        currentSnapshot = -1;
        eventHandlerManager.fire(EventType.CHANGED, new LevelEvent(level));
        makeSnapshot();
    }

    @Override
    public void dispose() {
        level.dispose();
    }

    public void addEventHandler(EventType type, EventHandler<LevelEvent> handler) {
        eventHandlerManager.add(type, handler);
    }

    public enum EventType {
        CHANGED, SNAPSHOT
    }
}
