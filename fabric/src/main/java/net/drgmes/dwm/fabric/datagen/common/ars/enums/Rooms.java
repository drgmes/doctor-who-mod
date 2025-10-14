package net.drgmes.dwm.fabric.datagen.common.ars.enums;

import net.drgmes.dwm.fabric.datagen.common.ars.IEStructures;

public enum Rooms implements IEStructures {
    LOW_NARROW("low/narrow"),
    LOW_SMALL("low/small"),
    LOW_MEDIUM("low/medium"),
    LOW_LARGE("low/large"),
    LOW_X_LARGE("low/x_large"),
    LOW_HUGE("low/huge"),
    TALL_NARROW("tall/narrow"),
    TALL_SMALL("tall/small"),
    TALL_MEDIUM("tall/medium"),
    TALL_LARGE("tall/large"),
    TALL_X_LARGE("tall/x_large"),
    TALL_HUGE("tall/huge");

    private final String path;

    Rooms(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
