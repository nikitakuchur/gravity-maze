package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.GameObject;

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
            JsonValue gameObject = gameObjects.get(i);
            String type = gameObject.getString("type");
            int x = gameObject.getInt("x");
            int y = gameObject.getInt("y");
            if (type.equals("ball")) {
                Ball ball = new Ball();
                ball.setX(x);
                ball.setY(y);
                result.add(ball);
            }

        }

        return result;
    }
}
