package com.github.nikitakuchur.puzzlegame.editor;

import com.badlogic.gdx.Game;

public class EditorApplication extends Game {
    @Override
    public void create() {
        this.setScreen(new EditorScreen());
    }
}
