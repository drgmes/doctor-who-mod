package net.drgmes.dwm.fabric.datagen.common.ars;

import net.drgmes.dwm.fabric.datagen.common.ars.enums.Hallways;
import net.drgmes.dwm.fabric.datagen.common.ars.enums.Rooms;

public record ArsEntry(String name, ETypes type) {
    public enum ETypes {
        HALLWAYS("hallways", "hallway", Hallways.values()),
        ROOMS("rooms", "room", Rooms.values());

        public final String path;
        public final String prefix;
        public final IEStructures[] structures;

        ETypes(String path, String prefix, IEStructures[] structures) {
            this.path = path;
            this.prefix = prefix;
            this.structures = structures;
        }
    }

}
