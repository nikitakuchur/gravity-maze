package com.github.nikitakuchur.puzzlegame.editor.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * The command history class.
 */
public class CommandHistory {

    private static final CommandHistory INSTANCE = new CommandHistory();

    private final List<Command> history = new ArrayList<>();

    private int currentCommand = -1;

    private final List<Runnable> historyChangeListeners = new ArrayList<>();

    private CommandHistory() {
        // This is a singleton class
    }

    /**
     * Adds the given command to the command history and executes it.
     *
     * @param command the command
     */
    public void addAndExecute(Command command) {
        if (command == null) return;
        add(command);
        command.execute();
    }

    /**
     * Adds the given command to the command history.
     *
     * @param command the command
     */
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

    /**
     * Returns true if you the undo operation can be invoked and false otherwise.
     */
    public boolean canUndo() {
        return currentCommand >= 0;
    }

    /**
     * Undos the last command.
     */
    public void undo() {
        if (!canUndo()) throw new IllegalStateException("There are no commands in the history to undo.");
        Command command = history.get(currentCommand);
        command.unexecute();
        currentCommand--;
        historyChangeListeners.forEach(Runnable::run);
    }

    /**
     * Returns true if you the redo operation can be invoked and false otherwise.
     */
    public boolean canRedo() {
        return currentCommand + 1 < history.size();
    }

    /**
     * Redos the command.
     */
    public void redo() {
        if (!canRedo()) throw new IllegalStateException("There are no commands in the history to redo.");
        currentCommand++;
        Command command = history.get(currentCommand);
        command.execute();
        historyChangeListeners.forEach(Runnable::run);
    }

    /**
     * Clears the command history.
     */
    public void clear() {
        history.forEach(Command::dispose);
        history.clear();
        currentCommand = -1;
    }

    /**
     * Returns command history instance.
     */
    public static CommandHistory getInstance() {
        return INSTANCE;
    }

    /**
     * Adds the listener to execute when the command history changes.
     *
     * @param runnable the listener
     */
    public void addHistoryChangeListener(Runnable runnable) {
        historyChangeListeners.add(runnable);
    }
}
