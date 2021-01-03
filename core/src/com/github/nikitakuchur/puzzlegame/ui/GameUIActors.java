package com.github.nikitakuchur.puzzlegame.ui;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.github.nikitakuchur.puzzlegame.screens.MainMenuScreen;
import com.github.nikitakuchur.puzzlegame.ui.actors.GameLabels;
import com.github.nikitakuchur.puzzlegame.ui.actors.MenuBackground;
import com.github.nikitakuchur.puzzlegame.ui.actors.buttons.Copyable;
import com.github.nikitakuchur.puzzlegame.ui.actors.buttons.InGameButtonsGroup;
import com.github.nikitakuchur.puzzlegame.ui.actors.buttons.InGameSettingsButtonsGroup;
import com.github.nikitakuchur.puzzlegame.ui.actors.buttons.MainMenuButtonsGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class GameUIActors implements Disposable {
    private final Stack<List<Actor>> uiStack;
    private final ScreenAdapter screen;
    private final Stage stage;

    public GameUIActors(ScreenAdapter screen, Stage stage) {
        this.stage = stage;
        this.screen = screen;

        uiStack = new Stack<>();
    }

    /**
     * Add copy of each actors from top of stack to stage
     */
    private void initStage() {
        if (uiStack.size() > 0)
            uiStack.peek().forEach(item -> {
                stage.addActor((Actor) ((Copyable) item).copy());
            });
    }

    /**
     * Remove actors in stage like actors from top of stack
     */
    private void removeFromStage() {
        uiStack.peek().forEach(item -> {
            stage.getActors().removeValue(Arrays.stream(stage.getActors().items)
                    .filter(actor -> actor.getClass().equals(item.getClass()))
                    .findFirst().get(), true);
        });
    }

    public List<Actor> popFromStage() {
        if (uiStack.size() == 0)
            return null;

        removeFromStage();

        List<Actor> actors = uiStack.pop();

        initStage();

        return actors;
    }

    public void openMenu(MenuType type) {
        if (uiStack.size() > 0)
            removeFromStage();

        switch (type) {
            case Main:
                openMain();
                break;
            case InGameDefault:
                openInGameDefault();
                break;
            case InGameSettings:
                openSettingsMain();
                break;
        }

        initStage();
    }

    private void openMain() {
        uiStack.push(Arrays.asList(new MenuBackground(),
                new MainMenuButtonsGroup((MainMenuScreen) screen)));
    }

    private void openInGameDefault() {
        List<Actor> actorsList = new ArrayList<>();

        actorsList.add(new GameLabels((GameScreen) screen));
        actorsList.add(new InGameButtonsGroup(this));

        uiStack.push(actorsList);

        initStage();
    }

    private void openSettingsMain() {
        uiStack.push(Arrays.asList(new MenuBackground(),
                new InGameSettingsButtonsGroup((GameScreen) screen, this)));
    }

    @Override
    public void dispose() {
        uiStack.forEach(list -> {
            list.forEach(item -> {
                ((Disposable) item).dispose();
            });
        });
    }
}
