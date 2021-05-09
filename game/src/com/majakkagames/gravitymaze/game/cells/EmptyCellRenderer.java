package com.majakkagames.gravitymaze.game.cells;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.majakkagames.gravitymaze.game.gameobjects.Maze;

public class EmptyCellRenderer extends CellRenderer {

    public EmptyCellRenderer(ShapeRenderer shapeRenderer, Maze map) {
        super(shapeRenderer, map);
    }

    @Override
    public void draw() {
        float x = getX() * getWidth() + getMap().getX();
        float y = getY() * getHeight() + getMap().getY();

        if (isCornerRound(Corner.BOTTOM_LEFT)) {
            roundedInsideCorner(x, y, Corner.BOTTOM_LEFT);
        }
        if (isCornerRound(Corner.BOTTOM_RIGHT)) {
            roundedInsideCorner(x + getWidth(), y, Corner.BOTTOM_RIGHT);
        }
        if (isCornerRound(Corner.TOP_RIGHT)) {
            roundedInsideCorner(x + getWidth(), y + getHeight(), Corner.TOP_RIGHT);
        }
        if (isCornerRound(Corner.TOP_LEFT)) {
            roundedInsideCorner(x, y + getHeight(), Corner.TOP_LEFT);
        }
    }

    private void roundedInsideCorner(float x, float y, Corner corner) {
        float[] vertices = new float[(SEGMENTS + 1) * 2];
        float t = corner.ordinal() * (float) Math.PI / 2;

        for (int i = 0; i <= SEGMENTS * 2; i += 2, t += Math.PI / (2 * SEGMENTS)) {
            vertices[i] = x - getRadius() * (float) Math.cos(t);
            vertices[i + 1] = y - getRadius() * (float) Math.sin(t);

            if (corner == Corner.BOTTOM_LEFT || corner == Corner.TOP_LEFT) {
                vertices[i] += getRadius();
            } else {
                vertices[i] -= getRadius();
            }

            if (corner == Corner.BOTTOM_LEFT || corner == Corner.BOTTOM_RIGHT) {
                vertices[i + 1] += getRadius();
            } else {
                vertices[i + 1] -= getRadius();
            }
        }

        // Draw triangles
        for (int i = 0; i < vertices.length - 2; i += 2) {
            getShapeRenderer().triangle(vertices[i], vertices[i + 1], vertices[i + 2], vertices[i + 3], x, y);
        }
    }

    private boolean isCornerRound(Corner corner) {
        switch (corner) {
            case BOTTOM_LEFT: return !isCellEmpty(-1, 0) && !isCellEmpty(0, -1);
            case BOTTOM_RIGHT: return !isCellEmpty(0, -1) && !isCellEmpty(1, 0);
            case TOP_RIGHT: return !isCellEmpty(1, 0) && !isCellEmpty(0, 1);
            case TOP_LEFT: return !isCellEmpty(0, 1) && !isCellEmpty(-1, 0);
            default: return false;
        }
    }
}
