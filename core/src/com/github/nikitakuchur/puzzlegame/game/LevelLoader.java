package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;

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
}
