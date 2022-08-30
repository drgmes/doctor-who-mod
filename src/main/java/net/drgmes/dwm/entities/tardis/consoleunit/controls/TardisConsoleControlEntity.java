package net.drgmes.dwm.entities.tardis.consoleunit.controls;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TardisConsoleControlEntity extends Entity {
    private BaseTardisConsoleUnitBlockEntity consoleBlockEntity;
    private TardisConsoleControlEntry controlEntry;

    public TardisConsoleControlEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setTardisConsole(BaseTardisConsoleUnitBlockEntity tile) {
        this.consoleBlockEntity = tile;
    }

    public void setTardisControlEntry(TardisConsoleControlEntry controlEntry) {
        this.controlEntry = controlEntry;
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void tick() {
        if (!this.world.isClient && this.consoleBlockEntity == null) this.discard();
        else super.tick();
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (hand != Hand.OFF_HAND) return ActionResult.PASS;
        if (this.consoleBlockEntity == null) return ActionResult.PASS;

        this.playControlSound();
        this.consoleBlockEntity.useControl(this.controlEntry, hand, player);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean handleAttack(Entity entity) {
        if (this.consoleBlockEntity == null) return false;

        this.playControlSound();
        this.consoleBlockEntity.useControl(this.controlEntry, Hand.MAIN_HAND, entity);
        return true;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    private void playControlSound() {
        SoundEvent sound = this.controlEntry.role.sound;
        if (!this.getWorld().isClient && sound != null) ModSounds.playSound(this.getWorld(), this.getBlockPos(), this.controlEntry.role.sound, 1.0F, 1.0F);
    }
}
