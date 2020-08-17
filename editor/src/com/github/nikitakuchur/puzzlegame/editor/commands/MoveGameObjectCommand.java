package com.github.nikitakuchur.puzzlegame.editor.commands;

import com.github.nikitakuchur.puzzlegame.game.entities.Level;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;

public class MoveGameObjectCommand implements Command {

    private final GameObject gameObject;
    private final Level level;

    private final int oldX;
    private final int oldY;

    private int newX;
    private int newY;

    public MoveGameObjectCommand(GameObject gameObject, Level level) {
        this.gameObject = gameObject;
        this.level = level;
        oldX = (int) gameObject.getX();
        oldY = (int) gameObject.getY();
        newX = oldX;
        newY = oldY;
    }

    public void setTarget(int x, int y) {
        newX = x;
        newY = y;
    }

    @Override
    public void execute() {
        gameObject.setPosition(newX, newY);
        gameObject.initialize(level);
    }

    @Override
    public void unexecute() {
        gameObject.setPosition(oldX, oldY);
        gameObject.initialize(level);
    }
}
