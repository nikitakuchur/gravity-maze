package com.android.game.gameobjects;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public class GameObject extends Actor {

    private List<Ball> balls = new ArrayList<>();

    /**
     * Adds a ball that can interact with this hole
     *
     * @param ball the ball
     */
    public void addBall(Ball ball) {
        balls.add(ball);
    }

    /**
     * @return the list of the balls that can interact with the object
     */
    public List<Ball> getBalls() {
        return balls;
    }

    /**
     * @param ball the ball
     * @return true if the ball can interact with this object and false otherwise
     */
    public boolean isInteracting(Ball ball) {
        return true;
    }
}
