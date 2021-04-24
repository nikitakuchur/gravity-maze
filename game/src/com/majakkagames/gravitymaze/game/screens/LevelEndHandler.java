package com.majakkagames.gravitymaze.game.screens;

import com.majakkagames.gravitymaze.core.events.EventHandler;
import com.majakkagames.gravitymaze.core.game.Context;
import com.majakkagames.gravitymaze.core.game.GameObjectStore;
import com.majakkagames.gravitymaze.core.ui.MenuStack;
import com.majakkagames.gravitymaze.game.gameobjects.LevelController;
import com.majakkagames.gravitymaze.game.gameobjects.LevelController.LevelEvent.Type;
import com.majakkagames.gravitymaze.game.gameobjects.LevelProperties;
import com.majakkagames.gravitymaze.game.ui.level.LevelResult;

public class LevelEndHandler implements EventHandler<LevelController.LevelEvent> {

    private final Context context;
    private final MenuStack menuStack;

    public LevelEndHandler(Context context, MenuStack menuStack) {
        this.context = context;
        this.menuStack = menuStack;
    }

    @Override
    public void handle(LevelController.LevelEvent event) {
        GameObjectStore store = event.getLevel().getGameObjectStore();
        LevelProperties properties = store.getAnyGameObject(LevelProperties.class);
        int stars = 1;
        if (event.getType() == Type.FAILED) {
            stars = 0;
        } else if (event.getType() == Type.PASSED) {
            if (properties.getMaxMoves() >= properties.getMoves()) {
                stars = 3;
            } else if (properties.getMaxMoves() * 1.3f >= properties.getMoves()) {
                stars = 2;
            }
        }
        LevelResult resultMenu = new LevelResult(context, menuStack, stars);
        menuStack.push(resultMenu);
    }
}
