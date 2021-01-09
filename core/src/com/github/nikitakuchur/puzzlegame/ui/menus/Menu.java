package com.github.nikitakuchur.puzzlegame.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;

public abstract class Menu<T extends GameScreen> extends Group {
    private final MenuStack menuStack;
    private final T gameScreen;
    private static final String MENU_BACKGROUND_PATH = "game/menu/";

    protected Menu(MenuStack menuStack, T gameScreen) {
        this.menuStack = menuStack;
        this.gameScreen = gameScreen;
    }

    protected Menu(MenuStack menuStack, T gameScreen, String backgroundName) {
        this.menuStack = menuStack;
        this.gameScreen = gameScreen;

        setBackground(new Texture(Gdx.files.internal(MENU_BACKGROUND_PATH + backgroundName), true));
    }

    private void setBackground(Texture texture) {
        Image background = new Image(texture);
        background.setPosition(-(float) Gdx.graphics.getWidth() / 4, -(float) Gdx.graphics.getHeight() / 4);
        background.setWidth((float) Gdx.graphics.getWidth() / 2);
        background.setHeight((float) Gdx.graphics.getHeight() / 2);

        this.addActor(background);
    }

    public MenuStack getMenuStack() {
        return menuStack;
    }

    public T getGameScreen() {
        return gameScreen;
    }
}
