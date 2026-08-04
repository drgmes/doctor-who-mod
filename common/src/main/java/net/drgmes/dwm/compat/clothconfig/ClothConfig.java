package net.drgmes.dwm.compat.clothconfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModConfig;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;
import java.util.List;

@Config(name = DWM.MODID)
public class ClothConfig implements ConfigData {
    public static ConfigHolder<ClothConfig> INSTANCE;

    @ConfigEntry.Gui.Tooltip
    public List<String> dimensionsBlacklist = new ArrayList<>(ModConfig.COMMON.dimensionsBlacklist.get());

    @ConfigEntry.Gui.Tooltip
    public boolean hideTheEndConditionally = ModConfig.COMMON.hideTheEndConditionally.get();

    @ConfigEntry.Gui.Tooltip
    public boolean tardisRecallOperatorOnly = ModConfig.COMMON.tardisRecallOperatorOnly.get();

    @ConfigEntry.Gui.Tooltip
    public boolean dimensionsAlwaysAvailable = ModConfig.COMMON.dimensionsAlwaysAvailable.get();

    @ConfigEntry.Gui.Tooltip
    public boolean biomesAlwaysAvailable = ModConfig.COMMON.biomesAlwaysAvailable.get();

    @ConfigEntry.Gui.Tooltip
    public boolean structuresAlwaysAvailable = ModConfig.COMMON.structuresAlwaysAvailable.get();

    @ConfigEntry.Gui.Tooltip
    public boolean filterStructuresByVisitedBiomes = ModConfig.COMMON.filterStructuresByVisitedBiomes.get();

    public static void setup() {
        INSTANCE = AutoConfig.register(ClothConfig.class, GsonConfigSerializer::new);

        INSTANCE.registerSaveListener((configHolder, config) -> {
            config.onChanged();
            return ActionResult.SUCCESS;
        });
    }

    public void onChanged() {
        ModConfig.COMMON.dimensionsBlacklist.set(this.dimensionsBlacklist);
        ModConfig.COMMON.hideTheEndConditionally.set(this.hideTheEndConditionally);
        ModConfig.COMMON.tardisRecallOperatorOnly.set(this.tardisRecallOperatorOnly);
        ModConfig.COMMON.dimensionsAlwaysAvailable.set(this.dimensionsAlwaysAvailable);
        ModConfig.COMMON.biomesAlwaysAvailable.set(this.biomesAlwaysAvailable);
        ModConfig.COMMON.structuresAlwaysAvailable.set(this.structuresAlwaysAvailable);
        ModConfig.COMMON.filterStructuresByVisitedBiomes.set(this.filterStructuresByVisitedBiomes);
    }
}
