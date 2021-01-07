package com.github.nikitakuchur.puzzlegame.ui.commands;

import com.badlogic.gdx.ScreenAdapter;

import java.util.function.Consumer;

public class SetScreenCommand implements Command {
    private Consumer<ScreenAdapter> method;
    private ScreenAdapter screen;

    public SetScreenCommand(Consumer<ScreenAdapter> method) {
        this.method = method;
    }

    public void setScreen(ScreenAdapter screen) {
        this.screen = screen;
    }

    @Override
    public void execute() {
        method.accept(screen);
    }
}
