package net.drgmes.dwm.fabric.datagen.common.ars.enums;

import net.drgmes.dwm.fabric.datagen.common.ars.IEStructures;

public enum Hallways implements IEStructures {
    SHORT("short"),
    MEDIUM("medium"),
    LONG("long"),
    JUNCTION_X("junction_x"),
    JUNCTION_T("junction_t"),
    JUNCTION_T_LEFT("junction_t_left"),
    JUNCTION_T_RIGHT("junction_t_right"),
    TURN_LEFT_SHORT("turn_left_short"),
    TURN_RIGHT_SHORT("turn_right_short"),
    TURN_LEFT_LONG("turn_left_long"),
    TURN_RIGHT_LONG("turn_right_long");

    private final String path;

    Hallways(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
