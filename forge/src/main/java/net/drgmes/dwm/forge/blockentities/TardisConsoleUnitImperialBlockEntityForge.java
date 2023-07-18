package net.drgmes.dwm.forge.blockentities;

import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.TardisConsoleUnitImperialBlockEntity;
import net.drgmes.dwm.forge.common.TardisEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class TardisConsoleUnitImperialBlockEntityForge extends TardisConsoleUnitImperialBlockEntity {
    private final TardisEnergyStorage energyStorage = new TardisEnergyStorage(8192, 8192);
    private LazyOptional<IEnergyStorage> energyCapability = LazyOptional.empty();

    public TardisConsoleUnitImperialBlockEntityForge(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction side) {
        return ForgeCapabilities.ENERGY.orEmpty(capability, this.energyCapability);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyCapability.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.energyStorage.refreshTardisStateManager(this.getWorld());
        this.energyCapability = LazyOptional.of(() -> this.energyStorage);
    }
}
