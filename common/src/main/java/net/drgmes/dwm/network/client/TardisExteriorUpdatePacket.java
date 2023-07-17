package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisExteriorUpdatePacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final boolean isDoorsOpened;
    private final boolean isLightEnabled;
    private final boolean needDemat;

    public TardisExteriorUpdatePacket(BlockPos blockPos, boolean isDoorsOpened, boolean isLightEnabled, boolean needDemat) {
        this.blockPos = blockPos;
        this.isDoorsOpened = isDoorsOpened;
        this.isLightEnabled = isLightEnabled;
        this.needDemat = needDemat;
    }

    public static TardisExteriorUpdatePacket create(PacketByteBuf buf) {
        return new TardisExteriorUpdatePacket(buf.readBlockPos(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_EXTERIOR_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeBoolean(this.isDoorsOpened);
        buf.writeBoolean(this.isLightEnabled);
        buf.writeBoolean(this.needDemat);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();
        BlockState blockState = mc.world.getBlockState(this.blockPos);

        if (mc.world.getBlockEntity(this.blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (this.needDemat) tardisExteriorBlockEntity.demat();
        }

        if (blockState.getBlock() instanceof BaseTardisExteriorBlock<?>) {
            boolean flag = false;

            if (blockState.get(BaseTardisExteriorBlock.OPEN) != this.isDoorsOpened) {
                blockState = blockState.with(BaseTardisExteriorBlock.OPEN, this.isDoorsOpened);
                flag = true;
            }

            if (blockState.get(BaseTardisExteriorBlock.LIT) != this.isLightEnabled) {
                blockState = blockState.with(BaseTardisExteriorBlock.LIT, this.isLightEnabled);
                flag = true;
            }

            if (flag) {
                mc.world.setBlockState(this.blockPos, blockState, 10);
            }
        }
    }
}
