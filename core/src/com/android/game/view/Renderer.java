package com.android.game.view;

import com.badlogic.gdx.math.Matrix4;

public interface Renderer {

    /**
     * @param projectionMatrix the projection matrix
     */
    void draw(Matrix4 projectionMatrix);

    /**
     * Releases all resources of this object.
     */
    void dispose();
}
