package com.majakkagames.gravitymaze.editorcore.config;

import com.majakkagames.gravitymaze.core.game.Context;
import com.majakkagames.gravitymaze.core.game.Level;

public interface LevelCreator {
    Level create(Context context);
}
