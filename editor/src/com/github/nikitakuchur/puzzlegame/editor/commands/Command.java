package com.github.nikitakuchur.puzzlegame.editor.commands;

public interface Command {
    void execute();
    void unexecute();

    default void dispose() {

    }
}
