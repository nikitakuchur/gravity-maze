package com.majakkagames.gravitymaze.game.screens;

import com.majakkagames.gravitymaze.core.events.EventHandler;
import com.majakkagames.gravitymaze.core.game.Context;
import com.majakkagames.gravitymaze.core.ui.MenuStack;
import com.majakkagames.gravitymaze.game.gameobjects.LevelController;
import com.majakkagames.gravitymaze.game.ui.level.LevelResult;

public class LevelFailedHandler implements EventHandler<LevelController.LevelEvent> {

    private final Context context;
    private final MenuStack menuStack;

    public LevelFailedHandler(Context context, MenuStack menuStack) {
        this.context = context;
        this.menuStack = menuStack;
    }

    @Override
    public void handle(LevelController.LevelEvent event) {
        LevelResult resultMenu = new LevelResult(context, menuStack, 0);
        menuStack.push(resultMenu);
    }
}
