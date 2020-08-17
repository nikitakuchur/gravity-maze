package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameObjectManager {

    private final HashMap<String, List<GameObject>> gameObjects = new HashMap<>();

    private final List<Consumer<GameObject>> gameObjectAddListeners = new ArrayList<>();
    private final List<Consumer<GameObject>> gameObjectRemoveListeners = new ArrayList<>();

    public void add(GameObject gameObject) {
        Set<Class<?>> classes = getAllClasses(gameObject.getClass());
        classes.forEach(clazz -> gameObjects.computeIfAbsent(clazz.getName(), c -> new ArrayList<>()).add(gameObject));
        gameObjectAddListeners.forEach(listener -> listener.accept(gameObject));
    }

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
        gameObjectRemoveListeners.forEach(listener -> listener.accept(gameObject));
    }

    public GameObject find(String name) {
        return find(GameObject.class, name);
    }

    public <T extends GameObject> T find(Class<T> clazz, String name) {
        if (name == null) return null;
        return getGameObjects(clazz).stream()
                .filter(gameObject -> name.equals(gameObject.getName()))
                .findAny()
                .orElse(null);
    }

    public List<GameObject> getGameObjects() {
        return getGameObjects(GameObject.class);
    }

    public <T> List<T> getGameObjects(Class<T> clazz) {
        List<GameObject> list = gameObjects.get(clazz.getName());
        if (list == null) return new ArrayList<>();
        return list.stream()
                .map(clazz::cast)
                .collect(Collectors.toList());
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

    public void addGameObjectAddListener(Consumer<GameObject> consumer) {
        gameObjectAddListeners.add(consumer);
    }

    public void clearGameObjectAddListeners() {
        gameObjectAddListeners.clear();
    }

    public void addGameObjectRemoveListener(Consumer<GameObject> consumer) {
        gameObjectRemoveListeners.add(consumer);
    }

    public void clearGameObjectRemoveListeners() {
        gameObjectRemoveListeners.clear();
    }
}
