package net.drgmes.dwm.world.data;

import net.drgmes.dwm.setup.ModDimensions;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class TardisWorldData extends SavedData {
    public static TardisWorldData load(CompoundTag tag) {
        TardisWorldData data = new TardisWorldData();

        ListTag list = tag.getList("dimensions", Tag.TAG_STRING);
        if (list == null) return null;

        ModDimensions.TARDISES.clear();
        for (Tag base : list) {
            ModDimensions.TARDISES.add(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(((StringTag) base).getAsString())));
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (ResourceKey<Level> loc : ModDimensions.TARDISES) {
            list.add(StringTag.valueOf(loc.location().toString()));
        }
        tag.put("dimensions", list);

        return tag;
    }
}
