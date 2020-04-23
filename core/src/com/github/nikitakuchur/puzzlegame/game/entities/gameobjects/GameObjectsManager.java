package com.github.nikitakuchur.puzzlegame.game.entities.gameobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameObjectsManager {

    private HashMap<Class<?>, List<GameObject>> gameObjects = new HashMap<>();

    private List<Consumer<GameObject>> gameObjectAddListeners = new ArrayList<>();
    private List<Consumer<GameObject>> gameObjectRemoveListeners = new ArrayList<>();

    public void add(GameObject gameObject) {
        Set<Class<?>> classes = getAllClasses(gameObject.getClass());
        classes.forEach(clazz -> {
            List<GameObject> list = gameObjects.get(clazz);
            if (list != null) {
                list.add(gameObject);
            } else {
                gameObjects.put(clazz, new ArrayList<>(Collections.singletonList(gameObject)));
            }
        });
        gameObjectAddListeners.forEach(listener -> listener.accept(gameObject));
    }

    public void remove(GameObject gameObject) {
        Set<Class<?>> classes = getAllClasses(gameObject.getClass());
        classes.forEach(clazz -> {
            List<GameObject> list = gameObjects.get(clazz);
            if (list != null) {
                list.remove(gameObject);
                if (list.isEmpty()) {
                    gameObjects.remove(clazz);
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
        List<GameObject> list = gameObjects.get(clazz);
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
        if (interfaces != null) {
            for (Class<?> inter : interfaces) {
                getAllClasses(inter, classes);
            }
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
