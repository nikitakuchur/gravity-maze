package com.github.nikitakuchur.puzzlegame.ui.commands;

public interface UnexecutableCommand extends Command {
    /**
     * Undo last operation
     */
    void unexecute();
}
