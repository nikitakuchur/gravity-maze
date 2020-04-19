package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Hole;
import com.github.nikitakuchur.puzzlegame.game.entities.gameobjects.Portal;

import java.lang.reflect.InvocationTargetException;

public enum GameObjectType {
    BALL(Ball.class),
    HOLE(Hole.class),
    PORTAL(Portal.class);

    private final Class<? extends GameObject> clazz;

    GameObjectType(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    public GameObject getGameObject() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }
}
