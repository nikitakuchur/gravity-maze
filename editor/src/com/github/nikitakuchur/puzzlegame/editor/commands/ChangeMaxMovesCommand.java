package com.github.nikitakuchur.puzzlegame.editor.commands;

import com.github.nikitakuchur.puzzlegame.level.Level;

public class ChangeMaxMovesCommand implements Command {

    private final Level level;

    private final int oldMaxMoves;
    private final int newMaxMoves;

    public ChangeMaxMovesCommand(Level level, int maxMoves) {
        this.level = level;
        oldMaxMoves = level.getMaxMoves();
        newMaxMoves = maxMoves;
    }

    @Override
    public void execute() {
        level.setMaxMoves(newMaxMoves);
    }

    @Override
    public void unexecute() {
        level.setMaxMoves(oldMaxMoves);
    }
}
