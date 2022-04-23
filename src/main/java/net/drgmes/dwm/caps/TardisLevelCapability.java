package net.drgmes.dwm.caps;

import java.util.ArrayList;
import java.util.List;

import net.drgmes.dwm.blocks.tardisdoor.TardisDoorBlock;
import net.drgmes.dwm.blocks.tardisdoor.TardisDoorBlockEntity;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.network.ClientboundTardisConsoleWorldDataUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisDoorUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TardisLevelCapability implements ITardisLevelData {
    private List<TardisDoorBlockEntity> doorTiles = new ArrayList<>();
    private List<BaseTardisConsoleBlockEntity> consoleTiles = new ArrayList<>();
    private Level level;

    private int energyArtron = 0;
    private int energyForge = 0;
    private int xyzStep = 1;

    private boolean doorsOpened = true;
    private boolean shieldsEnabled = false;
    private boolean energyArtronHarvesting = false;
    private boolean energyForgeHarvesting = false;

    private ResourceKey<Level> prevExteriorDimension;
    private ResourceKey<Level> currExteriorDimension;
    private ResourceKey<Level> destExteriorDimension;

    private Direction prevExteriorFacing;
    private Direction currExteriorFacing;
    private Direction destExteriorFacing;

    private BlockPos prevExteriorPosition;
    private BlockPos currExteriorPosition;
    private BlockPos destExteriorPosition;

    public TardisLevelCapability(Level level) {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag tdTag = new CompoundTag();

        tdTag.putString("prevExteriorDimension", this.getPreviousExteriorDimension().location().toString());
        tdTag.putString("currExteriorDimension", this.getCurrentExteriorDimension().location().toString());
        tdTag.putString("destExteriorDimension", this.getDestinationExteriorDimension().location().toString());

        tdTag.putString("prevExteriorFacing", this.getPreviousExteriorFacing().getName());
        tdTag.putString("currExteriorFacing", this.getCurrentExteriorFacing().getName());
        tdTag.putString("destExteriorFacing", this.getDestinationExteriorFacing().getName());

        tdTag.putInt("prevExteriorPositionX", this.getPreviousExteriorPosition().getX());
        tdTag.putInt("prevExteriorPositionY", this.getPreviousExteriorPosition().getY());
        tdTag.putInt("prevExteriorPositionZ", this.getPreviousExteriorPosition().getZ());

        tdTag.putInt("currExteriorPositionX", this.getCurrentExteriorPosition().getX());
        tdTag.putInt("currExteriorPositionY", this.getCurrentExteriorPosition().getY());
        tdTag.putInt("currExteriorPositionZ", this.getCurrentExteriorPosition().getZ());

        tdTag.putInt("destExteriorPositionX", this.getDestinationExteriorPosition().getX());
        tdTag.putInt("destExteriorPositionY", this.getDestinationExteriorPosition().getY());
        tdTag.putInt("destExteriorPositionZ", this.getDestinationExteriorPosition().getZ());

        tdTag.putInt("energyArtron", this.energyArtron);
        tdTag.putInt("energyForge", this.energyForge);
        tdTag.putInt("xyzStep", this.xyzStep);

        tdTag.putBoolean("doorsOpened", this.doorsOpened);
        tdTag.putBoolean("shieldsEnabled", this.shieldsEnabled);
        tdTag.putBoolean("energyArtronHarvesting", this.energyArtronHarvesting);
        tdTag.putBoolean("energyForgeHarvesting", this.energyForgeHarvesting);

        tag.put("tardisdim", tdTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        CompoundTag tdTag = tag.getCompound("tardisdim");

        this.prevExteriorDimension = this.getDimensionByKey(tdTag, "prevExteriorDimension");
        this.currExteriorDimension = this.getDimensionByKey(tdTag, "currExteriorDimension");
        this.destExteriorDimension = this.getDimensionByKey(tdTag, "destExteriorDimension");

        this.prevExteriorFacing = this.getDirectionByKey(tdTag, "prevExteriorFacing");
        this.currExteriorFacing = this.getDirectionByKey(tdTag, "currExteriorFacing");
        this.destExteriorFacing = this.getDirectionByKey(tdTag, "destExteriorFacing");

        this.prevExteriorPosition = this.getBlockPosByKey(tdTag, "prevExteriorPosition");
        this.currExteriorPosition = this.getBlockPosByKey(tdTag, "currExteriorPosition");
        this.destExteriorPosition = this.getBlockPosByKey(tdTag, "destExteriorPosition");

        this.energyArtron = tdTag.getInt("energyArtron");
        this.energyForge = tdTag.getInt("energyForge");
        this.xyzStep = tdTag.getInt("xyzStep");

        this.doorsOpened = tdTag.getBoolean("doorsOpened");
        this.shieldsEnabled = tdTag.getBoolean("shieldsEnabled");
        this.energyArtronHarvesting = tdTag.getBoolean("energyArtronHarvesting");
        this.energyForgeHarvesting = tdTag.getBoolean("energyForgeHarvesting");
    }

    @Override
    public boolean isValid() {
        return this.currExteriorDimension != null;
    }

    @Override
    public boolean isDoorsOpened() {
        return this.doorsOpened;
    }

    @Override
    public boolean isShieldsEnabled() {
        return this.shieldsEnabled;
    }

    @Override
    public boolean isEnergyArtronHarvesting() {
        return this.energyArtronHarvesting;
    }

    @Override
    public boolean isEnergyForgeHarvesting() {
        return this.energyForgeHarvesting;
    }

    @Override
    public List<TardisDoorBlockEntity> getDoorTiles() {
        return this.doorTiles;
    }

    @Override
    public List<BaseTardisConsoleBlockEntity> getConsoleTiles() {
        return this.consoleTiles;
    }

    @Override
    public int getEnergyArtron() {
        return this.energyArtron;
    }

    @Override
    public int getEnergyForge() {
        return this.energyForge;
    }

    @Override
    public int getXYZStep() {
        return this.xyzStep;
    }

    @Override
    public ResourceKey<Level> getPreviousExteriorDimension() {
        return this.prevExteriorDimension != null ? this.prevExteriorDimension : this.getCurrentExteriorDimension();
    }

    @Override
    public ResourceKey<Level> getCurrentExteriorDimension() {
        return this.currExteriorDimension;
    }

    @Override
    public ResourceKey<Level> getDestinationExteriorDimension() {
        return this.destExteriorDimension != null ? this.destExteriorDimension : this.getCurrentExteriorDimension();
    }

    @Override
    public Direction getPreviousExteriorFacing() {
        return this.prevExteriorFacing != null ? this.prevExteriorFacing : this.getCurrentExteriorFacing();
    }

    @Override
    public Direction getCurrentExteriorFacing() {
        return this.currExteriorFacing;
    }

    @Override
    public Direction getDestinationExteriorFacing() {
        return this.destExteriorFacing != null ? this.destExteriorFacing : this.getCurrentExteriorFacing();
    }

    @Override
    public BlockPos getPreviousExteriorPosition() {
        return this.prevExteriorPosition != null ? this.prevExteriorPosition : this.getCurrentExteriorPosition();
    }

    @Override
    public BlockPos getCurrentExteriorPosition() {
        return this.currExteriorPosition;
    }

    @Override
    public BlockPos getCurrentExteriorRelativePosition() {
        return this.currExteriorPosition.relative(this.currExteriorFacing);
    }

    @Override
    public BlockPos getDestinationExteriorPosition() {
        return this.destExteriorPosition != null ? this.destExteriorPosition : this.getCurrentExteriorPosition();
    }

    @Override
    public void updateDoorTiles() {
        this.doorTiles.forEach((tile) -> {
            level.setBlock(tile.getBlockPos(), tile.getBlockState().setValue(TardisDoorBlock.OPEN, this.isDoorsOpened()), 10);

            ClientboundTardisDoorUpdatePacket packet = new ClientboundTardisDoorUpdatePacket(tile.getBlockPos(), this.isDoorsOpened());
            ModPackets.send(this.level.getChunkAt(tile.getBlockPos()), packet);
        });
    }

    @Override
    public void updateConsoleTiles() {
        this.consoleTiles.forEach((tile) -> {
            tile.controlsStorage.applyTardisWorldStorage(this);
            tile.sendControlsUpdatePacket();

            tile.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                CompoundTag tag = this.serializeNBT();
                provider.deserializeNBT(tag);
                tile.setChanged();

                ClientboundTardisConsoleWorldDataUpdatePacket packet = new ClientboundTardisConsoleWorldDataUpdatePacket(tile.getBlockPos(), tag);
                ModPackets.send(level.getChunkAt(tile.getBlockPos()), packet);
            });
        });
    }

    @Override
    public void updateDoorsState(boolean flag, boolean shouldUpdate) {
        if (this.doorsOpened == flag) return;
        this.doorsOpened = flag;

        if (shouldUpdate && this.isValid() && this.level instanceof ServerLevel) {
            ServerLevel exteriorLevel = ((ServerLevel) this.level).getServer().getLevel(this.currExteriorDimension);
            BlockState exteriorBlockState = exteriorLevel.getBlockState(this.currExteriorPosition);

            if (exteriorBlockState.getBlock() instanceof TardisExteriorBlock) {
                exteriorLevel.setBlock(this.currExteriorPosition, exteriorBlockState.setValue(TardisExteriorBlock.OPEN, this.isDoorsOpened()), 10);

                ClientboundTardisExteriorUpdatePacket packet = new ClientboundTardisExteriorUpdatePacket(this.currExteriorPosition, this.isDoorsOpened());
                ModPackets.send(exteriorLevel.getChunkAt(this.currExteriorPosition), packet);
            }

            this.updateDoorTiles();
        }
    }

    @Override
    public void updateShieldsState(boolean flag, boolean shouldUpdate) {
        if (this.shieldsEnabled == flag) return;
        this.shieldsEnabled = flag;
    }

    @Override
    public void updateEnergyArtronHarvesting(boolean flag) {
        this.energyArtronHarvesting = flag;
    }

    @Override
    public void updateEnergyForgeHarvesting(boolean flag) {
        this.energyForgeHarvesting = flag;
    }

    @Override
    public void updateDimension(ResourceKey<Level> dimension) {
        this.prevExteriorDimension = this.currExteriorDimension;
        this.currExteriorDimension = dimension;
    }

    @Override
    public void updateFacing(Direction direction) {
        this.prevExteriorFacing = this.currExteriorFacing;
        this.currExteriorFacing = direction;
    }

    @Override
    public void updatePosition(BlockPos blockPos) {
        this.prevExteriorPosition = this.currExteriorPosition;
        this.currExteriorPosition = blockPos;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void applyControlsStorage(TardisConsoleControlsStorage controlsStorage) {
        if (this.destExteriorDimension == null) this.destExteriorDimension = this.currExteriorDimension;
        if (this.destExteriorFacing == null) this.destExteriorFacing = this.currExteriorFacing;
        if (this.destExteriorPosition == null) this.destExteriorPosition = this.currExteriorPosition;

        // Doors
        this.updateDoorsState((boolean) controlsStorage.get(TardisConsoleControlRoles.DOORS), true);

        // Shields
        this.updateShieldsState((boolean) controlsStorage.get(TardisConsoleControlRoles.SHIELDS), true);

        // Energy Artron Harvesting
        this.updateEnergyArtronHarvesting((boolean) controlsStorage.get(TardisConsoleControlRoles.ENERGY_ARTRON_HARVESTING));

        // Energy Forge Harvesting
        this.updateEnergyForgeHarvesting((boolean) controlsStorage.get(TardisConsoleControlRoles.ENERGY_FORGE_HARVESTING));

        // Facing
        int facing = (int) controlsStorage.get(TardisConsoleControlRoles.FACING);
        this.destExteriorFacing = Direction.values()[(facing < 0 ? TardisConsoleControlRoles.FACING.maxIntValue + facing : facing) + 2];

        // X Set
        int xSet = (int) controlsStorage.get(TardisConsoleControlRoles.XSET);
        if (xSet != 0) this.destExteriorPosition = xSet > 0 ? this.destExteriorPosition.east(this.xyzStep) : this.destExteriorPosition.west(this.xyzStep);

        // Y Set
        int ySet = (int) controlsStorage.get(TardisConsoleControlRoles.YSET);
        if (ySet != 0) this.destExteriorPosition = ySet > 0 ? this.destExteriorPosition.above(this.xyzStep) : this.destExteriorPosition.below(this.xyzStep);

        // Z Set
        int zSet = (int) controlsStorage.get(TardisConsoleControlRoles.ZSET);
        if (zSet != 0) this.destExteriorPosition = zSet > 0 ? this.destExteriorPosition.south(this.xyzStep) : this.destExteriorPosition.north(this.xyzStep);

        // XYZ Step
        int xyzStep = (int) controlsStorage.get(TardisConsoleControlRoles.XYZSTEP);
        if (xyzStep != 0) this.xyzStep = Math.max(1, Math.min(10000, (int) Math.round(this.xyzStep * (xyzStep > 0 ? 10 : 0.1))));

        // Randomizer
        if ((int) controlsStorage.get(TardisConsoleControlRoles.RANDOMIZER) != 0) {
            boolean facingRandom = Math.random() * 10 > 5;

            if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.east((int) Math.round(Math.random() * 10 * this.xyzStep));
            else this.destExteriorPosition = this.destExteriorPosition.west((int) Math.round(Math.random() * 10 * this.xyzStep));

            if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.south((int) Math.round(Math.random() * 10 * this.xyzStep));
            else this.destExteriorPosition = this.destExteriorPosition.north((int) Math.round(Math.random() * 10 * this.xyzStep));
        }

        // Dimension
        int dimPrev = (int) controlsStorage.get(TardisConsoleControlRoles.DIM_PREV);
        int dimNext = (int) controlsStorage.get(TardisConsoleControlRoles.DIM_NEXT);
        if (dimPrev != 0 || dimNext != 0) {
            List<ResourceKey<Level>> levelKeys = new ArrayList<>();

            this.level.getServer().forgeGetWorldMap().keySet().forEach((key) -> {
                if (key == this.level.dimension()) return;
                levelKeys.add(key);
            });

            int index = levelKeys.contains(this.destExteriorDimension) ? levelKeys.indexOf(this.destExteriorDimension) : 0;
            index = levelKeys.indexOf(this.destExteriorDimension) + (dimPrev != 0 ? -1 : 1);
            index %= levelKeys.size();
            index = index < 0 ? levelKeys.size() - 1 : index;

            this.destExteriorDimension = levelKeys.get(index);
        }

        // Reset
        int reset = (int) controlsStorage.get(TardisConsoleControlRoles.RESET);
        if (reset != 0) this.destExteriorDimension= this.currExteriorDimension;
        if (reset != 0) this.destExteriorFacing = this.currExteriorFacing;
        if (reset != 0) this.destExteriorPosition = this.currExteriorPosition;

        this.updateConsoleTiles();
    }

    private ResourceKey<Level> getDimensionByKey(CompoundTag tag, String key) {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString(key)));
    }

    private Direction getDirectionByKey(CompoundTag tag, String key) {
        return Direction.byName(tag.getString(key));
    }

    private BlockPos getBlockPosByKey(CompoundTag tag, String key) {
        return new BlockPos(tag.getInt(key + "X"), tag.getInt(key + "Y"), tag.getInt(key + "Z"));
    }
}
