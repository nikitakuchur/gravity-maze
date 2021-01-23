package com.github.nikitakuchur.puzzlegame.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.utils.Context;

/**
 * This is an abstract menu class.
 */
public abstract class Menu extends Group implements Disposable {

    private final Context context;
    private final MenuStack menuStack;

    public Menu(Context context, MenuStack menuStack) {
        this.context = context;
        this.menuStack = menuStack;
    }

    public Context getContext() {
        return context;
    }

    public MenuStack getMenuStack() {
        return menuStack;
    }

    @Override
    public void dispose() {
    }
}
