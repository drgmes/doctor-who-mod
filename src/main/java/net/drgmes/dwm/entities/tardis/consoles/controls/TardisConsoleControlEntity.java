package net.drgmes.dwm.entities.tardis.consoles.controls;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class TardisConsoleControlEntity extends Entity {
    private BaseTardisConsoleBlockEntity consoleBlockEntity;
    private TardisConsoleControlEntry controlEntry;

    public TardisConsoleControlEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void setTardisConsole(BaseTardisConsoleBlockEntity tile) {
        this.consoleBlockEntity = tile;
    }

    public void setTardisControlEntry(TardisConsoleControlEntry controlEntry) {
        this.controlEntry = controlEntry;
    }

    @Override
    protected void defineSynchedData() {
    }

    public void tick() {
        if (!this.level.isClientSide && this.consoleBlockEntity == null) this.discard();
        else super.tick();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        if (this.consoleBlockEntity == null) return false;

        this.consoleBlockEntity.useControl(this.controlEntry, InteractionHand.MAIN_HAND, entity);
        return true;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (hand != InteractionHand.OFF_HAND) return InteractionResult.PASS;
        if (this.consoleBlockEntity == null) return InteractionResult.PASS;

        this.consoleBlockEntity.useControl(this.controlEntry, hand, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
