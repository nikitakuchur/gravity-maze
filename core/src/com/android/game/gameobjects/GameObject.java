package com.android.game.gameobjects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameObject extends Actor implements Disposable {

    /**
     * @param ball the ball
     * @return true if the ball can interact with this object and false otherwise
     */
    public boolean isInteracting(Ball ball) {
        return true;
    }
}
