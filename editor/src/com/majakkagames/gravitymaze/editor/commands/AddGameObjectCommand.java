package com.majakkagames.gravitymaze.editor.commands;

import com.badlogic.gdx.utils.Disposable;
import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;

public class AddGameObjectCommand implements Command {

    private final GameObject gameObject;
    private final GameObjectStore store;
    private boolean removed = true;

    public AddGameObjectCommand(GameObject gameObject, GameObjectStore store) {
        this.gameObject = gameObject;
        this.store = store;
    }

    @Override
    public void execute() {
        store.add(gameObject);
        store.initialize();
        removed = false;
    }

    @Override
    public void unexecute() {
        store.remove(gameObject);
        removed = true;
    }

    @Override
    public void dispose() {
        if (removed && gameObject instanceof Disposable) {
            ((Disposable) gameObject).dispose();
        }
    }
}
