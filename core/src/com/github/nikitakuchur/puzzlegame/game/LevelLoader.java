package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.GameObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    private LevelLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static Level load(FileHandle handle) {
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonValue = jsonReader.parse(handle);
        return Level.builder()
                .background(parseBackground(jsonValue))
                .map(parseMap(jsonValue))
                .addGameObjects(parseGameObjects(jsonValue))
                .build();
    }

    private static Background parseBackground(JsonValue jsonValue) {
        if (!jsonValue.has("background")) return null;
        JsonValue backgroundJson = jsonValue.get("background");
        return Background.getBackground(backgroundJson.asString());
    }

    private static GameMap parseMap(JsonValue jsonValue) {
        if (!jsonValue.has("map")) return new GameMap(new CellType[][]{{}});
        JsonValue mapJson = jsonValue.get("map");
        CellType[][] cells = new CellType[mapJson.size][];

        for (int i = 0; i < mapJson.size; i++) {
            int[] cellsData = mapJson.get(i).asIntArray();
            cells[i] = new CellType[cellsData.length];

            for (int j = 0; j < cellsData.length; j++) {
                cells[i][j] = cellsData[j] == 1 ? CellType.BLOCK : CellType.EMPTY;
            }
        }

        return new GameMap(cells);
    }

    private static List<GameObject> parseGameObjects(JsonValue jsonValue) {
        JsonValue gameObjectsJson = jsonValue.get("gameObjects");
        List<GameObject> gameObjects = new ArrayList<>();

        for (int i = 0; i < gameObjectsJson.size; i++) {
            JsonValue gameObjectJson = gameObjectsJson.get(i);
            if (!gameObjectJson.has("type")) continue;
            String type = gameObjectJson.getString("type");
            GameObject gameObject = createGameObjectByName(type);
            if (gameObject != null) {
                gameObject.restore(gameObjectJson);
                gameObjects.add(gameObject);
            }
        }

        return gameObjects;
    }

    private static GameObject createGameObjectByName(String name) {
        String gameObjectPackage = "com.github.nikitakuchur.puzzlegame.game.gameobjects";
        try {
            String normalName = name.substring(0, 1).toUpperCase() + name.substring(1);
            Class<?> clazz = Class.forName(gameObjectPackage + "." + normalName);
            Constructor<?> constructor = clazz.getConstructor();
            return (GameObject) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            Gdx.app.error(LevelLoader.class.getName(), "Cannot create a game object with type \"" + name + "\".");
        }
        return null;
    }
}
