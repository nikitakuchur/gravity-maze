package com.github.nikitakuchur.puzzlegame.ui.actors.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.MenuType;
import com.github.nikitakuchur.puzzlegame.ui.GameUIActors;

public class InGameButtonsGroup extends ButtonsGroup {
    private final BitmapFont font;
    private final TextButton settingsMenuButton;
    private final GameUIActors actors;

    public InGameButtonsGroup(GameUIActors actors) {
        this.actors = actors;

        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // SettingsMenu button
        settingsMenuButton = new TextButton("#", textButtonStyle);
        settingsMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actors.openMenu(MenuType.InGameSettings);
            }
        });
        this.addActor(settingsMenuButton);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        settingsMenuButton.setPosition(Gdx.graphics.getWidth() / 2 - (float) 2 * settingsMenuButton.getWidth() - 20,
                Gdx.graphics.getHeight() / 2 - (float) 1.5 * settingsMenuButton.getHeight());
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public InGameButtonsGroup copy() {
        return new InGameButtonsGroup(actors);
    }
}
