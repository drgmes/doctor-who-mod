package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockBuilder;

public class TardisEngineToyotaBlockBuilder extends BaseTardisEngineBlockBuilder {
    public TardisEngineToyotaBlockBuilder(String name) {
        super(name, () -> new TardisEngineToyotaBlock(getBlockSettings()));
    }
}
