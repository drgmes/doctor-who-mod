package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter;

import net.drgmes.dwm.enums.TardisTeleporterEntityTypes;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class TardisTeleporterBlockEntity extends BlockEntity {
    public static final PacketCodec<PacketByteBuf, List<TardisTeleporterEntityTypes>> ENTITY_TYPES_PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(PacketByteBuf buf, List<TardisTeleporterEntityTypes> payload) {
            buf.writeShort(payload.size());
            payload.forEach(buf::writeEnumConstant);
        }

        @Override
        public List<TardisTeleporterEntityTypes> decode(PacketByteBuf buf) {
            List<TardisTeleporterEntityTypes> list = new ArrayList<>(1);
            int size = buf.readShort();

            for (int i = 0; i < size; i++) list.add(buf.readEnumConstant(TardisTeleporterEntityTypes.class));
            return list;
        }
    };

    public BlockPos destinationBlockPos;
    public Direction destinationFacing;
    public boolean isLocked = true;

    public List<TardisTeleporterEntityTypes> allowedEntityTypes = new ArrayList<>();

    public TardisTeleporterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_TELEPORTER.getBlockEntityType(), blockPos, blockState);

        this.allowedEntityTypes.add(TardisTeleporterEntityTypes.PLAYERS);
        this.allowedEntityTypes.add(TardisTeleporterEntityTypes.MOBS);
        this.allowedEntityTypes.add(TardisTeleporterEntityTypes.ITEMS);
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

        if (tag.contains("destinationBlockPos")) this.destinationBlockPos = BlockPos.fromLong(tag.getLong("destinationBlockPos"));
        if (tag.contains("destinationFacing")) this.destinationFacing = Direction.byId(tag.getInt("destinationFacing"));
        if (tag.contains("isLocked")) this.isLocked = tag.getBoolean("isLocked");

        if (tag.contains("allowedEntityTypes")) {
            this.allowedEntityTypes.clear();

            for (String entityTypeName : tag.getCompound("allowedEntityTypes").getKeys()) {
                this.allowedEntityTypes.add(TardisTeleporterEntityTypes.valueOf(entityTypeName));
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(tag, registryLookup);

        if (this.destinationBlockPos != null) tag.putLong("destinationBlockPos", this.destinationBlockPos.asLong());
        if (this.destinationFacing != null) tag.putInt("destinationFacing", this.destinationFacing.getId());
        tag.putBoolean("isLocked", this.isLocked);

        NbtCompound allowedEntityTypesTag = new NbtCompound();
        this.allowedEntityTypes.forEach((entityType) -> allowedEntityTypesTag.putBoolean(entityType.name(), true));
        tag.put("allowedEntityTypes", allowedEntityTypesTag);
    }
}
