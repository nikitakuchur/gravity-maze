package com.github.nikitakuchur.puzzlegame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.nikitakuchur.puzzlegame.PuzzleGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useGyroscope = false;
		config.useWakelock = true;
		config.hideStatusBar = true;
        config.numSamples = 4;

		initialize(new PuzzleGame(), config);
	}
}