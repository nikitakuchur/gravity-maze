package com.triateq.gravitymaze.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.triateq.gravitymaze.utils.Context;

public abstract class GameScreen extends ScreenAdapter {

    private final Context context;

    protected GameScreen(Context context) {
        this.context = Context.from(context)
                .gameScreen(this)
                .build();
    }

    /**
     * Returns the context.
     */
    public Context getContext() {
        return context;
    }
}