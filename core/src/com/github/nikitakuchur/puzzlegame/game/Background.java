package com.github.nikitakuchur.puzzlegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Set;

public class Background extends Actor {

    public static final Background BLUE = new Background(Color.valueOf("#4993bf"),
                                                         Color.valueOf("#937fc1"),
                                                         Color.valueOf("#5193bf"),
                                                         Color.valueOf("#07a8ba"));

    private static HashMap<String, Background> hashMap = new HashMap<>();

    static {
        hashMap.put("blue", Background.BLUE);
    }

    private Color[] colors;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Background(Color bl, Color br, Color tr, Color tl){
        colors = new Color[]{bl, br, tr, tl};
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(-(float) Gdx.graphics.getWidth() / 2, -(float) Gdx.graphics.getHeight() / 2,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                colors[0], colors[1], colors[2], colors[3]);
        shapeRenderer.end();
        batch.begin();
    }

    public static Background getBackground(String name) {
        return hashMap.get(name);
    }

    public static Set<String> getBackgroundNames() {
        return hashMap.keySet();
    }
}
