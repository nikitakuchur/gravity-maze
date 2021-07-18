package com.majakkagames.gravitymaze.editor;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.editorcore.LevelEditor;
import com.majakkagames.gravitymaze.editorcore.LevelManager;
import com.majakkagames.gravitymaze.editorcore.config.Modifier;
import com.majakkagames.gravitymaze.game.cells.CellType;
import com.majakkagames.gravitymaze.game.gameobjects.Maze;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;

public class MazeModifier implements Modifier {

    @Override
    public Component getComponent(LevelEditor editor) {
        return new MazeEditorPanel(editor);
    }

    @SuppressWarnings("java:S1948")
    public static class MazeEditorPanel extends JPanel {

        private final LevelEditor editor;
        private final MazeEditorInputListener inputListener;

        private boolean pressed;

        public MazeEditorPanel(LevelEditor editor) {
            this.editor = editor;
            inputListener = new MazeEditorInputListener(editor.getLevelManager());

            JToggleButton button = new JToggleButton("Edit");
            button.addActionListener(e -> {
                pressed = !pressed;
                if (pressed) {
                    enableMazeEditor();
                } else {
                    disableMazeEditor();
                }
            });
            button.addAncestorListener(new AncestorListener() {
                @Override
                public void ancestorAdded(AncestorEvent event) {
                    // Do nothing
                }

                @Override
                public void ancestorRemoved(AncestorEvent event) {
                    if (pressed) {
                        disableMazeEditor();
                        pressed = false;
                    }
                }

                @Override
                public void ancestorMoved(AncestorEvent event) {
                    // Do nothing
                }
            });
            add(button);
        }

        public void enableMazeEditor() {
            editor.setEditable(false);
            editor.addListener(inputListener);
        }

        public void disableMazeEditor() {
            editor.setEditable(true);
            editor.removeListener(inputListener);
        }
    }

    private static class MazeEditorInputListener extends InputListener {

        private final LevelManager levelManager;
        private final Maze maze;
        private boolean emptyCell;

        public MazeEditorInputListener(LevelManager levelManager) {
            this.levelManager = levelManager;
            GameObjectStore store = levelManager.getLevel().getGameObjectStore();
            this.maze = store.getAnyGameObject(Maze.class);
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Vector2 position = maze.toMapCoords(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            if (maze.isOutside(px, py)) return false;

            if (maze.isEmpty(px, py)) {
                maze.setCellType(px, py, CellType.FILLED);
                emptyCell = false;
            } else if (maze.isFilled(px, py)) {
                maze.setCellType(px, py, CellType.EMPTY);
                emptyCell = true;
            }
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            Vector2 position = maze.toMapCoords(x, y);
            int px = (int) position.x;
            int py = (int) position.y;

            if (maze.isOutside(px, py)) return;

            if (!emptyCell && maze.isEmpty(px, py)) {
                maze.setCellType(px, py, CellType.FILLED);
            } else if (emptyCell && maze.isFilled(px, py)) {
                maze.setCellType(px, py, CellType.EMPTY);
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            levelManager.makeSnapshot();
        }
    }
}
