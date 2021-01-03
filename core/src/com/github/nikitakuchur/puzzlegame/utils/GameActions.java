package com.github.nikitakuchur.puzzlegame.utils;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class GameActions {

    private GameActions() {
        throw new IllegalStateException("Utility class");
    }

    public static Action bounceAndRotate() {
        return Actions.sequence(
                Actions.parallel(
                        Actions.rotateBy(-40, 1, Interpolation.linear),
                        Actions.scaleTo(0.9f, 0.9f, 1, Interpolation.smooth)),
                Actions.parallel(
                        Actions.rotateBy(-40, 1, Interpolation.linear),
                        Actions.scaleTo(1, 1, 1, Interpolation.smooth)
                ));
    }

    public static Action shrink() {
        return Actions.scaleTo(0f, 0f, 0.25f, Interpolation.smooth);
    }
}
