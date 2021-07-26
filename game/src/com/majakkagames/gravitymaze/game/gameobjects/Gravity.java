package com.majakkagames.gravitymaze.game.gameobjects;

import com.majakkagames.mazecore.game.GameObject;
import com.majakkagames.mazecore.game.serialization.annotations.Transient;
import com.majakkagames.gravitymaze.game.utils.Direction;

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
