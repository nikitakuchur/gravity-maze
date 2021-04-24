package com.majakkagames.gravitymaze.core.game;

import com.majakkagames.gravitymaze.core.events.Event;
import com.majakkagames.gravitymaze.core.events.EventHandler;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This is a game object store class. It stores all game objects contained in the level.
 */
public class GameObjectStore {

    private final Level level;

    private final Map<String, List<GameObject>> gameObjects = new HashMap<>();

    private final List<EventHandler<GameObjectEvent>> eventHandlers = new ArrayList<>();

    /**
     * Creates a new game object store.
     *
     * @param level the level
     */
    public GameObjectStore(Level level) {
        this.level = level;
    }

    /**
     * Initializes game object store. This method initializes every game object.
     */
    public void initialize() {
        getGameObjects().forEach(gameObject -> gameObject.initialize(level));
    }

    /**
     * Adds the given game object to the store.
     *
     * @param gameObject the game object
     */
    public void add(GameObject gameObject) {
        Set<Class<?>> classes = getAllClasses(gameObject.getClass());
        classes.forEach(clazz -> gameObjects.computeIfAbsent(clazz.getName(), c -> new ArrayList<>()).add(gameObject));
        GameObjectEvent event = new GameObjectEvent(GameObjectEvent.Type.ADD, gameObject);
        eventHandlers.forEach(listener -> listener.handle(event));
    }

    /**
     * Remove the given game object from the store.
     *
     * @param gameObject the game object
     */
    public void remove(GameObject gameObject) {
        Set<Class<?>> classes = getAllClasses(gameObject.getClass());
        classes.forEach(clazz -> {
            List<GameObject> list = gameObjects.get(clazz.getName());
            if (list != null) {
                list.remove(gameObject);
                if (list.isEmpty()) {
                    gameObjects.remove(clazz.getName());
                }
            }
        });
        GameObjectEvent event = new GameObjectEvent(GameObjectEvent.Type.REMOVE, gameObject);
        eventHandlers.forEach(listener -> listener.handle(event));
    }

    /**
     * Checks if the given game object is contained in the store or not.
     *
     * @param gameObject the game object
     * @return true if the game object is contained in the store and false otherwise
     */
    public boolean contains(GameObject gameObject) {
        if (gameObject == null) return false;
        return getGameObjects(gameObject.getClass()).stream()
                .anyMatch(object -> object == gameObject);
    }

    /**
     * Finds a game object by its given name.
     *
     * @param name the name
     * @return the founded game object or null
     */
    public GameObject find(String name) {
        return find(GameObject.class, name);
    }

    /**
     * Finds a game object of the given class by its name.
     *
     * @param clazz the class
     * @param name  the name
     * @return the founded game object or null
     */
    public <T> T find(Class<T> clazz, String name) {
        if (name == null) return null;
        return getGameObjects(clazz).stream()
                .filter(gameObject -> name.equals(((GameObject) gameObject).getName()))
                .findAny()
                .orElse(null);
    }

    /**
     * Returns all game objects that are contained in the store.
     *
     * @return the list of game objects
     */
    public List<GameObject> getGameObjects() {
        return getGameObjects(GameObject.class);
    }

    /**
     * Returns all game objects of the given type.
     *
     * @param clazz the class
     * @return the list of game objects
     */
    public <T> List<T> getGameObjects(Class<T> clazz) {
        List<GameObject> list = gameObjects.get(clazz.getName());
        if (list == null) return new ArrayList<>();
        return list.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    public <T> T getAnyGameObjectOrThrow(Class<T> clazz, Supplier<RuntimeException> exception) {
        T gameObject = getAnyGameObject(clazz);
        if (gameObject == null) {
            throw exception.get();
        }
        return gameObject;
    }

    /**
     * Returns any game object of the given type.
     *
     * @param clazz the class
     * @return the found game object or null
     */
    public <T> T getAnyGameObject(Class<T> clazz) {
        List<GameObject> list = gameObjects.get(clazz.getName());
        if (list == null) return null;
        return list.stream()
                .map(clazz::cast)
                .findAny()
                .orElse(null);
    }

    private Set<Class<?>> getAllClasses(Class<?> clazz) {
        Set<Class<?>> classes = new HashSet<>();
        getAllClasses(clazz, classes);
        return classes;
    }

    private void getAllClasses(Class<?> clazz, Set<Class<?>> classes) {
        classes.add(clazz);
        Class<?> superclass = clazz.getSuperclass();
        Class<?>[] interfaces = clazz.getInterfaces();
        if (superclass != null) {
            getAllClasses(superclass, classes);
        }
        for (Class<?> inter : interfaces) {
            getAllClasses(inter, classes);
        }
    }

    /**
     * Clears the store.
     */
    public void clear() {
        getGameObjects().forEach(gameObject -> {
            GameObjectEvent event = new GameObjectEvent(GameObjectEvent.Type.ADD, gameObject);
            eventHandlers.forEach(handler -> handler.handle(event));
        });
        gameObjects.clear();
    }

    public void addEventHandler(EventHandler<GameObjectEvent> handler) {
        eventHandlers.add(handler);
    }

    public static class GameObjectEvent implements Event {

        public enum Type {
            ADD, REMOVE
        }

        private final Type type;
        private final GameObject gameObject;

        public GameObjectEvent(Type type, GameObject gameObject) {
            this.type = type;
            this.gameObject = gameObject;
        }

        public Type getType() {
            return type;
        }

        public GameObject getGameObject() {
            return gameObject;
        }
    }
}
