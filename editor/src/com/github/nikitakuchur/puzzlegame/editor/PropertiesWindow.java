package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.*;

public class PropertiesWindow extends VisWindow {

    public PropertiesWindow() {
        super("Properties");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setSize(150, 400);
        setPosition((float) Gdx.graphics.getWidth() / 2 - getWidth() - 10,
                (float) Gdx.graphics.getHeight() / 2 - getHeight() - 35);
        addWidgets();
    }

    private void addWidgets() {
    }
}
