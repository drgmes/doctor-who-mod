package net.drgmes.dwm.world.data;

import net.drgmes.dwm.setup.ModDimensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

public class TardisLevelData extends SavedData {
    public static TardisLevelData load(CompoundTag tag) {
        TardisLevelData data = new TardisLevelData();

        ListTag list = tag.getList("dimensions", Tag.TAG_STRING);
        if (list == null) return null;

        ModDimensions.TARDISES.clear();
        for (Tag base : list) ModDimensions.TARDISES.add(base.getAsString());

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();

        for (String id : ModDimensions.TARDISES) list.add(StringTag.valueOf(id));
        tag.put("dimensions", list);

        return tag;
    }
}
