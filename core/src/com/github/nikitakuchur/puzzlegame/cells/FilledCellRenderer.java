package com.github.nikitakuchur.puzzlegame.cells;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.nikitakuchur.puzzlegame.actors.GameMap;

public class FilledCellRenderer extends CellRenderer {

    public FilledCellRenderer(ShapeRenderer shapeRenderer, GameMap map) {
        super(shapeRenderer, map);
    }

    @Override
    public void draw() {
        float x = getX() * getWidth();
        float y = getY() * getHeight();

        getShapeRenderer().rect(x + getRadius(), y + getRadius(), getWidth() - 2 * getRadius(), getHeight() - 2 * getRadius());
        getShapeRenderer().rect(x, y + getRadius(), getRadius(), getHeight() - 2 * getRadius());
        getShapeRenderer().rect(x + getRadius(), y + getHeight() - getRadius(), getWidth() - 2 * getRadius(), getRadius());
        getShapeRenderer().rect(x + getWidth() - getRadius(), y + getRadius(), getRadius(), getHeight() - 2 * getRadius());
        getShapeRenderer().rect(x + getRadius(), y, getWidth() - 2 * getRadius(), getRadius());

        if (isCornerRound(Corner.BOTTOM_LEFT)) {
            getShapeRenderer().arc(x + getRadius(), y + getRadius(), getRadius(), -180, 90, SEGMENTS);
        } else {
            getShapeRenderer().rect(x, y, getRadius(), getRadius());
        }

        if (isCornerRound(Corner.BOTTOM_RIGHT)) {
            getShapeRenderer().arc(x + getWidth() - getRadius(), y + getRadius(), getRadius(), -90, 90, SEGMENTS);
        } else {
            getShapeRenderer().rect(x + getWidth() - getRadius(), y, getRadius(), getRadius());
        }

        if (isCornerRound(Corner.TOP_RIGHT)) {
            getShapeRenderer().arc(x + getWidth() - getRadius(), y + getHeight() - getRadius(), getRadius(), 0, 90, SEGMENTS);
        } else {
            getShapeRenderer().rect(x + getWidth() - getRadius(), y + getHeight() - getRadius(), getRadius(), getRadius());
        }

        if (isCornerRound(Corner.TOP_LEFT)) {
            getShapeRenderer().arc(x + getRadius(), y + getHeight() - getRadius(), getRadius(), 90, 90, SEGMENTS);
        } else {
            getShapeRenderer().rect(x, y + getHeight() - getRadius(), getRadius(), getRadius());
        }
    }

    private boolean isCornerRound(Corner corner) {
        switch (corner) {
            case BOTTOM_LEFT: return isCellEmpty(-1, 0) && isCellEmpty(-1, -1) && isCellEmpty(0, -1);
            case BOTTOM_RIGHT: return isCellEmpty(0, -1) && isCellEmpty(1, -1) && isCellEmpty(1, 0);
            case TOP_RIGHT: return isCellEmpty(1, 0) && isCellEmpty(1, 1) && isCellEmpty(0, 1);
            case TOP_LEFT: return isCellEmpty(0, 1) && isCellEmpty(-1, 1) && isCellEmpty(-1, 0);
            default: return false;
        }
    }
}
