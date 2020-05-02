package com.github.nikitakuchur.puzzlegame;

import com.badlogic.gdx.Gdx;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.badlogic.gdx.Game;

public class PuzzleGame extends Game {
	
	@Override
	public void create() {
	    this.setScreen(new GameScreen());
	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		if (delta > 0.05f) delta = 0.05f;
		screen.render(delta);
	}
}