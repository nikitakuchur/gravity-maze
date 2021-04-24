package com.majakkagames.gravitymaze.core.events;

public interface EventHandler<T extends Event> {
    void handle(T event);
}
