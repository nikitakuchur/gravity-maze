package com.majakkagames.gravitymaze.core.ui;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class MenuStack extends Group {
    private final Deque<Menu> stack;

    public MenuStack() {
        stack = new ArrayDeque<>();
    }

    public void push(Menu menu) {
        stack.push(menu);
        clearChildren();
        addActor(menu);
    }

    public void pop() {
        if (!stack.isEmpty()) {
            stack.pop().dispose();
            clearChildren();
            Optional.ofNullable(stack.peek()).ifPresent(this::addActor);
        }
    }

    public Menu peek() {
        return stack.peek();
    }

    public int size() {
        return stack.size();
    }
}
