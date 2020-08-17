package com.github.nikitakuchur.puzzlegame.editor.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory {

    private static final CommandHistory INSTANCE = new CommandHistory();

    private final List<Command> history = new ArrayList<>();

    private int currentCommand = -1;

    private CommandHistory() {
    }

    public void addAndExecute(Command command) {
        if (history.size() > currentCommand + 1) {
            history.subList(currentCommand + 1, history.size()).clear();
        }
        history.add(command);
        command.execute();
        currentCommand++;
    }

    public boolean canUndo() {
        return currentCommand >= 0;
    }

    public void undo() {
        if (!canUndo()) throw new IllegalStateException("There are no commands in the history to undo.");
        Command command = history.get(currentCommand);
        command.unexecute();
        currentCommand--;
    }

    public boolean canRedo() {
        return currentCommand + 1 < history.size();
    }

    public void redo() {
        if (!canRedo()) throw new IllegalStateException("There are no commands in the history to redo.");
        currentCommand++;
        Command command = history.get(currentCommand);
        command.execute();
    }

    public void clear() {
        history.clear();
        currentCommand = -1;
    }

    public static CommandHistory getInstance() {
        return INSTANCE;
    }
}
