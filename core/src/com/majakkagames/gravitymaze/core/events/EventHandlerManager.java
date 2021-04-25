package com.majakkagames.gravitymaze.core.events;

import java.util.*;

public class EventHandlerManager<T extends Enum<?>, E extends Event> {

    private final Map<T, List<EventHandler<E>>> eventHandlers = new HashMap<>();

    public void add(T type, EventHandler<E> handler) {
        eventHandlers.computeIfAbsent(type, k -> new ArrayList<>()).add(handler);
    }

    public void fire(T type, E event) {
        Optional.ofNullable(eventHandlers.get(type))
                .ifPresent(handlers -> handlers.forEach(handler -> handler.handle(event)));
    }
}
