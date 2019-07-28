package com.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisWindow;

public class ObjectsWindow extends VisWindow  {

    public ObjectsWindow() {
        super("Objects");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setSize(75, 150);
        setPosition(-Gdx.graphics.getWidth() / 2 + 10, (float) Gdx.graphics.getHeight() / 2 - getHeight() - 35);
        addWidgets();
    }

    private void addWidgets() {
        VisList<String> list = new VisList<>();
        list.setItems("Ball", "Hole", "Portal");
        add(list);
    }
}
