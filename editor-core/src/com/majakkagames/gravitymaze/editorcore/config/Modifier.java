package com.majakkagames.gravitymaze.editorcore.config;

import com.majakkagames.gravitymaze.editorcore.LevelEditor;

import java.awt.*;

public interface Modifier {
    Component getComponent(LevelEditor editor);
}
