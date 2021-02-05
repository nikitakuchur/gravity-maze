package com.triateq.gravitymaze.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.triateq.gravitymaze.utils.Context;

/**
 * This is an abstract menu class.
 */
public abstract class Menu extends Group implements Disposable {

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

    @Override
    public void dispose() {
    }
}
