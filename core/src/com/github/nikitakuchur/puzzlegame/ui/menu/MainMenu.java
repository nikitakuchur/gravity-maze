package com.github.nikitakuchur.puzzlegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.nikitakuchur.puzzlegame.actors.Background;
import com.github.nikitakuchur.puzzlegame.ui.Menu;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.utils.Context;

import java.util.Random;

public class MainMenu extends Menu {

    private static final Background GREEN_BACKGROUND = new Background(
            new Color(0.5764706f, 1.f, 0.75686276f, 1.f),
            new Color(0.02745098f, 0.65882355f, 0.7294118f, 1f)
    );

    private static final Background ORANGE_BACKGROUND = new Background(
            new Color(0.96078431f, 0.80392156f, 0.1921568f, 1.f),
            new Color(0.96470588f, 0.31372549f, 0.2823529f, 1f)
    );

    private static final Background PINK_BACKGROUND = new Background(
            new Color(0.94901960f, 0.65490196f, 0.9058823f, 1.f),
            new Color(0.29803921f, 0.89411764f, 0.9568627f, 1f)
    );

    public MainMenu(Context context, MenuStack menuStack) {
        super(context, menuStack);

        AssetManager assetManager = context.getAssetManager();
        BitmapFont font = assetManager.get("ui/fonts/ReemKufi.ttf", BitmapFont.class);

        Image caption = new Image((Texture) assetManager.get("ui/menu/caption.png"));
        float captionWidth = caption.getWidth();
        caption.setWidth((float) Gdx.graphics.getWidth() / 1.5f);
        caption.setHeight(caption.getWidth() / captionWidth * caption.getHeight());
        caption.setPosition(-caption.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - caption.getHeight());

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Play button
        TextButton playButton = new TextButton("Play", textButtonStyle);
        playButton.setPosition(-playButton.getWidth() / 2, -playButton.getHeight() / 2);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuStack.push(new PacksMenu(context, menuStack));
            }
        });

        Background[] backgrounds = {GREEN_BACKGROUND, ORANGE_BACKGROUND, PINK_BACKGROUND};
        Random random = new Random();
        int index = Math.abs(random.nextInt() % backgrounds.length);
        addActor(backgrounds[index]);
        addActor(caption);
        addActor(playButton);
    }
}
