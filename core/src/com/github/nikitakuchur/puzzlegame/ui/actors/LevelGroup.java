package com.github.nikitakuchur.puzzlegame.ui.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.github.nikitakuchur.puzzlegame.ui.FontGenerator;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetActorCommand;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetLevelCommand;
import com.github.nikitakuchur.puzzlegame.ui.commands.SetScreenCommand;

import java.util.function.Supplier;

public class LevelGroup extends Group implements Disposable {
    private final BitmapFont font;
    private final TextButton settingsMenuButton;

    private final Label fpsLabel;
    private final Label scoreLabel;
    private final Supplier<Integer> getScore;

    public LevelGroup(SetScreenCommand setScreenCommand, SetActorCommand setActorCommand, SetLevelCommand setLevelCommand, Supplier<Integer> getScore) {
        this.getScore = getScore;
        font = FontGenerator.getFont(Gdx.graphics.getWidth() / 16);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // SettingsMenu button
        settingsMenuButton = new TextButton("#", textButtonStyle);
        settingsMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setActorCommand.add(new InGameSettingsGroup(setScreenCommand, setActorCommand, setLevelCommand));
                setActorCommand.execute();
            }
        });
        this.addActor(settingsMenuButton);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // FPS label
        fpsLabel = new Label("FPS: 0", labelStyle);
        fpsLabel.setFontScale(0.5f);
        fpsLabel.setAlignment(Align.bottomLeft);
        this.addActor(fpsLabel);

        // Score label
        scoreLabel = new Label("", labelStyle);
        scoreLabel.setAlignment(Align.center);
        this.addActor(scoreLabel);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        settingsMenuButton.setPosition((float) Gdx.graphics.getWidth() / 2 - (float) 2 * settingsMenuButton.getWidth() - 20,
                (float) Gdx.graphics.getHeight() / 2 - (float) 1.5 * settingsMenuButton.getHeight());

        fpsLabel.setPosition(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2);
        scoreLabel.setPosition(0, (float) Gdx.graphics.getHeight() / 2 - (float) Gdx.graphics.getHeight() / 20);

        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        scoreLabel.setText(this.getScore.get());
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
