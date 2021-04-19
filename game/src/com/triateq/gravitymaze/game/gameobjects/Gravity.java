package com.triateq.gravitymaze.game.gameobjects;

import com.triateq.gravitymaze.core.game.GameObject;
import com.triateq.gravitymaze.core.serialization.annotations.Transient;
import com.triateq.gravitymaze.game.utils.Direction;

@Transient
public class Gravity extends GameObject {

    private Direction gravityDirection = Direction.BOTTOM;

    public void setGravityDirection(Direction gravityDirection) {
        this.gravityDirection = gravityDirection;
    }

    public Direction getGravityDirection() {
        return gravityDirection;
    }
}
