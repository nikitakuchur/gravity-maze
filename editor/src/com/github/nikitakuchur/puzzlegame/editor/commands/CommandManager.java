package com.github.nikitakuchur.puzzlegame.editor.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private static final CommandManager INSTANCE = new CommandManager();

    private final List<Command> history = new ArrayList<>();

    private int currentCommand = -1;

    private final List<Runnable> undoListeners = new ArrayList<>();
    private final List<Runnable> redoListeners = new ArrayList<>();

    private CommandManager() {
    }

    public void addAndExecute(Command command) {
        if (command == null) return;
        add(command);
        command.execute();
    }

    public void add(Command command) {
        if (command == null) return;
        if (history.size() > currentCommand + 1) {
            List<Command> subList = history.subList(currentCommand + 1, history.size());
            subList.forEach(Command::dispose);
            subList.clear();
        }
        history.add(command);
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
        undoListeners.forEach(Runnable::run);
    }

    public boolean canRedo() {
        return currentCommand + 1 < history.size();
    }

    public void redo() {
        if (!canRedo()) throw new IllegalStateException("There are no commands in the history to redo.");
        currentCommand++;
        Command command = history.get(currentCommand);
        command.execute();
        redoListeners.forEach(Runnable::run);
    }

    public void clear() {
        history.forEach(Command::dispose);
        history.clear();
        currentCommand = -1;
    }

    public static CommandManager getInstance() {
        return INSTANCE;
    }

    public void addUndoListener(Runnable runnable) {
        undoListeners.add(runnable);
    }

    public void addRedoListener(Runnable runnable) {
        redoListeners.add(runnable);
    }
}
