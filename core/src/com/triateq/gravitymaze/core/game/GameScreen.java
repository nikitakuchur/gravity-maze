package com.triateq.gravitymaze.core.game;

import com.badlogic.gdx.ScreenAdapter;

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
