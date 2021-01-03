package com.github.nikitakuchur.puzzlegame.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.screens.GameScreen;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.actors.buttons.Copyable;

public class MenuBackground extends Group implements Disposable, Copyable {
    private final Texture menuBg = new Texture(Gdx.files.internal("game/menu/bg1.png"), true);

    public MenuBackground() {
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(menuBg, -Gdx.graphics.getWidth()/4, -Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }

    @Override
    public void dispose() {
        menuBg.dispose();
    }

    @Override
    public MenuBackground copy() {
        return new MenuBackground();
    }
}
