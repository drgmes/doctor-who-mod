package net.drgmes.dwm.blocks.tardis.misc.tardisroundel;

import net.drgmes.dwm.network.client.TardisRoundelBlockTemplateClearPacket;
import net.drgmes.dwm.network.client.TardisRoundelBlockTemplateUpdatePacket;
import net.drgmes.dwm.network.client.TardisRoundelUpdatePacket;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TardisRoundelBlockEntity extends BlockEntity {
    public boolean uncovered;
    public boolean lightMode;
    public Identifier blockTemplate;

    public TardisRoundelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public TardisRoundelBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ModBlockEntities.TARDIS_ROUNDEL.getBlockEntityType(), blockPos, blockState);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(tag, registryLookup);

        this.uncovered = tag.getBoolean("uncovered");
        this.lightMode = tag.getBoolean("lightMode");

        if (tag.contains("blockTemplate")) this.blockTemplate = Identifier.of(tag.getString("blockTemplate"));
        else this.blockTemplate = null;
    }

    @Override
    protected void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(tag, registryLookup);

        tag.putBoolean("uncovered", this.uncovered);
        tag.putBoolean("lightMode", this.lightMode);

        if (this.blockTemplate != null) tag.putString("blockTemplate", this.blockTemplate.toString());
        else tag.remove("blockTemplate");
    }

    public void sendUpdatePacket() {
        if (!(this.world instanceof ServerWorld serverWorld)) return;

        new TardisRoundelUpdatePacket(this.getPos(), this.uncovered, this.lightMode)
            .sendToAll(serverWorld.getServer());

        if (this.blockTemplate != null) {
            new TardisRoundelBlockTemplateUpdatePacket(this.getPos(), this.blockTemplate.toString())
                .sendToAll(serverWorld.getServer());
        }
        else {
            new TardisRoundelBlockTemplateClearPacket(this.getPos())
                .sendToAll(serverWorld.getServer());
        }
    }
}
