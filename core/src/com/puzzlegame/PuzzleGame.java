package com.puzzlegame;

import com.puzzlegame.screens.GameScreen;
import com.badlogic.gdx.Game;

public class PuzzleGame extends Game {
	
	@Override
	public void create() {
	    this.setScreen(new GameScreen());
	}
}