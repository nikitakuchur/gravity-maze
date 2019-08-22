package com.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;

public class LevelPropertiesWindow extends VisWindow {

    private final EditorUI editorUI;
    private boolean editingMode;

    public LevelPropertiesWindow(EditorUI editorUI) {
        super("Level Properties");
        this.editorUI = editorUI;
        this.setResizable(true);

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setSize(300, 100);
        setPosition(-(float) Gdx.graphics.getWidth() / 2 + 10, -(float) Gdx.graphics.getHeight() / 2 + 10);
        addWidgets();
    }
    @Override
    public void act(float delta) {
        EditableLevel editableLevel = editorUI.getEditorScreen().getEditableLevel();
        editableLevel.setEditingMode(editingMode);
        super.act(delta);
    }

    private void addWidgets() {
        VisTable textFieldTable = new VisTable(true);

        textFieldTable.defaults().right();
        textFieldTable.add(new VisLabel("Background:"));
        VisSelectBox<String> selectBox = new VisSelectBox<>();
        selectBox.setItems("Theme 1", "Theme 2", "Theme 3");
        textFieldTable.add(selectBox);
        add(textFieldTable).row();

        VisTextButton editMapButton = new VisTextButton("Edit Map", "toggle", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                editingMode = !editingMode;
            }
        });
        this.defaults().fillX();
        add(editMapButton).row();
    }
}
