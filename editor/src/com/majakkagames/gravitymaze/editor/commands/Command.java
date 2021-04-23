package com.majakkagames.gravitymaze.editor.commands;

/**
 * This is the command interface.
 */
public interface Command {

    /**
     * Executes the command.
     */
    void execute();

    /**
     * Unexecutes the command.
     */
    void unexecute();

    /**
     * Disposes the resources.
     * This method is invoked when we remove the command from the command history.
     */
    default void dispose() {
    }
}
