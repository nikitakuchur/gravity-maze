package com.github.nikitakuchur.puzzlegame.ui.commands;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.Stack;

public class SetActorCommand implements UnexecutableCommand {
    private Stack<Actor> uiStack;
    private Stage stage;
    private Actor newActor;

    public SetActorCommand(Stage stage) {
        this.uiStack = new Stack<>();
        this.stage = stage;
        this.newActor = null;
    }

    public void add(Actor actor) {
        this.newActor = actor;
    }

    @Override
    public void execute() {
        if (newActor == null)
            return;

        if (!uiStack.isEmpty()) {
            uiStack.peek().remove();
        }

        uiStack.push(newActor);
        stage.addActor(uiStack.peek());

        newActor = null;
    }

    @Override
    public void unexecute() {
        if (!uiStack.isEmpty()) {
            uiStack.peek().remove();
        }

        uiStack.pop();

        if (!uiStack.isEmpty()) {
            stage.addActor(uiStack.peek());
        }
    }
}
