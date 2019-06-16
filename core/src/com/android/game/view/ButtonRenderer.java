package com.android.game.view;

import com.android.game.model.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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

    /**
     * Creates a renderer for the button
     *
     * @param button the button
     * @param font the font
     */
    public ButtonRenderer(Button button, BitmapFont font) {
        this.button = button;
        this.font = font;

        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
    }

    /**
     * Sets the button
     *
     * @param button the button
     */
    public void setButton(Button button) {
        this.button = button;
    }

    /**
     * @return the button
     */
    public Button getButton() {
        return button;
    }

    /**
     * Sets the font
     *
     * @param font the font
     */
    public void setFont(BitmapFont font) {
        this.font = font;
    }

    /**
     * @return the font
     */
    public BitmapFont getFont() {
        return font;
    }

    @Override
    public void draw(Matrix4 projectionMatrix) {
        Vector2 position = button.getPosition();
        Vector2 size = button.getSize();

        // Enable alpha channel
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

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

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }
}
