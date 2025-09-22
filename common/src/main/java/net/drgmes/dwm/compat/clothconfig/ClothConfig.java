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

@Config(name = DWM.MODID)
public class ClothConfig implements ConfigData {
    public static ConfigHolder<ClothConfig> INSTANCE;

    @ConfigEntry.Gui.Tooltip
    public boolean hideTheEndConditionally = ModConfig.COMMON.hideTheEndConditionally.get();

    @ConfigEntry.Gui.Tooltip
    public boolean tardisRecallOperatorOnly = ModConfig.COMMON.tardisRecallOperatorOnly.get();

    public static void setup() {
        INSTANCE = AutoConfig.register(ClothConfig.class, GsonConfigSerializer::new);

        INSTANCE.registerSaveListener((configHolder, config) -> {
            config.onChanged();
            return ActionResult.SUCCESS;
        });
    }

    public void onChanged() {
        ModConfig.COMMON.hideTheEndConditionally.set(this.hideTheEndConditionally);
        ModConfig.COMMON.tardisRecallOperatorOnly.set(this.tardisRecallOperatorOnly);
    }
}
