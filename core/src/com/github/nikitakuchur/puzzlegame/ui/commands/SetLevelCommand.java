package com.github.nikitakuchur.puzzlegame.ui.commands;

import com.github.nikitakuchur.puzzlegame.level.Level;

import java.util.function.Consumer;

public class SetLevelCommand implements Command {
    private Consumer<Level> method;
    private Level level = null;

    public SetLevelCommand(Consumer<Level> method) {
        this.method = method;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void execute() {
        method.accept(level);
    }
}
