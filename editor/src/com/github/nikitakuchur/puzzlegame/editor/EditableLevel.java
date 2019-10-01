package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.game.Level;

public class EditableLevel extends Level implements Disposable {

    private boolean editingMode;

    public EditableLevel() {
        super();
        setPause(true);

        addListener(new EditableLevelInputListener());
    }

    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }

    public boolean isMapEditing() {
        return editingMode;
    }

    public Vector2 screenToMapCoordinates(float x, float y) {
        float cellWidth = getMap().getWidth() / getMap().getCellsWidth();
        return new Vector2((int) x / cellWidth + (float) getMap().getCellsWidth() / 2,
                y / cellWidth + (float) getMap().getCellsHeight() / 2);
    }

    private class EditableLevelInputListener extends InputListener {

        private boolean emptyCell;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (editingMode) {
                Vector2 position = screenToMapCoordinates(x, y);
                if (getMap().getCellId((int) position.x, (int) position.y) == 0) {
                    getMap().setCellId((int) position.x, (int) position.y, 1);
                    emptyCell = false;
                } else {
                    getMap().setCellId((int) position.x, (int) position.y, 0);
                    emptyCell = true;
                }
            }
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (editingMode) {
                Vector2 position = screenToMapCoordinates(x, y);
                if (!emptyCell && getMap().getCellId((int) position.x, (int) position.y) == 0) {
                    getMap().setCellId((int) position.x, (int) position.y, 1);
                } else if (emptyCell){
                    getMap().setCellId((int) position.x, (int) position.y, 0);
                }
            }
        }
    }
}
