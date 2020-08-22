package com.github.nikitakuchur.puzzlegame.editor.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory {

    private static final CommandHistory INSTANCE = new CommandHistory();

    private final List<Command> history = new ArrayList<>();

    private int currentCommand = -1;

    private final List<Runnable> historyChangeListeners = new ArrayList<>();

    private CommandHistory() {
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
        historyChangeListeners.forEach(Runnable::run);
    }

    public boolean canUndo() {
        return currentCommand >= 0;
    }

    public void undo() {
        if (!canUndo()) throw new IllegalStateException("There are no commands in the history to undo.");
        Command command = history.get(currentCommand);
        command.unexecute();
        currentCommand--;
        historyChangeListeners.forEach(Runnable::run);
    }

    public boolean canRedo() {
        return currentCommand + 1 < history.size();
    }

    public void redo() {
        if (!canRedo()) throw new IllegalStateException("There are no commands in the history to redo.");
        currentCommand++;
        Command command = history.get(currentCommand);
        command.execute();
        historyChangeListeners.forEach(Runnable::run);
    }

    public void clear() {
        history.forEach(Command::dispose);
        history.clear();
        currentCommand = -1;
    }

    public static CommandHistory getInstance() {
        return INSTANCE;
    }

    public void addHistoryChangeListener(Runnable runnable) {
        historyChangeListeners.add(runnable);
    }
}
