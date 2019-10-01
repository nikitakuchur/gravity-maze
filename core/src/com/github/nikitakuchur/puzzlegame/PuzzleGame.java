package com.github.nikitakuchur.puzzlegame;

import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.badlogic.gdx.Game;

public class PuzzleGame extends Game {
	
	@Override
	public void create() {
	    this.setScreen(new GameScreen());
	}
}