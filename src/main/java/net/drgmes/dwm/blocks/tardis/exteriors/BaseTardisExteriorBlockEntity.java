package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.network.TardisExteriorRemoteCallablePackets;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.UUID;

public abstract class BaseTardisExteriorBlockEntity extends BlockEntity {
    public String tardisId;

    private int tickInProgress = 0;
    private boolean isMaterialized = false;
    private boolean inRematProgress = false;
    private boolean inDematProgress = false;

    public BaseTardisExteriorBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        this.tardisId = tag.getString("tardisId");
        this.tickInProgress = tag.getInt("tickInProgress");
        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.inRematProgress = tag.getBoolean("inRematProgress");
        this.inDematProgress = tag.getBoolean("inDematProgress");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        tag.putString("tardisId", this.getTardisId());
        tag.putInt("tickInProgress", this.tickInProgress);
        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putBoolean("inRematProgress", this.inRematProgress);
        tag.putBoolean("inDematProgress", this.inDematProgress);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void tick() {
        float goal = this.inRematProgress ? DWM.TIMINGS.REMAT : (this.inDematProgress ? DWM.TIMINGS.DEMAT : 0);

        if (goal > 0) {
            if (this.inRematProgress) this.inDematProgress = false;
            if (this.inDematProgress) this.inRematProgress = false;
            if (this.tickInProgress < goal) this.tickInProgress++;
            else this.resetMaterializationState(this.inRematProgress);
        }

        if (this.world instanceof ServerWorld) {
            ServerWorld tardisWorld = DimensionHelper.getModWorld(this.getTardisId());
            if (tardisWorld == null) return;

            Optional<TardisStateManager> tardisStateManager = TardisStateManager.get(tardisWorld);
            if (tardisStateManager.isEmpty()) return;

            if (!tardisStateManager.get().isValid()
                || !tardisStateManager.get().getCurrentExteriorDimension().equals(this.world.getRegistryKey())
                || !tardisStateManager.get().getCurrentExteriorPosition().equals(this.getPos())
            ) {
                if (this.getMaterializedPercent() >= 100) {
                    this.demat();

                    PacketHelper.sendToClient(
                        TardisExteriorRemoteCallablePackets.class,
                        "updateTardisExteriorData",
                        world.getWorldChunk(this.getPos()),
                        this.getPos(), false, true
                    );
                }
                else if (!this.inDematProgress) {
                    this.world.removeBlock(this.getPos().up(), false);
                    this.world.removeBlock(this.getPos(), false);
                }
            }
        }
    }

    public void remat() {
        this.inRematProgress = true;
        ModSounds.playTardisLandingSound(this.world, this.getPos());
    }

    public void demat() {
        this.inDematProgress = true;
        ModSounds.playTardisTakeoffSound(this.world, this.getPos());
    }

    public void resetMaterializationState(boolean flag) {
        this.isMaterialized = flag;
        this.inRematProgress = false;
        this.inDematProgress = false;
        this.tickInProgress = 0;
    }

    public int getMaterializedPercent() {
        if (this.inDematProgress) return (int) Math.ceil((DWM.TIMINGS.DEMAT - this.tickInProgress) / DWM.TIMINGS.DEMAT * 100);
        if (this.inRematProgress) return (int) Math.ceil(this.tickInProgress / DWM.TIMINGS.REMAT * 100);
        return this.isMaterialized ? 100 : 0;
    }

    public String getTardisId() {
        if (this.tardisId == null) this.tardisId = UUID.randomUUID().toString();
        return this.tardisId;
    }

    public ServerWorld getTardisWorld() {
        return TardisHelper.getOrCreateTardisWorld(this);
    }
}
