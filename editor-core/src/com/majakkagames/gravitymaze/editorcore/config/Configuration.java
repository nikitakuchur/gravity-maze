package com.majakkagames.gravitymaze.editorcore.config;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.serialization.annotations.Transient;

public class Configuration {

    private Configuration() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isGameObjectTransient(Class<? extends GameObject> clazz) {
        return clazz.getAnnotation(Transient.class) != null;
    }

    public static boolean isGameObjectLocked(Configurator configurator, Class<? extends GameObject> clazz) {
        return configurator.getLockedGameObjects().stream()
                .anyMatch(type -> type.isAssignableFrom(clazz));
    }
}
