package com.github.nikitakuchur.puzzlegame.editor.commands;

import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.GameObjectStore;

public class AddGameObjectCommand implements Command {

    private final GameObject gameObject;
    private final GameObjectStore gameObjectStore;
    private boolean removed = true;

    public AddGameObjectCommand(GameObject gameObject, GameObjectStore gameObjectStore) {
        this.gameObject = gameObject;
        this.gameObjectStore = gameObjectStore;
    }

    @Override
    public void execute() {
        gameObjectStore.add(gameObject);
        removed = false;
    }

    @Override
    public void unexecute() {
        gameObjectStore.remove(gameObject);
        removed = true;
    }

    @Override
    public void dispose() {
        if (removed && gameObject instanceof Disposable) {
            ((Disposable) gameObject).dispose();
        }
    }
}
