package net.drgmes.dwm.entities.tardis.consoleunit.controls;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TardisConsoleControlEntity extends Entity {
    private static final TrackedData<String> CONTROL_ROLE = DataTracker.registerData(
        TardisConsoleControlEntity.class,
        TrackedDataHandlerRegistry.STRING
    );

    private static final TrackedData<BlockPos> CONSOLE_UNIT_POS = DataTracker.registerData(
        TardisConsoleControlEntity.class,
        TrackedDataHandlerRegistry.BLOCK_POS
    );

    private BaseTardisConsoleUnitBlockEntity consoleUnit;

    public TardisConsoleControlEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void initDataTracker() {
        this.dataTracker.startTracking(CONTROL_ROLE, ETardisConsoleUnitControlRole.NONE.name());
        this.dataTracker.startTracking(CONSOLE_UNIT_POS, BlockPos.ORIGIN);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        if (tag.contains("controlRole")) {
            this.setTardisControlRole(ETardisConsoleUnitControlRole.valueOf(tag.getString("controlRole")));
        }

        if (tag.contains("consoleUnitPos")) {
            this.setTardisConsolePos(BlockPos.fromLong(tag.getLong("consoleUnitPos")));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        ETardisConsoleUnitControlRole controlRole = this.getTardisControlRole();
        if (controlRole != null) {
            tag.putString("controlRole", controlRole.name());
        }

        BlockPos consoleUnitPos = this.getTardisConsolePos();
        if (consoleUnitPos != null) {
            tag.putLong("consoleUnitPos", consoleUnitPos.asLong());
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient && this.consoleUnit == null) this.discard();
        else super.tick();
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (hand != Hand.OFF_HAND) return ActionResult.PASS;
        if (this.consoleUnit == null) return ActionResult.PASS;

        this.consoleUnit.useControl(this.getTardisControlRole(), hand, player);
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean handleAttack(Entity entity) {
        if (this.consoleUnit == null) return false;

        this.consoleUnit.useControl(this.getTardisControlRole(), Hand.MAIN_HAND, entity);
        return true;
    }

    public ETardisConsoleUnitControlRole getTardisControlRole() {
        return ETardisConsoleUnitControlRole.valueOf(this.dataTracker.get(CONTROL_ROLE));
    }

    public void setTardisControlRole(ETardisConsoleUnitControlRole controlRole) {
        this.dataTracker.set(CONTROL_ROLE, controlRole.name());
    }

    public BlockPos getTardisConsolePos() {
        return this.dataTracker.get(CONSOLE_UNIT_POS);
    }

    public void setTardisConsolePos(BlockPos blockPos, @Nullable BaseTardisConsoleUnitBlockEntity blockEntity) {
        this.dataTracker.set(CONSOLE_UNIT_POS, blockPos);
        if (blockEntity != null) this.consoleUnit = blockEntity;
    }

    public void setTardisConsolePos(BlockPos blockPos) {
        if (this.getWorld().getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity blockEntity) {
            this.setTardisConsolePos(blockPos, blockEntity);
            return;
        }

        this.setTardisConsolePos(blockPos, null);
    }
}
