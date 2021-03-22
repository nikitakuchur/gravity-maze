package com.triateq.gravitymaze.editor.utils;

import com.triateq.gravitymaze.actors.gameobjects.Ball;
import com.triateq.gravitymaze.actors.gameobjects.Box;
import com.triateq.gravitymaze.actors.gameobjects.Barrier;
import com.triateq.gravitymaze.actors.gameobjects.GameObject;
import com.triateq.gravitymaze.actors.gameobjects.Cup;
import com.triateq.gravitymaze.actors.gameobjects.Conveyor;
import com.triateq.gravitymaze.actors.gameobjects.Switch;
import com.triateq.gravitymaze.actors.gameobjects.Portal;
import com.triateq.gravitymaze.actors.gameobjects.Spike;
import com.triateq.gravitymaze.actors.gameobjects.Magnet;
import com.triateq.puzzlecore.game.Context;

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
