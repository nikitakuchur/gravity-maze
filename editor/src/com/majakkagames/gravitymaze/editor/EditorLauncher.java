package com.majakkagames.gravitymaze.editor;

import com.alee.laf.WebLookAndFeel;
import com.badlogic.gdx.Gdx;
import com.majakkagames.mazecore.game.GameMap;
import com.majakkagames.mazecore.game.GameObject;
import com.majakkagames.mazecore.game.Level;
import com.majakkagames.mazecore.editor.Editor;
import com.majakkagames.mazecore.editor.config.Configurator;
import com.majakkagames.gravitymaze.game.gameobjects.*;
import com.majakkagames.gravitymaze.game.gameobjects.mazeobjects.*;
import com.majakkagames.gravitymaze.game.gameobjects.mazeobjects.Box;
import com.majakkagames.gravitymaze.game.physics.Physics;

public class EditorLauncher {

    public static void main(String[] arg) {
        WebLookAndFeel.install();
        Editor.launch(Configurator.builder()
                .newLevelCreator(new NewLevelCreator())
                .levelPreparer(EditorLauncher::prepareLevel)
                .addGameObjectCreator("Ball", Ball::new)
                .addGameObjectCreator("Cup", Cup::new)
                .addGameObjectCreator("Portal", Portal::new)
                .addGameObjectCreator("Spike", Spike::new)
                .addGameObjectCreator("Box", Box::new)
                .addGameObjectCreator("Magnet", Magnet::new)
                .addGameObjectCreator("Conveyor", Conveyor::new)
                .addGameObjectCreator("Barrier", Barrier::new)
                .addGameObjectCreator("Switch", Switch::new)
                .addLockedGameObject(Background.class)
                .addLockedGameObject(GameMap.class)
                .addLockedGameObject(LevelProperties.class)
                .addModifier(Maze.class, new MazeModifier())
                .build());
    }

    private static void prepareLevel(Level level) {
        addIfAbsent(level, new Background());
        addIfAbsent(level, new Maze());
        addIfAbsent(level, new LevelProperties());
        level.setWidth(Gdx.graphics.getWidth());
        level.setHeight(Gdx.graphics.getHeight());
        level.getGameObjectStore().add(new Gravity());
        level.getGameObjectStore().add(new LevelController());
        level.getGameObjectStore().add(new Physics(level));
    }

    private static void addIfAbsent(Level level, GameObject gameObject) {
        if (!level.getGameObjectStore().contains(gameObject.getClass())) {
            level.getGameObjectStore().add(gameObject);
        }
    }
}
