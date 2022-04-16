package net.drgmes.dwm;

import net.drgmes.dwm.setup.ModWorldGen;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.Bindings;
import net.minecraftforge.fml.common.Mod;

@Mod(DWM.MODID)
public class DWM {
    public static final String MODID = "dwm";

    public static class LOCS {
        public static final ResourceLocation TARDIS = new ResourceLocation(DWM.MODID, "tardis");
    }

    public static class TEXTURES {
        public static final ResourceLocation GUI_TEST = new ResourceLocation(DWM.MODID, "textures/gui/test.png");
        public static final int[] GUI_TEST_SIZE = new int[]{224, 176};
    }

    public DWM() {
        Registration.init();

        final IEventBus forgeEventBus = Bindings.getForgeBus().get();
        forgeEventBus.addListener(ModWorldGen::biomeLoading);
    }
}
