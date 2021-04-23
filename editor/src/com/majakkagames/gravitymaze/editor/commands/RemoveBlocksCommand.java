package com.majakkagames.gravitymaze.editor.commands;

import java.util.ArrayList;
import java.util.List;

import com.majakkagames.gravitymaze.game.gameobjects.Maze;
import com.majakkagames.gravitymaze.game.cells.CellType;

public class RemoveBlocksCommand implements Command {

    private final Maze map;
    private final List<Block> blocks = new ArrayList<>();

    public RemoveBlocksCommand(Maze map) {
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
