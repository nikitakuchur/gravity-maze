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

    public static Button createBackButton(AssetManager assetManager, MenuStack menuStack) {
        Drawable backDrawable = new TextureRegionDrawable(new TextureRegion((Texture) assetManager.get("ui/menu/back.png")));
        ImageButton backButton = new ImageButton(backDrawable);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuStack.pop();
            }
        });
        backButton.setSize((float) Gdx.graphics.getWidth() / 12, (float) Gdx.graphics.getWidth() / 12);
        backButton.setPosition(
                -(float) Gdx.graphics.getWidth() / 2 + backButton.getWidth() / 3,
                (float) Gdx.graphics.getHeight() / 2 - 1.4f * backButton.getHeight()
        );
        return backButton;
    }
}
