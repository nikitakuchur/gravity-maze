package com.github.nikitakuchur.puzzlegame.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.nikitakuchur.puzzlegame.game.*;
import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.Ball;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.Hole;
import com.github.nikitakuchur.puzzlegame.game.gameobjects.Portal;
import com.github.nikitakuchur.puzzlegame.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;

import static com.github.nikitakuchur.puzzlegame.game.cells.CellType.BLOCK;
import static com.github.nikitakuchur.puzzlegame.game.cells.CellType.EMPTY;

public class GameScreen extends ScreenAdapter {

    private Stage stage = new Stage(new ScreenViewport());

    private Level level;
    private GameUI gameUI = new GameUI(this);

    /**
     * Creates a new game screen
     */
    public GameScreen() {
        stage.getCamera().position.set(Vector3.Zero);

        Map map = new Map(new CellType[][]{{EMPTY, BLOCK, BLOCK, BLOCK, BLOCK, BLOCK, EMPTY, EMPTY},
                                           {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK},
                                           {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK},
                                           {EMPTY, EMPTY, EMPTY, BLOCK, BLOCK, EMPTY, EMPTY, BLOCK},
                                           {BLOCK, EMPTY, EMPTY, BLOCK, BLOCK, EMPTY, EMPTY, BLOCK},
                                           {BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
                                           {BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK},
                                           {BLOCK, BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK, BLOCK},
                                           {BLOCK, BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK, BLOCK},
                                           {BLOCK, BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK, BLOCK},
                                           {BLOCK, BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK, BLOCK},
                                           {BLOCK, BLOCK, EMPTY, EMPTY, EMPTY, EMPTY, BLOCK, BLOCK}});

        // Add holes
        Hole blueHole = new Hole();
        blueHole.setColor(0.14f, 0.35f, 0.76f, 1);
        blueHole.setPosition(9, 2);

        Hole pinkHole = new Hole();
        pinkHole.setColor(0.86f, 0.34f, 0.68f, 1);
        pinkHole.setPosition(11, 5);

        // Add portals
        Portal portalOne = new Portal();
        portalOne.setPosition(6, 6);

        Portal portalTwo = new Portal();
        portalTwo.setPosition(11, 2);

        portalOne.to(portalTwo);
        portalTwo.to(portalOne);

        // Add balls
        Ball blueBall = new Ball();
        blueBall.setColor(blueHole.getColor());
        blueBall.setPosition(0, 0);

        Ball pinkBall = new Ball();
        pinkBall.setColor(pinkHole.getColor());
        pinkBall.setPosition(4, 1);

        blueHole.addBall(blueBall);
        pinkHole.addBall(pinkBall);

        level = Level.builder()
                .background(Background.BLUE)
                .map(map)
                .addGameObjects(blueHole, pinkHole, portalOne, portalTwo, blueBall, pinkBall)
                .build();

        stage.addActor(level);
        stage.addActor(gameUI);
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the level
     *
     * @param level the level
     */
    public void setLevel(Level level) {
        int index = stage.getActors().indexOf(this.level, true);
        stage.getActors().set(index, level);
        this.level.dispose();
        this.level = level;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        level.dispose();
        gameUI.dispose();
    }
}