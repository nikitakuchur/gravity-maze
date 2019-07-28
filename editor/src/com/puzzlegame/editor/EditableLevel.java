package com.puzzlegame.editor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.puzzlegame.game.*;

import java.util.ArrayList;
import java.util.List;

public class EditableLevel extends Group implements Disposable {

    private Background background = new Background();
    private Map map = new Map();
    private List<GameObject> gameObjects = new ArrayList<>();
    private List<Ball> balls = new ArrayList<>();

    public EditableLevel() {
        this.addActor(background);

        map.setWidth(100);
        map.setHeight(map.getWidth() / map.getCellsWidth() * map.getCellsHeight());
        this.addActor(map);

        addListener(new EditableLevelInputListener());
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return this;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void dispose() {
        background.dispose();
        map.dispose();
    }

    private class EditableLevelInputListener extends InputListener {

        private boolean emptyCell;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Vector2 position = screenToMapCoordinates(x, y);
            if (map.getCellId((int) position.x, (int) position.y) == 0) {
                map.setCellId((int) position.x, (int) position.y, 1);
                emptyCell = false;
            } else {
                map.setCellId((int) position.x, (int) position.y, 0);
                emptyCell = true;
            }
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            Vector2 position = screenToMapCoordinates(x, y);
            if (!emptyCell && map.getCellId((int) position.x, (int) position.y) == 0) {
                map.setCellId((int) position.x, (int) position.y, 1);
            } else if (emptyCell){
                map.setCellId((int) position.x, (int) position.y, 0);
            }
        }


        public Vector2 screenToMapCoordinates(float x, float y) {
            float cellWidth = map.getWidth() / map.getCellsWidth();
            return new Vector2((int) x / cellWidth + (float) map.getCellsWidth() / 2,
                    y / cellWidth + (float) map.getCellsHeight() / 2);
        }
    }
}
