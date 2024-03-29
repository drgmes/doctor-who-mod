package net.drgmes.dwm.entities.tardis.consoleunit.controls;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TardisConsoleControlEntity extends Entity {
    private BaseTardisConsoleUnitBlockEntity consoleBlockEntity;
    private TardisConsoleControlEntry controlEntry;

    public TardisConsoleControlEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient && this.consoleBlockEntity == null) this.discard();
        else super.tick();
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (hand != Hand.OFF_HAND) return ActionResult.PASS;
        if (this.consoleBlockEntity == null) return ActionResult.PASS;

        this.consoleBlockEntity.useControl(this.controlEntry, hand, player);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean handleAttack(Entity entity) {
        if (this.consoleBlockEntity == null) return false;

        this.consoleBlockEntity.useControl(this.controlEntry, Hand.MAIN_HAND, entity);
        return true;
    }

    public void setTardisConsole(BaseTardisConsoleUnitBlockEntity tile) {
        this.consoleBlockEntity = tile;
    }

    public void setTardisControlEntry(TardisConsoleControlEntry controlEntry) {
        this.controlEntry = controlEntry;
    }
}
