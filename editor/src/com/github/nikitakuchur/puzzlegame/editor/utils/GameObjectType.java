package com.github.nikitakuchur.puzzlegame.editor.utils;

import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Box;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Barrier;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.GameObject;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Cup;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Conveyor;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Switch;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Portal;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Spike;
import com.github.nikitakuchur.puzzlegame.actors.gameobjects.Magnet;
import com.github.nikitakuchur.puzzlegame.utils.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum GameObjectType {
    BALL(Ball.class),
    CUP(Cup.class),
    PORTAL(Portal.class),
    SPIKE(Spike.class),
    BOX(Box.class),
    MAGNET(Magnet.class),
    CONVEYOR(Conveyor.class),
    BARRIER(Barrier.class),
    SWITCH(Switch.class);

    private final Class<? extends GameObject> clazz;

    GameObjectType(Class<? extends GameObject> clazz) {
        this.clazz = clazz;
    }

    public GameObject newInstance(Context context) {
        try {
            return createGameObject(clazz, context);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("Can't create a new game object.");
        }
    }

    private GameObject createGameObject(Class<?> clazz, Context context) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor(Context.class);
            return (GameObject) constructor.newInstance(context);
        } catch (NoSuchMethodException ignored) {
            constructor = clazz.getConstructor();
            return (GameObject) constructor.newInstance();
        }
    }
}
