package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemResearch;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.enums.TardisConsoleUnitControlFlags;
import net.drgmes.dwm.enums.TardisConsoleUnitControlRole;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class TardisConsoleControlsStorage {
    public final Map<TardisConsoleUnitControlRole, Object> values = new HashMap<>();

    public TardisConsoleControlsStorage() {
        for (TardisConsoleUnitControlRole controlRole : TardisConsoleUnitControlRole.values()) {
            this.get(controlRole);
        }
    }

    public void readNbt(NbtCompound tag) {
        for (TardisConsoleUnitControlRole controlRole : TardisConsoleUnitControlRole.values()) {
            String key = controlRole.name();
            if (!tag.contains(key)) continue;

            this.values.put(controlRole, switch (controlRole.type) {
                case BOOLEAN, BOOLEAN_DIRECT -> tag.getBoolean(key);
                case NUMBER, NUMBER_DIRECT, NUMBER_DIRECT_LIMITED, ANIMATION, ANIMATION_DIRECT -> tag.getInt(key);
                default -> null;
            });
        }
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        for (Entry<TardisConsoleUnitControlRole, Object> entry : this.values.entrySet()) {
            String name = entry.getKey().name();
            Object value = entry.getValue();

            switch (entry.getKey().type) {
                case BOOLEAN, BOOLEAN_DIRECT -> tag.putBoolean(name, (boolean) value);
                case NUMBER, NUMBER_DIRECT, NUMBER_DIRECT_LIMITED, ANIMATION, ANIMATION_DIRECT -> tag.putInt(name, (int) value);
            }
        }

        return tag;
    }

    public Object get(TardisConsoleUnitControlRole controlRole) {
        if (this.values.containsKey(controlRole)) {
            return this.values.get(controlRole);
        }

        return this.values.put(controlRole, switch (controlRole.type) {
            case BOOLEAN, BOOLEAN_DIRECT -> false;
            case NUMBER, NUMBER_DIRECT, NUMBER_DIRECT_LIMITED, ANIMATION, ANIMATION_DIRECT -> 0;
            default -> null;
        });
    }

    public boolean update(TardisConsoleUnitControlRole controlRole, Hand hand) {
        Object initialValue = this.values.get(controlRole);

        Object value = switch (controlRole.type) {
            case BOOLEAN -> this.getUpdatedBoolean(controlRole, hand, initialValue);
            case BOOLEAN_DIRECT -> this.getUpdatedBooleanDirect(controlRole, hand, initialValue);
            case NUMBER -> this.getUpdatedNumber(controlRole, hand, initialValue);
            case NUMBER_DIRECT -> this.getUpdatedNumberDirect(controlRole, hand, initialValue);
            case NUMBER_DIRECT_LIMITED -> this.getUpdatedNumberDirectLimited(controlRole, hand, initialValue);
            case ANIMATION -> this.getUpdatedAnimation(controlRole, hand, initialValue);
            case ANIMATION_DIRECT -> this.getUpdatedAnimationDirect(controlRole, hand, initialValue);
            default -> initialValue;
        };

        if (Objects.equals(initialValue, value)) return false;
        this.values.put(controlRole, value);
        return true;
    }

    public void applyDataFromTardis(TardisStateManager tardis) {
        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        this.values.put(TardisConsoleUnitControlRole.STARTER, flightSystem.inProgress());
        this.values.put(TardisConsoleUnitControlRole.MATERIALIZATION, materializationSystem.isMaterialized() || materializationSystem.isInMaterializationProcess());
        this.values.put(TardisConsoleUnitControlRole.VERTICAL_SCANNING, materializationSystem.getVerticalScanning().ordinal());
        this.values.put(TardisConsoleUnitControlRole.SHIELDS, shieldsSystem.inProgress());
        this.values.put(TardisConsoleUnitControlRole.SHIELDS_OXYGEN, shieldsSystem.isEnabled() && tardis.isShieldsOxygenEnabled());
        this.values.put(TardisConsoleUnitControlRole.SHIELDS_FIRE_PROOF, shieldsSystem.isEnabled() && tardis.isShieldsFireProofEnabled());
        this.values.put(TardisConsoleUnitControlRole.SHIELDS_MEDICAL, shieldsSystem.isEnabled() && tardis.isShieldsMedicalEnabled());
        this.values.put(TardisConsoleUnitControlRole.SHIELDS_MINING, shieldsSystem.isEnabled() && tardis.isShieldsMiningEnabled());
        this.values.put(TardisConsoleUnitControlRole.SHIELDS_GRAVITATION, shieldsSystem.isEnabled() && tardis.isShieldsGravitationEnabled());
        this.values.put(TardisConsoleUnitControlRole.SHIELDS_SPECIAL, shieldsSystem.isEnabled() && tardis.isShieldsSpecialEnabled());
        this.values.put(TardisConsoleUnitControlRole.FUEL_HARVESTING, tardis.isFuelHarvesting());
        this.values.put(TardisConsoleUnitControlRole.ENERGY_HARVESTING, tardis.isEnergyHarvesting());
        this.values.put(TardisConsoleUnitControlRole.LIGHT, tardis.isLightEnabled());
        this.values.put(TardisConsoleUnitControlRole.DOORS, tardis.isDoorsOpened());
        this.values.put(TardisConsoleUnitControlRole.HANDBRAKE, tardis.isHandbrakeLocked());
        this.values.put(TardisConsoleUnitControlRole.FACING, switch (tardis.getDestinationExteriorFacing()) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        });
    }

    public boolean applyDataToTardis(TardisStateManager tardis, TardisConsoleUnitControlRole controlRole, PlayerEntity player) {
        Object value = this.get(controlRole);

        TardisSystemResearch researchSystem = tardis.getSystem(TardisSystemResearch.class);
        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        boolean isUpdated = switch (controlRole) {
            case HANDBRAKE -> {
                boolean handbrake = (boolean) value;

                if (!tardis.setHandbrakeLockState(handbrake, player)) {
                    this.values.put(TardisConsoleUnitControlRole.HANDBRAKE, tardis.isHandbrakeLocked());
                    if (handbrake != tardis.isHandbrakeLocked()) ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                    yield false;
                }

                yield true;
            }

            case STARTER -> {
                boolean starter = (boolean) value;

                if (flightSystem.isEnabled() && !tardis.isHandbrakeLocked()) {
                    yield flightSystem.init(starter, player.getUuid());
                }
                else {
                    this.values.put(TardisConsoleUnitControlRole.STARTER, flightSystem.inProgress());
                    if (starter != flightSystem.inProgress()) ModSounds.playTardisFailSound(tardis.getWorld(), tardis.getMainConsolePosition());
                    yield false;
                }
            }

            case MATERIALIZATION -> {
                boolean materialization = (boolean) value;

                if (materializationSystem.isEnabled() && !tardis.isHandbrakeLocked()) {
                    yield materializationSystem.init(materialization, player.getUuid());
                }
                else {
                    this.values.put(TardisConsoleUnitControlRole.MATERIALIZATION, materializationSystem.isMaterialized());
                    if (materialization != materializationSystem.inProgress()) ModSounds.playTardisFailSound(tardis.getWorld(), tardis.getMainConsolePosition());
                    yield false;
                }
            }

            case VERTICAL_SCANNING -> {
                if (!materializationSystem.isEnabled()) {
                    this.values.put(TardisConsoleUnitControlRole.VERTICAL_SCANNING, materializationSystem.getVerticalScanning().ordinal());
                    yield false;
                }

                int verticalScanning = (int) value;
                materializationSystem.setVerticalScanning(Math.abs(verticalScanning));
                yield true;
            }

            case FACING -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) {
                    this.values.put(TardisConsoleUnitControlRole.FACING, switch (tardis.getDestinationExteriorFacing()) {
                        case EAST -> 1;
                        case SOUTH -> 2;
                        case WEST -> 3;
                        default -> 0;
                    });

                    yield false;
                }

                int facing = (int) value;
                tardis.setDestinationFacing(switch (facing >= 0 ? facing : controlRole.maxIntValue + facing) {
                    case 1 -> Direction.EAST;
                    case 2 -> Direction.SOUTH;
                    case 3 -> Direction.WEST;
                    default -> Direction.NORTH;
                });

                yield true;
            }

            case XYZSTEP -> {
                if (!flightSystem.isEnabled()) yield false;

                int xyzStep = (int) value;
                if (xyzStep != 0) tardis.setXYZStep((int) Math.round(tardis.getXYZStep() * (xyzStep > 0 ? 10 : 0.1)));
                yield true;
            }

            case XSET -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) yield false;

                int xSet = (int) value;
                if (xSet != 0) tardis.setDestinationPosition(xSet > 0 ? tardis.getDestinationExteriorPosition().east(tardis.getXYZStep()) : tardis.getDestinationExteriorPosition().west(tardis.getXYZStep()));
                yield true;
            }

            case YSET -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) yield false;

                int ySet = (int) value;
                if (ySet != 0) tardis.setDestinationPosition(ySet > 0 ? tardis.getDestinationExteriorPosition().up(tardis.getXYZStep()) : tardis.getDestinationExteriorPosition().down(tardis.getXYZStep()));
                yield true;
            }

            case ZSET -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) yield false;

                int zSet = (int) value;
                if (zSet != 0) tardis.setDestinationPosition(zSet > 0 ? tardis.getDestinationExteriorPosition().south(tardis.getXYZStep()) : tardis.getDestinationExteriorPosition().north(tardis.getXYZStep()));
                yield true;
            }

            case RANDOMIZER -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) yield false;

                int randomizer = (int) value;
                if (randomizer == 0) yield false;

                boolean facingRandom = Math.random() * 10 > 5;

                if (facingRandom) tardis.setDestinationPosition(tardis.getDestinationExteriorPosition().east((int) Math.round(Math.random() * 10 * tardis.getXYZStep())));
                else tardis.setDestinationPosition(tardis.getDestinationExteriorPosition().west((int) Math.round(Math.random() * 10 * tardis.getXYZStep())));

                if (facingRandom) tardis.setDestinationPosition(tardis.getDestinationExteriorPosition().south((int) Math.round(Math.random() * 10 * tardis.getXYZStep())));
                else tardis.setDestinationPosition(tardis.getDestinationExteriorPosition().north((int) Math.round(Math.random() * 10 * tardis.getXYZStep())));

                yield true;
            }

            case DIM_PREV, DIM_NEXT -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) yield false;

                int dim = (int) value;
                if (dim == 0) yield false;

                List<RegistryKey<World>> worldKeys = researchSystem.getAvailableDimensions();
                if (worldKeys.isEmpty()) yield false;

                int index = worldKeys.contains(tardis.getDestinationExteriorDimension()) ? worldKeys.indexOf(tardis.getDestinationExteriorDimension()) : 0;
                index = index + (controlRole == TardisConsoleUnitControlRole.DIM_PREV ? -1 : 1);
                index %= worldKeys.size();
                index = index < 0 ? worldKeys.size() - 1 : index;

                tardis.setDestinationDimension(worldKeys.get(index));
                yield true;
            }

            case RESET_TO_PREV -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) yield false;

                int resetToPrev = (int) value;
                if (resetToPrev == 0) yield false;

                tardis.setDestinationDimension(tardis.getPreviousExteriorDimension());
                tardis.setDestinationFacing(tardis.getPreviousExteriorFacing());
                tardis.setDestinationPosition(tardis.getPreviousExteriorPosition());
                yield true;
            }

            case RESET_TO_CURR -> {
                if (!flightSystem.isEnabled() || flightSystem.inProgress()) yield false;

                int resetToCurr = (int) value;
                if (resetToCurr == 0) yield false;

                tardis.setDestinationDimension(tardis.getCurrentExteriorDimension());
                tardis.setDestinationFacing(tardis.getCurrentExteriorFacing());
                tardis.setDestinationPosition(tardis.getCurrentExteriorPosition());
                yield true;
            }

            case FUEL_HARVESTING -> {
                if (flightSystem.inProgress()) {
                    this.values.put(TardisConsoleUnitControlRole.FUEL_HARVESTING, tardis.isFuelHarvesting());
                    yield false;
                }

                tardis.setFuelHarvesting((boolean) value);
                yield true;
            }

            case ENERGY_HARVESTING -> {
                if (flightSystem.inProgress()) {
                    this.values.put(TardisConsoleUnitControlRole.ENERGY_HARVESTING, tardis.isEnergyHarvesting());
                    yield false;
                }

                tardis.setEnergyHarvesting((boolean) value);
                yield true;
            }

            case DOORS -> {
                if (!materializationSystem.isMaterialized() || !tardis.setDoorsOpenState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.DOORS, tardis.isDoorsOpened());
                    yield false;
                }

                yield true;
            }

            case LIGHT -> {
                if (!materializationSystem.isMaterialized() || !tardis.setLightState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.FUEL_HARVESTING, tardis.isFuelHarvesting());
                    yield false;
                }

                yield true;
            }

            case SHIELDS -> {
                if (!materializationSystem.isMaterialized() || !tardis.setShieldsState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.FUEL_HARVESTING, tardis.isFuelHarvesting());
                    yield false;
                }

                yield true;
            }

            case SHIELDS_OXYGEN -> {
                if (!shieldsSystem.inProgress() || !tardis.setShieldsOxygenState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.SHIELDS_OXYGEN, tardis.isShieldsOxygenEnabled());
                    yield false;
                }

                yield true;
            }

            case SHIELDS_FIRE_PROOF -> {
                if (!shieldsSystem.inProgress() || !tardis.setShieldsFireProofState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.SHIELDS_FIRE_PROOF, tardis.isShieldsFireProofEnabled());
                    yield false;
                }

                yield true;
            }

            case SHIELDS_MEDICAL -> {
                if (!shieldsSystem.inProgress() || !tardis.setShieldsMedicalState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.SHIELDS_MEDICAL, tardis.isShieldsMedicalEnabled());
                    yield false;
                }

                yield true;
            }

            case SHIELDS_MINING -> {
                if (!shieldsSystem.inProgress() || !tardis.setShieldsMiningState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.SHIELDS_MINING, tardis.isShieldsMiningEnabled());
                    yield false;
                }

                yield true;
            }

            case SHIELDS_GRAVITATION -> {
                if (!shieldsSystem.inProgress() || !tardis.setShieldsGravitationState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.SHIELDS_GRAVITATION, tardis.isShieldsGravitationEnabled());
                    yield false;
                }

                yield true;
            }

            case SHIELDS_SPECIAL -> {
                if (!shieldsSystem.inProgress() || !tardis.setShieldsSpecialState((boolean) value)) {
                    this.values.put(TardisConsoleUnitControlRole.SHIELDS_SPECIAL, tardis.isShieldsSpecialEnabled());
                    yield false;
                }

                yield true;
            }

            default -> false;
        };

        this.displayNotification(tardis, controlRole, player, isUpdated);
        if (isUpdated) tardis.markConsoleTilesUpdated();
        return isUpdated;
    }

    public void displayNotification(TardisStateManager tardis, TardisConsoleUnitControlRole controlRole, PlayerEntity player, boolean needSound) {
        Object value = this.get(controlRole);
        String message = controlRole.message == null ? null : "message.dwm.tardis.control.role." + controlRole.message;

        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.REQUIRED_MATERIALIZING_SYSTEM) && !materializationSystem.isEnabled()) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.MATERIALIZATION_SYSTEM_NOT_INSTALLED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM) && !flightSystem.isEnabled()) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.FLIGHT_SYSTEM_NOT_INSTALLED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM) && !shieldsSystem.isEnabled()) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.SHIELDS_SYSTEM_NOT_INSTALLED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED) && !materializationSystem.isMaterialized()) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.MUST_BE_LANDED) && flightSystem.inProgress()) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.DEPENDS_ON_OWNER) && !tardis.checkAccess(player, true, false)) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON) && !tardis.isShieldsEnabled()) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.SHIELDS_SYSTEM_NOT_ACTIVE, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.DEPENDS_ON_HANDBRAKE_OFF) && tardis.isHandbrakeLocked()) {
            if (needSound) ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_HANDBRAKE_ACTIVATED, true);
            return;
        }

        Text component = switch (controlRole) {
            case DOORS, LIGHT, SHIELDS, SHIELDS_OXYGEN, SHIELDS_FIRE_PROOF, SHIELDS_MEDICAL, SHIELDS_MINING, SHIELDS_GRAVITATION, SHIELDS_SPECIAL, FUEL_HARVESTING, ENERGY_HARVESTING, HANDBRAKE -> Text.translatable(message + ((boolean) value ? ".active" : ".inactive"));
            case DIM_PREV, DIM_NEXT -> Text.translatable(message, "§e" + tardis.getDestinationExteriorDimension().getValue().getPath().replace("_", " ").toUpperCase());
            case FACING -> Text.translatable(message, Text.translatable(message + "." + (tardis.getDestinationExteriorFacing().ordinal() - 2)));
            case XSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getX());
            case YSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getY());
            case ZSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getZ());
            case XYZSTEP -> Text.translatable(message, "§e" + tardis.getXYZStep());
            case VERTICAL_SCANNING -> Text.translatable(message, Text.translatable(message + "." + value));
            case STARTER -> tardis.getFuelAmount() <= 0 && tardis.getEnergyAmount() <= 0 ? DWM.TEXTS.TARDIS_NOT_ENOUGH_FUEL : null;

            default -> message == null ? null : Text.translatable(message, value);
        };

        if (needSound) controlRole.playSound(tardis.getWorld(), tardis.getMainConsolePosition());
        if (component != null) player.sendMessage(component, true);
    }

    private boolean getUpdatedBoolean(TardisConsoleUnitControlRole controlRole, Hand hand, Object initialValue) {
        return !(boolean) initialValue;
    }

    private boolean getUpdatedBooleanDirect(TardisConsoleUnitControlRole controlRole, Hand hand, Object initialValue) {
        return hand == Hand.MAIN_HAND;
    }

    private int getUpdatedNumber(TardisConsoleUnitControlRole controlRole, Hand hand, Object initialValue) {
        return ((int) initialValue + 1) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirect(TardisConsoleUnitControlRole controlRole, Hand hand, Object initialValue) {
        return ((int) initialValue + (hand == Hand.MAIN_HAND ? 1 : -1)) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirectLimited(TardisConsoleUnitControlRole controlRole, Hand hand, Object initialValue) {
        return Math.max(0, Math.min((int) initialValue + (hand == Hand.MAIN_HAND ? 1 : -1), controlRole.maxIntValue - 1));
    }

    private int getUpdatedAnimation(TardisConsoleUnitControlRole controlRole, Hand hand, Object initialValue) {
        int value = (int) initialValue;
        if (value == 0) return controlRole.maxIntValue;
        return value;
    }

    private int getUpdatedAnimationDirect(TardisConsoleUnitControlRole controlRole, Hand hand, Object initialValue) {
        int value = (int) initialValue;
        if (value == 0) return controlRole.maxIntValue * (hand == Hand.MAIN_HAND ? 1 : -1);
        return value;
    }
}
