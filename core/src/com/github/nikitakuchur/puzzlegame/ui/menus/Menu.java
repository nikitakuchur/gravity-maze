package com.github.nikitakuchur.puzzlegame.ui.menus;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;

public abstract class Menu<T extends GameScreen> extends Group {

    private final MenuStack menuStack;
    private final T gameScreen;

    protected Menu(MenuStack menuStack, T gameScreen) {
        this.menuStack = menuStack;
        this.gameScreen = gameScreen;
    }

    public MenuStack getMenuStack() {
        return menuStack;
    }

    public T getGameScreen() {
        return gameScreen;
    }
}
