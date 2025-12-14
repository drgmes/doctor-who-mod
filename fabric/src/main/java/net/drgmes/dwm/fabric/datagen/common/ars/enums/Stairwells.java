package net.drgmes.dwm.fabric.datagen.common.ars.enums;

import net.drgmes.dwm.fabric.datagen.common.ars.IEStructures;

public enum Stairwells implements IEStructures {
    DOWN("down"),
    UP("up");

    private final String path;

    Stairwells(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
