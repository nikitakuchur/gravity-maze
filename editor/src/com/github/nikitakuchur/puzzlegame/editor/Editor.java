package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.Game;

public class Editor extends Game {

    @Override
    public void create() {
        this.setScreen(new EditorScreen());
    }
}
