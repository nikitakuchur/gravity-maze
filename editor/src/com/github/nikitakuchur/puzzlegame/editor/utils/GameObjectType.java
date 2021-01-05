package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Box;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Cup;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Portal;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Spike;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Magnet;

import java.lang.reflect.InvocationTargetException;

public enum GameObjectType {
    BALL(Ball.class),
    CUP(Cup.class),
    PORTAL(Portal.class),
    SPIKE(Spike.class),
    BOX(Box.class),
    MAGNET(Magnet.class);

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
