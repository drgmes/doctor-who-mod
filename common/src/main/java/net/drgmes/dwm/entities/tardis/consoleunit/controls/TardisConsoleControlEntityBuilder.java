package net.drgmes.dwm.entities.tardis.consoleunit.controls;

import net.drgmes.dwm.utils.builders.EntityBuilder;
import net.minecraft.entity.SpawnGroup;

public class TardisConsoleControlEntityBuilder extends EntityBuilder<TardisConsoleControlEntity> {
    public TardisConsoleControlEntityBuilder(String name, float width, float height) {
        super(name, TardisConsoleControlEntity::new, SpawnGroup.MISC, width, height);
    }

    public TardisConsoleControlEntityBuilder(String name, float size) {
        this(name, size, size);
    }

    public TardisConsoleControlEntityBuilder(String name) {
        this(name, 0.1F);
    }
}
