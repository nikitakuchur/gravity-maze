package com.triateq.gravitymaze.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.triateq.gravitymaze.ui.MenuStack;

public class MenuUtils {

    private MenuUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Button createBackButton(AssetManager assetManager, MenuStack menuStack) {
        Button backButton = createButton(assetManager.get("ui/menu/back.png"));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuStack.peek().back();
            }
        });
        backButton.setPosition(
                -(float) Gdx.graphics.getWidth() / 2 + backButton.getWidth() / 3,
                (float) Gdx.graphics.getHeight() / 2 - 1.4f * backButton.getHeight()
        );
        return backButton;
    }

    public static Button createButton(Texture texture) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        ImageButton button = new ImageButton(drawable);
        button.setSize((float) Gdx.graphics.getWidth() / 12, (float) Gdx.graphics.getWidth() / 12);
        return button;
    }
 }
