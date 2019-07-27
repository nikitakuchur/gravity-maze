package com.puzzlegame.editor;

import com.badlogic.gdx.Game;
import com.puzzlegame.screens.GameScreen;

public class Editor extends Game {

    @Override
    public void create() {
        this.setScreen(new GameScreen());
    }
}
