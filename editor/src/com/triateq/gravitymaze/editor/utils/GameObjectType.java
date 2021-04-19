package com.triateq.gravitymaze.editor.utils;

import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Ball;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Box;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Barrier;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Cup;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Conveyor;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Switch;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Portal;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Spike;
import com.triateq.gravitymaze.game.gameobjects.mazeobjects.Magnet;

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

    public GameObject newInstance() {
        try {
            return createGameObject(clazz);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("Can't create a new game object");
        }
    }

    private GameObject createGameObject(Class<?> clazz) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = clazz.getConstructor();
        return (GameObject) constructor.newInstance();
    }
}
