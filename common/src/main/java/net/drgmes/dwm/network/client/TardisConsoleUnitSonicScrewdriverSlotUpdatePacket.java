package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitSonicScrewdriverSlotUpdatePacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final ItemStack itemStack;

    public TardisConsoleUnitSonicScrewdriverSlotUpdatePacket(BlockPos blockPos, ItemStack itemStack) {
        this.blockPos = blockPos;
        this.itemStack = itemStack;
    }

    public static TardisConsoleUnitSonicScrewdriverSlotUpdatePacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitSonicScrewdriverSlotUpdatePacket(buf.readBlockPos(), buf.readItemStack());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_SONIC_SCREWDRIVER_SLOT_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeItemStack(this.itemStack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            tardisConsoleUnitBlockEntity.sonicScrewdriverItemStack = this.itemStack;
        }
    }
}
