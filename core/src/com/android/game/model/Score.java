package com.android.game.model;

public class Score {
    int value;

    public Score() {
        value = 0;
    }

    /**
     * Increments the value
     */
    public void add() {
        value++;
    }

    /**
     * Sets the value
     *
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
