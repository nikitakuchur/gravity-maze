package com.triateq.puzzlecore.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.triateq.puzzlecore.game.Context;

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

    public void back() {
        if (menuStack.size() > 1) {
            menuStack.pop();
        }
    }

    @Override
    public void dispose() {
    }
}
