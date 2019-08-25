package com.puzzlegame.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.puzzlegame.game.Ball;
import com.puzzlegame.game.GameObject;
import com.puzzlegame.game.Hole;
import com.puzzlegame.game.Portal;

public class GameObjectsWindow extends VisWindow  {

    VisList<String> list = new VisList<>();

    private boolean selected;

    public GameObjectsWindow() {
        super("Game Objects");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        setSize(120, 150);
        setPosition(-(float) Gdx.graphics.getWidth() / 2 + 10, (float) Gdx.graphics.getHeight() / 2 - getHeight() - 35);
        addWidgets();
        addListener(new GameObjectsWindowInputListener());
    }

    private void addWidgets() {
        list.setItems("Ball", "Hole", "Portal");
        list.getSelection().clear();
        add(list);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!selected) {
            list.getSelection().clear();
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
    }

    public GameObject getSelectedObject() {
        String selected = list.getSelected();
        switch (selected) {
            case "Ball":
                return new Ball();
            case "Hole":
                return new Hole();
            case "Portal":
                return new Portal();
        }
        return null;
    }

    private class GameObjectsWindowInputListener extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            selected = !selected;
            return true;
        }
    }
}
