package com.majakkagames.gravitymaze.core.events;

import com.majakkagames.gravitymaze.core.game.GameObject;

public class GameObjectEvent implements Event {

    private final GameObject gameObject;

    public GameObjectEvent(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
