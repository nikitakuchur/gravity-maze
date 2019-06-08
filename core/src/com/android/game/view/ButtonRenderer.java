package com.android.game.view;

import com.android.game.model.Button;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class ButtonRenderer implements Renderer {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private Button button;
    private BitmapFont font;


    public ButtonRenderer(Button button, BitmapFont font) {
        this.button = button;
        this.font = font;

        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    @Override
    public void draw(Matrix4 projectionMatrix) {
        Vector2 position = button.getPosition();
        Vector2 size = button.getSize();

        // Draw a rectangle
        shapeRenderer.setProjectionMatrix(projectionMatrix);

        if (button.isPressed())
            shapeRenderer.setColor(button.getPressedColor());
        else
            shapeRenderer.setColor(button.getColor());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(position.x, position.y, size.x, size.y);
        shapeRenderer.end();

        // Draw the text
        spriteBatch.setProjectionMatrix(projectionMatrix);
        spriteBatch.begin();

        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, button.getText());
        font.draw(spriteBatch, glyphLayout, position.x + (size.x - glyphLayout.width) / 2,
                                            position.y + (glyphLayout.height + size.y) / 2);

        spriteBatch.end();

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }
}
