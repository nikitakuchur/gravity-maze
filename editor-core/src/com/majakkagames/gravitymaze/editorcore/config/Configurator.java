package com.majakkagames.gravitymaze.editorcore.config;

import com.majakkagames.gravitymaze.core.game.GameObject;
import com.majakkagames.gravitymaze.core.game.Level;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Configurator {

    private final LevelCreator levelCreator;
    private final Consumer<Level> levelPreparer;
    private final List<GameObjectCreator> gameObjectCreators;
    private final Set<Class<? extends GameObject>> lockedGameObjects;
    private final Map<Class<? extends GameObject>, Modifier> modifiers;

    private Configurator(Builder builder) {
        this.levelCreator = builder.levelCreator;
        this.levelPreparer = builder.levelPreparer;
        this.gameObjectCreators = Collections.unmodifiableList(builder.gameObjectCreators);
        this.lockedGameObjects = Collections.unmodifiableSet(builder.lockedGameObjects);
        this.modifiers = Collections.unmodifiableMap(builder.modifiers);
    }

    public LevelCreator getLevelCreator() {
        return levelCreator;
    }

    public Consumer<Level> getLevelPreparer() {
        return levelPreparer;
    }

    public List<GameObjectCreator> getGameObjectCreators() {
        return gameObjectCreators;
    }

    public Set<Class<? extends GameObject>> getLockedGameObjects() {
        return lockedGameObjects;
    }

    public Modifier getModifier(Class<? extends GameObject> clazz) {
        if (!modifiers.containsKey(clazz)) return null;
        return modifiers.get(clazz);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LevelCreator levelCreator = Level::new;
        private Consumer<Level> levelPreparer = level -> {};
        private final List<GameObjectCreator> gameObjectCreators = new ArrayList<>();
        private final Set<Class<? extends GameObject>> lockedGameObjects = new HashSet<>();
        private final Map<Class<? extends GameObject>, Modifier> modifiers = new HashMap<>();

        public Builder newLevelCreator(LevelCreator creator) {
            levelCreator = creator;
            return this;
        }

        public Builder levelPreparer(Consumer<Level> preparer) {
            levelPreparer = preparer;
            return this;
        }

        public Builder addGameObjectCreator(String name, Supplier<GameObject> creator) {
            gameObjectCreators.add(new GameObjectCreator() {
                @Override
                public GameObject create() {
                    return creator.get();
                }

                @Override
                public String toString() {
                    return name;
                }
            });
            return this;
        }

        public Builder addLockedGameObject(Class<? extends GameObject> clazz) {
            lockedGameObjects.add(clazz);
            return this;
        }

        public Builder addModifier(Class<? extends GameObject> clazz, Modifier modifier) {
            modifiers.put(clazz, modifier);
            return this;
        }

        public Configurator build() {
            return new Configurator(this);
        }
    }
}
