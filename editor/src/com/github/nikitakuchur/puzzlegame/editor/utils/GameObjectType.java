package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Hole;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Portal;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Spike;

import java.lang.reflect.InvocationTargetException;

public enum GameObjectType {
    BALL(Ball.class),
    HOLE(Hole.class),
    PORTAL(Portal.class),
    SPIKE(Spike.class);

    private final Class<? extends GameObject> clazz;

    GameObjectType(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    public GameObject getGameObject() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("Can't create a new game object.");
        }
    }
}
