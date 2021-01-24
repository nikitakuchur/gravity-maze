package com.github.nikitakuchur.puzzlegame.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.nikitakuchur.puzzlegame.ui.Menu;
import com.github.nikitakuchur.puzzlegame.ui.MenuStack;
import com.github.nikitakuchur.puzzlegame.utils.Context;

public class MainMenu extends Menu {
    public MainMenu(Context context, MenuStack menuStack) {
        super(context, menuStack);

        AssetManager assetManager = context.getAssetManager();
        BitmapFont font = assetManager.get("ui/fonts/ReemKufi.ttf", BitmapFont.class);

        Image caption = new Image((Texture) assetManager.get("ui/menu/caption.png"));
        float captionWidth = caption.getWidth();
        caption.setWidth((float) Gdx.graphics.getWidth() / 1.3f);
        caption.setHeight(caption.getWidth() / captionWidth * caption.getHeight());
        caption.setPosition(-caption.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2 - caption.getHeight());

        // Button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Start button
        TextButton playButton = new TextButton("Play", textButtonStyle);
        playButton.setPosition(-playButton.getWidth() / 2, -playButton.getHeight() / 2);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuStack.push(new PacksMenu(context, menuStack));
            }
        });

        addActor(caption);
        addActor(playButton);
    }
}
