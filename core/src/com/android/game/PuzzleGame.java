package com.android.game;

import com.android.game.screens.GameScreen;
import com.badlogic.gdx.Game;

public class PuzzleGame extends Game {
	
	@Override
	public void create() {
	    this.setScreen(new GameScreen());
	}
}