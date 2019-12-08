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

    private static String GAME_OBJECTS_PACKAGE = "com.github.nikitakuchur.puzzlegame.game.gameobjects";

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
        JsonValue map = jsonValue.get("background");
        return Background.getBackground(map.asString());
    }

    private static Map parseMap(JsonValue jsonValue) {
        JsonValue map = jsonValue.get("map");
        CellType[][] cells = new CellType[map.size][];

        for (int i = 0; i < map.size; i++) {
            int[] cellsData = map.get(i).asIntArray();
            cells[i] = new CellType[cellsData.length];

            for (int j = 0; j < cellsData.length; j++) {
                cells[i][j] = cellsData[j] == 1 ? CellType.BLOCK : CellType.EMPTY;
            }
        }

        return new Map(cells);
    }

    private static List<GameObject> parseGameObjects(JsonValue jsonValue) {
        JsonValue gameObjects = jsonValue.get("gameObjects");
        List<GameObject> result = new ArrayList<>();

        for (int i = 0; i < gameObjects.size; i++) {
            JsonValue gameObjectJson = gameObjects.get(i);
            String type = gameObjectJson.getString("type");
            int x = gameObjectJson.getInt("x");
            int y = gameObjectJson.getInt("y");

            GameObject gameObject = createGameObjectByName(type);
            if (gameObject == null) continue;
            gameObject.setX(x);
            gameObject.setY(y);
            result.add(gameObject);
        }

        return result;
    }

    private static GameObject createGameObjectByName(String name) {
        try {
            String normalName = name.substring(0, 1).toUpperCase() + name.substring(1);
            Class<?> clazz = Class.forName(GAME_OBJECTS_PACKAGE + "." + normalName);
            Constructor<?> constructor = clazz.getConstructor();
            return (GameObject) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            Gdx.app.error(LevelLoader.class.getName(), "Cannot create \"" + name + "\"");
        }
        return null;
    }
}
