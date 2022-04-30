package net.drgmes.dwm.caps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.drgmes.dwm.blocks.tardisdoor.TardisDoorBlock;
import net.drgmes.dwm.blocks.tardisdoor.TardisDoorBlockEntity;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.network.ClientboundTardisConsoleWorldDataUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisDoorUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.DWMUtils;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TardisLevelDataCapability implements ITardisLevelData {
    private Map<Class<? extends ITardisSystem>, ITardisSystem> systems = new HashMap<>();

    private BlockPos entracePosition = TardisHelper.TARDIS_POS.above(7).south(1).east(14).immutable();
    private Direction entraceFacing = Direction.SOUTH;

    private List<TardisDoorBlockEntity> doorTiles = new ArrayList<>();
    private List<BaseTardisConsoleBlockEntity> consoleTiles = new ArrayList<>();
    private Level level;

    private int energyArtron = 0;
    private int energyForge = 0;
    private int xyzStep = 1;

    private boolean doorsOpened = false;
    private boolean lightEnabled = false;
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

    public TardisLevelDataCapability(Level level) {
        this.level = level;

        this.addSystem(new TardisSystemMaterialization(this));
        this.addSystem(new TardisSystemFlight(this));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag tdTag = new CompoundTag();

        tdTag.putString("prevExteriorDimension", this.getPreviousExteriorDimension().location().toString());
        tdTag.putString("currExteriorDimension", this.getCurrentExteriorDimension().location().toString());
        tdTag.putString("destExteriorDimension", this.getDestinationExteriorDimension().location().toString());

        tdTag.putString("entraceFacing", this.getEntraceFacing().getName());
        tdTag.putString("prevExteriorFacing", this.getPreviousExteriorFacing().getName());
        tdTag.putString("currExteriorFacing", this.getCurrentExteriorFacing().getName());
        tdTag.putString("destExteriorFacing", this.getDestinationExteriorFacing().getName());

        tdTag.putInt("entracePositionX", this.getEntracePosition().getX());
        tdTag.putInt("entracePositionY", this.getEntracePosition().getY());
        tdTag.putInt("entracePositionZ", this.getEntracePosition().getZ());

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

        this.getSystems().values().forEach((system) -> {
            tdTag.put(system.getClass().getName(), system.save());
        });

        tag.put("tardisdim", tdTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        CompoundTag tdTag = tag.getCompound("tardisdim");

        this.prevExteriorDimension = this.getDimensionByKey(tdTag, "prevExteriorDimension");
        this.currExteriorDimension = this.getDimensionByKey(tdTag, "currExteriorDimension");
        this.destExteriorDimension = this.getDimensionByKey(tdTag, "destExteriorDimension");

        this.entraceFacing = this.getDirectionByKey(tdTag, "entraceFacing");
        this.prevExteriorFacing = this.getDirectionByKey(tdTag, "prevExteriorFacing");
        this.currExteriorFacing = this.getDirectionByKey(tdTag, "currExteriorFacing");
        this.destExteriorFacing = this.getDirectionByKey(tdTag, "destExteriorFacing");

        this.entracePosition = this.getBlockPosByKey(tdTag, "entracePosition");
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

        this.getSystems().values().forEach((system) -> {
            system.load(tdTag.getCompound(system.getClass().getName()));
        });
    }

    @Override
    public void addSystem(ITardisSystem system) {
        this.systems.put(system.getClass(), system);
    }

    @Override
    public Map<Class<? extends ITardisSystem>, ITardisSystem> getSystems() {
        return this.systems;
    }

    @Override
    public ITardisSystem getSystem(Class<? extends ITardisSystem> system) {
        return this.systems.containsKey(system) ? this.systems.get(system) : null;
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
    public boolean isLightEnabled() {
        return this.lightEnabled;
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
    public ServerLevel getLevel() {
        return this.level instanceof ServerLevel serverLevel ? serverLevel : null;
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
    public Direction getEntraceFacing() {
        return this.entraceFacing;
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
    public BlockPos getEntracePosition() {
        return this.entracePosition;
    }

    @Override
    public BlockPos getPreviousExteriorPosition() {
        return this.prevExteriorPosition != null ? this.prevExteriorPosition.immutable() : this.getCurrentExteriorPosition();
    }

    @Override
    public BlockPos getCurrentExteriorPosition() {
        return this.currExteriorPosition.immutable();
    }

    @Override
    public BlockPos getCurrentExteriorRelativePosition() {
        return this.currExteriorPosition.relative(this.currExteriorFacing).immutable();
    }

    @Override
    public BlockPos getDestinationExteriorPosition() {
        return this.destExteriorPosition != null ? this.destExteriorPosition.immutable() : this.getCurrentExteriorPosition();
    }

    @Override
    public int getXYZStep() {
        return this.xyzStep;
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
    public TardisDoorBlockEntity getMainDoorTile() {
        int size = this.doorTiles.size();
        return size > 0 ? this.doorTiles.get(size - 1) : null;
    }

    @Override
    public List<TardisDoorBlockEntity> getDoorTiles() {
        return this.doorTiles;
    }

    @Override
    public BaseTardisConsoleBlockEntity getMainConsoleTile() {
        int size = this.consoleTiles.size();
        return size > 0 ? this.consoleTiles.get(size - 1) : null;
    }

    @Override
    public List<BaseTardisConsoleBlockEntity> getConsoleTiles() {
        return this.consoleTiles;
    }

    @Override
    public void setDimension(ResourceKey<Level> dimension, boolean shouldUpdatePrev) {
        if (shouldUpdatePrev) this.prevExteriorDimension = this.currExteriorDimension;
        this.currExteriorDimension = dimension;
    }

    @Override
    public void setDestinationDimension(ResourceKey<Level> dimension) {
        this.destExteriorDimension = dimension;
    }

    @Override
    public void setEntraceFacing(Direction direction) {
        this.entraceFacing = direction;
    }

    @Override
    public void setFacing(Direction direction, boolean shouldUpdatePrev) {
        if (shouldUpdatePrev) this.prevExteriorFacing = this.currExteriorFacing;
        this.currExteriorFacing = direction;
    }

    @Override
    public void setDestinationFacing(Direction direction) {
        this.destExteriorFacing = direction;
    }

    @Override
    public void setEntracePosition(BlockPos blockPos) {
        this.entracePosition = blockPos.immutable();
    }

    @Override
    public void setPosition(BlockPos blockPos, boolean shouldUpdatePrev) {
        if (shouldUpdatePrev) this.prevExteriorPosition = this.currExteriorPosition;
        this.currExteriorPosition = blockPos.immutable();
    }

    @Override
    public void setDestinationPosition(BlockPos blockPos) {
        this.destExteriorPosition = blockPos.immutable();
    }

    @Override
    public void setDoorsState(boolean flag, boolean shouldUpdate) {
        if (flag && this.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            if (!materializationSystem.isMaterialized()) return;
        }

        if (this.doorsOpened == flag) return;
        this.doorsOpened = flag;

        if (shouldUpdate) {
            this.updateDoorTiles();
            this.updateExterior();
        }
    }

    @Override
    public void setLightState(boolean flag, boolean shouldUpdate) {
        if (this.lightEnabled == flag) return;
        this.lightEnabled = flag;

        if (shouldUpdate) {
            this.updateExterior();
        }
    }

    @Override
    public void setShieldsState(boolean flag, boolean shouldUpdate) {
        if (this.shieldsEnabled == flag) return;
        this.shieldsEnabled = flag;
    }

    @Override
    public void setEnergyArtronHarvesting(boolean flag) {
        this.energyArtronHarvesting = flag;
    }

    @Override
    public void setEnergyForgeHarvesting(boolean flag) {
        this.energyForgeHarvesting = flag;
    }

    @Override
    public void updateDoorTiles() {
        this.doorTiles.forEach((tile) -> {
            level.setBlock(tile.getBlockPos(), tile.getBlockState().setValue(TardisDoorBlock.OPEN, this.isDoorsOpened()), 3);

            ClientboundTardisDoorUpdatePacket packet = new ClientboundTardisDoorUpdatePacket(tile.getBlockPos(), this.isDoorsOpened());
            ModPackets.send(this.level.getChunkAt(tile.getBlockPos()), packet);
        });
    }

    @Override
    public void updateConsoleTiles() {
        this.consoleTiles.forEach((tile) -> {
            this.applyDataToControlsStorage(tile.controlsStorage);
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

    public void applyDataToControlsStorage(TardisConsoleControlsStorage controlsStorage) {
        if (this.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
            controlsStorage.values.put(TardisConsoleControlRoles.STARTER, flightSystem.inProgress());
        }

        if (this.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            controlsStorage.values.put(TardisConsoleControlRoles.MATERIALIZATION, !materializationSystem.isMaterialized());
            controlsStorage.values.put(TardisConsoleControlRoles.SAFE_DIRECTION, materializationSystem.safeDirection.ordinal());
        }

        controlsStorage.values.put(TardisConsoleControlRoles.DOORS, this.isDoorsOpened());
        controlsStorage.values.put(TardisConsoleControlRoles.SHIELDS, this.isShieldsEnabled());
        controlsStorage.values.put(TardisConsoleControlRoles.LIGHT, this.isLightEnabled());
        controlsStorage.values.put(TardisConsoleControlRoles.ENERGY_ARTRON_HARVESTING, this.isEnergyArtronHarvesting());
        controlsStorage.values.put(TardisConsoleControlRoles.ENERGY_FORGE_HARVESTING, this.isEnergyForgeHarvesting());
        controlsStorage.values.put(TardisConsoleControlRoles.FACING, this.getDestinationExteriorFacing().ordinal() - 2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void applyControlsStorageToData(TardisConsoleControlsStorage controlsStorage) {
        boolean isInFlight = false;
        boolean isMaterialized = false;

        if (this.destExteriorDimension == null) this.destExteriorDimension = this.currExteriorDimension;
        if (this.destExteriorFacing == null) this.destExteriorFacing = this.currExteriorFacing;
        if (this.destExteriorPosition == null) this.destExteriorPosition = this.currExteriorPosition;

        // Flight
        if (this.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
            boolean handbrake = (boolean) controlsStorage.get(TardisConsoleControlRoles.HANDBRAKE);
            flightSystem.setFlight(handbrake ? false : (boolean) controlsStorage.get(TardisConsoleControlRoles.STARTER));
            isInFlight = flightSystem.inProgress();
        }

        // Materialization
        if (this.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            materializationSystem.setSafeDirection(Math.abs((int) controlsStorage.get(TardisConsoleControlRoles.SAFE_DIRECTION)));
            materializationSystem.setMaterializationState(!(boolean) controlsStorage.get(TardisConsoleControlRoles.MATERIALIZATION));
            isMaterialized = materializationSystem.isMaterialized();
        }

        // Only if Tardis is not in flight (and could be when dematerialized)
        if (!isInFlight) {
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
                Map<ResourceKey<Level>, ServerLevel> levels = this.level.getServer().forgeGetWorldMap();
                List<ResourceKey<Level>> levelKeys = new ArrayList<>();

                levels.keySet().forEach((key) -> {
                    if (levels.get(key).dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) return;
                    if (key == this.level.dimension()) return;
                    levelKeys.add(key);
                });

                int index = levelKeys.contains(this.destExteriorDimension) ? levelKeys.indexOf(this.destExteriorDimension) : 0;
                index = levelKeys.indexOf(this.destExteriorDimension) + (dimPrev != 0 ? -1 : 1);
                index %= levelKeys.size();
                index = index < 0 ? levelKeys.size() - 1 : index;

                this.destExteriorDimension = levelKeys.get(index);
            }

            // Reset to Prev
            int resetToPrev = (int) controlsStorage.get(TardisConsoleControlRoles.RESET_TO_PREV);
            if (resetToPrev != 0) this.destExteriorDimension = this.getPreviousExteriorDimension();
            if (resetToPrev != 0) this.destExteriorFacing = this.getPreviousExteriorFacing();
            if (resetToPrev != 0) this.destExteriorPosition = this.getPreviousExteriorPosition();

            // Reset to Current
            int resetToCurr = (int) controlsStorage.get(TardisConsoleControlRoles.RESET_TO_CURR);
            if (resetToCurr != 0) this.destExteriorDimension = this.getCurrentExteriorDimension();
            if (resetToCurr != 0) this.destExteriorFacing = this.getCurrentExteriorFacing();
            if (resetToCurr != 0) this.destExteriorPosition = this.getCurrentExteriorPosition();
        }

        // Only if Tardis materialized
        if (isMaterialized) {
            // Doors
            this.setDoorsState((boolean) controlsStorage.get(TardisConsoleControlRoles.DOORS), true);

            // Light
            this.setLightState((boolean) controlsStorage.get(TardisConsoleControlRoles.LIGHT), true);

            // Shields
            this.setShieldsState((boolean) controlsStorage.get(TardisConsoleControlRoles.SHIELDS), true);

            // Energy Artron Harvesting
            this.setEnergyArtronHarvesting((boolean) controlsStorage.get(TardisConsoleControlRoles.ENERGY_ARTRON_HARVESTING));

            // Energy Forge Harvesting
            this.setEnergyForgeHarvesting((boolean) controlsStorage.get(TardisConsoleControlRoles.ENERGY_FORGE_HARVESTING));
        }

        this.updateConsoleTiles();
    }

    @Override
    public void tick() {
        if (this.level.isClientSide || !this.isValid()) return;
        this.getSystems().values().forEach((system) -> system.tick());
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

    private void updateExterior() {
        if (!this.isValid() || !(this.level instanceof ServerLevel)) return;

        ServerLevel exteriorLevel = ((ServerLevel) this.level).getServer().getLevel(this.getCurrentExteriorDimension());
        if (exteriorLevel == null) return;

        DWMUtils.runInThread("tardisUpdateExterior", () -> {
            BlockPos exteriorBlockPos = this.getCurrentExteriorPosition();
            BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);
            if (!Thread.currentThread().isAlive() || Thread.currentThread().isInterrupted()) return;

            try {
                if (exteriorBlockState.getBlock() instanceof TardisExteriorBlock) {
                    exteriorBlockState = exteriorBlockState.setValue(TardisExteriorBlock.OPEN, this.isDoorsOpened());
                    exteriorBlockState = exteriorBlockState.setValue(TardisExteriorBlock.LIT, this.isLightEnabled());
                    exteriorLevel.setBlock(exteriorBlockPos, exteriorBlockState, 3);

                    if (exteriorLevel.getBlockState(exteriorBlockPos.above()).getBlock() instanceof TardisExteriorBlock) {
                        exteriorBlockState = exteriorBlockState.setValue(TardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
                        exteriorLevel.setBlock(exteriorBlockPos.above(), exteriorBlockState, 3);
                    }

                    this.sendExteriorUpdatePacket(exteriorLevel, exteriorBlockPos);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }

    private void sendExteriorUpdatePacket(ServerLevel exteriorLevel, BlockPos exteriorBlockPos) {
        if (exteriorLevel == null || exteriorBlockPos == null) return;

        ModPackets.send(exteriorLevel.getChunkAt(exteriorBlockPos), new ClientboundTardisExteriorUpdatePacket(
            exteriorBlockPos,
            this.isDoorsOpened(),
            this.isLightEnabled(),
            false,
            false
        ));
    }
}
