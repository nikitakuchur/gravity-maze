package com.android.game.groups;

import com.android.game.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class GameScreenUI extends Group {

    private final GameScreen gameScreen;

    private BitmapFont font;

    private Label fpsLabel;
    private Label scoreLabel;

    private TextButton backButton;
    private TextButton resetButton;

    /**
     * Create a new ui for the game screen
     *
     * @param gameScreen the game screen
     */
    public GameScreenUI(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        // Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getWidth() / 16;
        font = generator.generateFont(parameter);
        generator.dispose();

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
        scoreLabel.setPosition(Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() - (float) Gdx.graphics.getHeight() / 20);
        this.addActor(scoreLabel);

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Back button
        backButton = new TextButton("Back", textButtonStyle);
        backButton.setPosition((float) Gdx.graphics.getWidth() / 3 - backButton.getWidth() / 2,
                (float) Gdx.graphics.getHeight() / 10);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        this.addActor(backButton);

        // Reset button
        resetButton = new TextButton("Reset", textButtonStyle);
        resetButton.setPosition(2 * (float) Gdx.graphics.getWidth() / 3 - resetButton.getWidth() / 2,
                (float) Gdx.graphics.getHeight() / 10);
        resetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Stage stage = gameScreen.getStage();
                Level level = gameScreen.getLevel();

                Level newlevel = new Level();
                newlevel.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

                int index = stage.getActors().indexOf(level, true);
                stage.getActors().set(index, newlevel);
                gameScreen.setLevel(newlevel);
                level.dispose();
            }
        });
        this.addActor(resetButton);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        scoreLabel.setText(gameScreen.getLevel().getScore());
    }

    /**
     * Releases all resources of this object
     */
    public void dispose() {
        font.dispose();
    }
}
