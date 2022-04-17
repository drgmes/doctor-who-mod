package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.caps.ITardisLevelData.TardisLevelProvider;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.Bindings;

public class ModCapabilities {
    public static final ResourceLocation TARDIS_LOC = new ResourceLocation(DWM.MODID, "tardis_data");
    public static final Capability<ITardisLevelData> TARDIS_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    public static void init() {
        Bindings.getForgeBus().get().addGenericListener(Level.class, ModCapabilities::attachWorldCaps);
    }

    public static void attachWorldCaps(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject().dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) {
            event.addCapability(TARDIS_LOC, new TardisLevelProvider(event.getObject()));
        }
    }
}
