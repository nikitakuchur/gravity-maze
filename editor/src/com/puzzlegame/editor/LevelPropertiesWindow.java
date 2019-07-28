package com.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;

public class LevelPropertiesWindow extends VisWindow {

    private final EditorUI editorUI;

    public LevelPropertiesWindow(EditorUI editorUI) {
        super("Level Properties");
        this.editorUI = editorUI;
        this.setResizable(true);

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setSize(300, 100);
        setPosition(-Gdx.graphics.getWidth() / 2 + 10, -(float) Gdx.graphics.getHeight() / 2 + 10);
        addWidgets();
    }

    private void addWidgets() {
        VisTable textFieldTable = new VisTable(true);

        textFieldTable.defaults().right();
        textFieldTable.add(new VisLabel("Background:"));
        textFieldTable.add(new VisTextField());
        add(textFieldTable).row();

        VisTextButton editMapButton = new VisTextButton("Edit Map", "toggle", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EditableLevel editableLevel = editorUI.getEditorScreen().getEditableLevel();
                editableLevel.setMapEditing(!editableLevel.isMapEditing());
            }
        });
        this.defaults().fillX();
        add(editMapButton).row();
    }
}
