package net.drgmes.dwm.network;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class TardisExteriorRemoteCallablePackets {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    // /////////// //
    // Clientbound //
    // /////////// //

    public static void updateTardisExteriorData(BlockPos blockPos, boolean isDoorsOpened, boolean demat) {
        BlockState blockState = mc.world.getBlockState(blockPos);

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (demat) tardisExteriorBlockEntity.demat();
        }

        if (blockState.getBlock() instanceof BaseTardisExteriorBlock && blockState.get(BaseTardisExteriorBlock.OPEN) != isDoorsOpened) {
            mc.world.setBlockState(blockPos, blockState.with(BaseTardisExteriorBlock.OPEN, isDoorsOpened), 10);
        }
    }
}