package com.github.nikitakuchur.puzzlegame.ui.menus;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.utils.Context;

/**
 * This is an abstract menu class.
 */
public abstract class Menu extends Group {

    private final Context context;
    private final MenuStack menuStack;

    protected Menu(Context context, MenuStack menuStack) {
        this.context = context;
        this.menuStack = menuStack;
    }

    public Context getContext() {
        return context;
    }

    public MenuStack getMenuStack() {
        return menuStack;
    }
}
