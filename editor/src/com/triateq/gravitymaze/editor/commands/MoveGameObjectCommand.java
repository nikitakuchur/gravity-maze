package com.triateq.gravitymaze.editor.commands;

import com.triateq.gravitymaze.core.game.GameObject;

public class MoveGameObjectCommand implements Command {

    private final GameObject gameObject;

    private final int oldX;
    private final int oldY;

    private int newX;
    private int newY;

    public MoveGameObjectCommand(GameObject gameObject) {
        this.gameObject = gameObject;
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
    }

    @Override
    public void unexecute() {
        gameObject.setPosition(oldX, oldY);
    }
}
