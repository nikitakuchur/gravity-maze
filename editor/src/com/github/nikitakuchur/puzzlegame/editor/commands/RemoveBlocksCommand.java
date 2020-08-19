package com.github.nikitakuchur.puzzlegame.editor.commands;

import java.util.ArrayList;
import java.util.List;

import com.github.nikitakuchur.puzzlegame.game.cells.CellType;
import com.github.nikitakuchur.puzzlegame.game.entities.GameMap;

public class RemoveBlocksCommand implements Command {

    private final GameMap map;
    private final List<Block> blocks = new ArrayList<>();

    public RemoveBlocksCommand(GameMap map) {
        this.map = map;
    }

    public void addBlock(int x, int y) {
        blocks.add(new Block(x, y));
    }

    @Override
    public void execute() {
        blocks.forEach(block -> map.setCellType(block.x, block.y, CellType.EMPTY));
    }

    @Override
    public void unexecute() {
        blocks.forEach(block -> map.setCellType(block.x, block.y, CellType.FILLED));
    }

    private static class Block {
        private final int x;
        private final int y;

        Block(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
