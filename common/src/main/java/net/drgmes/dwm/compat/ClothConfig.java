package net.drgmes.dwm.compat;

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

    @ConfigEntry.Gui.Tooltip
    public String tardisFlightDistanceRate = String.valueOf(ModConfig.COMMON.tardisFlightDistanceRate.get());

    @ConfigEntry.Gui.Tooltip
    public String tardisMaxFlightTime = String.valueOf(ModConfig.COMMON.tardisMaxFlightTime.get());

    @ConfigEntry.Gui.Tooltip
    public String tardisFuelRefillTiming = String.valueOf(ModConfig.COMMON.tardisFuelRefillTiming.get());

    @ConfigEntry.Gui.Tooltip
    public String tardisFuelConsumeTiming = String.valueOf(ModConfig.COMMON.tardisFuelConsumeTiming.get());

    @ConfigEntry.Gui.Tooltip
    public String tardisFuelToEnergyRating = String.valueOf(ModConfig.COMMON.tardisFuelToEnergyRating.get());

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

        String tardisFlightDistanceRate = this.tardisFlightDistanceRate.replaceAll("[^0-9]", "");
        if (!tardisFlightDistanceRate.equals("")) {
            ModConfig.COMMON.tardisFlightDistanceRate.set(Math.max(1, Math.min(Integer.MAX_VALUE, Integer.parseInt(tardisFlightDistanceRate))));
            this.tardisFlightDistanceRate = tardisFlightDistanceRate;
        }
        else {
            ModConfig.COMMON.tardisFlightDistanceRate.set(1);
            this.tardisFlightDistanceRate = "1";
        }

        String tardisMaxFlightTime = this.tardisMaxFlightTime.replaceAll("[^0-9]", "");
        if (!tardisMaxFlightTime.equals("")) {
            ModConfig.COMMON.tardisMaxFlightTime.set(Math.max(1, Math.min(Integer.MAX_VALUE, Integer.parseInt(tardisMaxFlightTime))));
            this.tardisMaxFlightTime = tardisMaxFlightTime;
        }
        else {
            ModConfig.COMMON.tardisMaxFlightTime.set(1);
            this.tardisMaxFlightTime = "1";
        }

        String tardisFuelRefillTiming = this.tardisFuelRefillTiming.replaceAll("[^0-9]", "");
        if (!tardisFuelRefillTiming.equals("")) {
            ModConfig.COMMON.tardisFuelRefillTiming.set(Math.max(1, Math.min(Integer.MAX_VALUE, Integer.parseInt(tardisFuelRefillTiming))));
            this.tardisFuelRefillTiming = tardisFuelRefillTiming;
        }
        else {
            ModConfig.COMMON.tardisFuelRefillTiming.set(1);
            this.tardisFuelRefillTiming = "1";
        }

        String tardisFuelConsumeTiming = this.tardisFuelConsumeTiming.replaceAll("[^0-9]", "");
        if (!tardisFuelConsumeTiming.equals("")) {
            ModConfig.COMMON.tardisFuelConsumeTiming.set(Math.max(1, Math.min(Integer.MAX_VALUE, Integer.parseInt(tardisFuelConsumeTiming))));
            this.tardisFuelConsumeTiming = tardisFuelConsumeTiming;
        }
        else {
            ModConfig.COMMON.tardisFuelConsumeTiming.set(1);
            this.tardisFuelConsumeTiming = "1";
        }

        String tardisFuelToEnergyRating = this.tardisFuelToEnergyRating.replaceAll("[^0-9]", "");
        if (!tardisFuelToEnergyRating.equals("")) {
            ModConfig.COMMON.tardisFuelToEnergyRating.set(Math.max(1, Math.min(Integer.MAX_VALUE, Integer.parseInt(tardisFuelToEnergyRating))));
            this.tardisFuelToEnergyRating = tardisFuelToEnergyRating;
        }
        else {
            ModConfig.COMMON.tardisFuelToEnergyRating.set(1);
            this.tardisFuelToEnergyRating = "1";
        }
    }
}
