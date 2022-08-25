package net.drgmes.dwm.network;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ArsRemoteCallablePackets {
    // /////////// //
    // Serverbound //
    // /////////// //

    public static void applyArsCreator(ServerPlayerEntity player, BlockPos blockPos, String arsCategoryName, String arsStructureName) {
        ServerWorld serverWorld = (ServerWorld) player.world;
        if (!TardisHelper.isTardisDimension(serverWorld)) return;

        TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
            if (tardis.isBroken()) {
                player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                return;
            }

            boolean isArsStructureGenerated = getArsStructure(arsCategoryName, arsStructureName).place(player, (ServerWorld) player.world, blockPos);
            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.ars_interface.generated." + (isArsStructureGenerated ? "success" : "failed")), true);
        });
    }

    public static void applyArsDestroyer(ServerPlayerEntity player, BlockPos blockPos, String arsCategoryName, String arsStructureName) {
        ServerWorld serverWorld = (ServerWorld) player.world;
        if (!TardisHelper.isTardisDimension(serverWorld)) return;

        TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
            if (tardis.isBroken()) {
                player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                return;
            }

            boolean isArsStructureDestroyed = getArsStructure(arsCategoryName, arsStructureName).destroy(player, (ServerWorld) player.world, blockPos);
            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.ars_interface.destroy." + (isArsStructureDestroyed ? "success" : "failed")), true);
        });
    }

    // ///// //
    // Utils //
    // ///// //

    private static ArsStructure getArsStructure(String arsCategoryName, String arsStructureName) {
        return ArsStructures.STRUCTURES.get(ArsCategories.CATEGORIES.get(arsCategoryName)).get(arsStructureName);
    }
}
