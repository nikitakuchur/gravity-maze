package com.github.nikitakuchur.puzzlegame.editor.commands;

import com.github.nikitakuchur.puzzlegame.editor.LevelEditor;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObjectsManager;

public class AddGameObjectCommand implements Command {

    private final GameObject gameObject;
    private final LevelEditor editor;
    private final GameObjectsManager manager;

    public AddGameObjectCommand(GameObject gameObject, LevelEditor editor) {
        this.gameObject = gameObject;
        this.editor = editor;
        this.manager = editor.getLevel().getGameObjectsManager();
    }

    @Override
    public void execute() {
        gameObject.initialize(editor.getLevel());
        gameObject.act(0);
        manager.add(gameObject);
        editor.setSelectedGameObject(gameObject);
    }

    @Override
    public void unexecute() {
        manager.remove(gameObject);
        editor.setSelectedGameObject(null);
    }
}
