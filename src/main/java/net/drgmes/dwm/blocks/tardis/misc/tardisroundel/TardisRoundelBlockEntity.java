package net.drgmes.dwm.blocks.tardis.misc.tardisroundel;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.network.TardisMiscRemoteCallablePackets;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.base.DelegatingEnergyStorage;

import java.util.function.Supplier;

public class TardisRoundelBlockEntity extends BlockEntity {
    public final TardisRoundelEnergyStorage energyStorage = new TardisRoundelEnergyStorage(512, 2048);

    public boolean uncovered;
    public boolean lightMode;
    public Identifier blockTemplate;

    public TardisRoundelBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public TardisRoundelBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ModBlockEntities.TARDIS_ROUNDEL, blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        this.uncovered = tag.getBoolean("uncovered");
        this.lightMode = tag.getBoolean("lightMode");

        if (tag.contains("blockTemplate")) this.blockTemplate = new Identifier(tag.getString("blockTemplate"));
        else this.blockTemplate = null;
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        tag.putBoolean("uncovered", this.uncovered);
        tag.putBoolean("lightMode", this.lightMode);

        if (this.blockTemplate != null) tag.putString("blockTemplate", this.blockTemplate.toString());
        else tag.remove("blockTemplate");
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void sendUpdatePacket() {
        if (!(this.world instanceof ServerWorld serverWorld)) return;

        PacketHelper.sendToClient(
            TardisMiscRemoteCallablePackets.class,
            "updateTardisRoundelData",
            serverWorld.getWorldChunk(this.getPos()),
            this.getPos(), this.uncovered, this.lightMode
        );

        if (this.blockTemplate != null) {
            PacketHelper.sendToClient(
                TardisMiscRemoteCallablePackets.class,
                "updateTardisRoundelBlockTemplate",
                serverWorld.getWorldChunk(this.getPos()),
                this.getPos(), this.blockTemplate
            );
        }
        else {
            PacketHelper.sendToClient(
                TardisMiscRemoteCallablePackets.class,
                "clearTardisRoundelBlockTemplate",
                serverWorld.getWorldChunk(this.getPos()),
                this.getPos()
            );
        }
    }

    protected class TardisRoundelEnergyStorage extends DelegatingEnergyStorage {
        private final long maxInsert;
        private final long maxExtract;

        public TardisRoundelEnergyStorage(long maxInsert, long maxExtract, Supplier<ServerWorld> tardisWorldGetter) {
            super(
                () -> TardisStateManager.get(tardisWorldGetter.get()).map(TardisStateManager::getEnergyStorage).orElse(null),
                () -> tardisWorldGetter.get() != null
            );

            this.maxInsert = maxInsert;
            this.maxExtract = maxExtract;
        }

        public TardisRoundelEnergyStorage(long maxInsert, long maxExtract) {
            this(maxInsert, maxExtract, () -> {
                World world = TardisRoundelBlockEntity.this.world;
                return world instanceof ServerWorld serverWorld && TardisHelper.isTardisDimension(world) ? serverWorld : null;
            });
        }

        @Override
        public boolean supportsInsertion() {
            return maxInsert > 0 && super.supportsInsertion();
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        public long insert(long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notNegative(maxAmount);
            return maxInsert == 0 || !validPredicate.getAsBoolean() ? 0 : backingStorage.get().insert(Math.min(maxAmount, maxInsert), transaction);
        }

        @Override
        public boolean supportsExtraction() {
            return maxExtract > 0 && super.supportsExtraction();
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        public long extract(long maxAmount, TransactionContext transaction) {
            StoragePreconditions.notNegative(maxAmount);
            return maxExtract == 0 || !validPredicate.getAsBoolean() ? 0 : backingStorage.get().extract(Math.min(maxAmount, maxExtract), transaction);
        }
    }
}
