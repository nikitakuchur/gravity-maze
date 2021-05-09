package com.majakkagames.gravitymaze.core.events;

import com.majakkagames.gravitymaze.core.game.Level;

public class LevelEvent implements Event {

    private final Level level;

    public LevelEvent(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
