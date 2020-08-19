package com.github.nikitakuchur.puzzlegame.editor.commands;

import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectManager;

public class AddGameObjectCommand implements Command {

    private final GameObject gameObject;
    private final GameObjectManager gameObjectManager;
    private boolean removed = true;

    public AddGameObjectCommand(GameObject gameObject, GameObjectManager gameObjectManager) {
        this.gameObject = gameObject;
        this.gameObjectManager = gameObjectManager;
    }

    @Override
    public void execute() {
        gameObjectManager.add(gameObject);
        removed = false;
    }

    @Override
    public void unexecute() {
        gameObjectManager.remove(gameObject);
        removed = true;
    }

    @Override
    public void dispose() {
        if (removed) gameObject.dispose();
    }
}
