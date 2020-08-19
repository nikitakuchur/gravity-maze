package com.github.nikitakuchur.puzzlegame.editor.commands;

import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectManager;

public class RemoveGameObjectCommand implements Command {

    private final GameObject gameObject;
    private final GameObjectManager gameObjectManager;
    private boolean removed;

    public RemoveGameObjectCommand(GameObject gameObject, GameObjectManager gameObjectManager) {
        this.gameObject = gameObject;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void execute() {
        gameObjectManager.remove(gameObject);
        removed = true;
    }

    @Override
    public void unexecute() {
        gameObjectManager.add(gameObject);
        removed = false;
    }

    @Override
    public void dispose() {
        if (removed) gameObject.dispose();
    }
}
