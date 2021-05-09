package com.majakkagames.gravitymaze.core.game;

import com.majakkagames.gravitymaze.core.events.EventHandler;
import com.majakkagames.gravitymaze.core.events.EventHandlerManager;
import com.majakkagames.gravitymaze.core.events.GameObjectEvent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a game object store class. It stores all game objects contained in the level.
 */
public class GameObjectStore {

    private final Level level;

    private final Map<String, List<GameObject>> gameObjects = new HashMap<>();

    private final EventHandlerManager<EventType, GameObjectEvent> eventHandlerManager = new EventHandlerManager<>();

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
        GameObjectEvent event = new GameObjectEvent(gameObject);
        eventHandlerManager.fire(EventType.ADDED, event);
    }

    /**
     * Removes the given game object from the store.
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
        GameObjectEvent event = new GameObjectEvent(gameObject);
        eventHandlerManager.fire(EventType.REMOVED, event);
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
     * Checks if the store has any game object with the given name or not.
     *
     * @param name the name
     * @return true if any game object is contained in the store and false otherwise
     */
    public boolean contains(String name) {
        Objects.requireNonNull(name);
        return getGameObjects().stream()
                .anyMatch(gameObject -> name.equals(gameObject.getName()));
    }

    /**
     * Checks if the store has any game object with the given type.
     *
     * @param clazz the type
     * @return true if any game object is contained in the store and false otherwise
     */
    public boolean contains(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return gameObjects.containsKey(clazz.getName());
    }

    /**
     * Checks if the store has any game object with the given type and name.
     *
     * @param clazz the type
     * @param name  the name
     * @return true if any game object is contained in the store and false otherwise
     */
    public boolean contains(Class<?> clazz, String name) {
        Objects.requireNonNull(clazz);
        if (name == null) return false;
        return Optional.ofNullable(gameObjects.get(clazz.getName()))
                .map(list -> list.stream()
                        .anyMatch(gameObject -> name.equals(gameObject.getName())))
                .orElse(false);
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
    public <T extends GameObject> T find(Class<T> clazz, String name) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(name);
        return getGameObjects(clazz).stream()
                .filter(gameObject -> name.equals(gameObject.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Cannot find game object"));
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
        if (list == null) return Collections.emptyList();
        return list.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    /**
     * Returns any game object of the given type.
     *
     * @param clazz the class
     * @return the found game object or null
     */
    public <T> T getAnyGameObject(Class<T> clazz) {
        List<GameObject> result = gameObjects.get(clazz.getName());
        return Optional.ofNullable(result)
                .flatMap(list -> list.stream()
                        .map(clazz::cast)
                        .findAny())
                .orElseThrow(() -> new IllegalStateException("Cannot find " + clazz.getName()));
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
            GameObjectEvent event = new GameObjectEvent(gameObject);
            eventHandlerManager.fire(EventType.REMOVED, event);
        });
        gameObjects.clear();
    }

    /**
     * Adds the given event handler.
     *
     * @param handler the event handler
     */
    public void addEventHandler(EventType type, EventHandler<GameObjectEvent> handler) {
        eventHandlerManager.add(type, handler);
    }

    public enum EventType {
        ADDED, REMOVED
    }
}
