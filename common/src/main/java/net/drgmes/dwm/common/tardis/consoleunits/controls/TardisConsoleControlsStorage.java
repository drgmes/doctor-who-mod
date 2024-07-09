package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.enums.TardisConsoleUnitControlRole;
import net.drgmes.dwm.enums.TardisConsoleUnitControlValueType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
            Object value = null;

            if (controlRole.type == TardisConsoleUnitControlValueType.BOOLEAN) value = tag.getBoolean(key);
            else if (controlRole.type == TardisConsoleUnitControlValueType.BOOLEAN_DIRECT) value = tag.getBoolean(key);
            else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER) value = tag.getInt(key);
            else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER_DIRECT) value = tag.getInt(key);
            else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER_DIRECT_LIMITED) value = tag.getInt(key);
            else if (controlRole.type == TardisConsoleUnitControlValueType.ANIMATION) value = tag.getInt(key);
            else if (controlRole.type == TardisConsoleUnitControlValueType.ANIMATION_DIRECT) value = tag.getInt(key);

            this.values.put(controlRole, value);
        }
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        for (Entry<TardisConsoleUnitControlRole, Object> entry : this.values.entrySet()) {
            String name = entry.getKey().name();
            Object value = entry.getValue();

            if (entry.getKey().type == TardisConsoleUnitControlValueType.BOOLEAN) tag.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == TardisConsoleUnitControlValueType.BOOLEAN_DIRECT) tag.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == TardisConsoleUnitControlValueType.NUMBER) tag.putInt(name, (int) value);
            else if (entry.getKey().type == TardisConsoleUnitControlValueType.NUMBER_DIRECT) tag.putInt(name, (int) value);
            else if (entry.getKey().type == TardisConsoleUnitControlValueType.NUMBER_DIRECT_LIMITED) tag.putInt(name, (int) value);
            else if (entry.getKey().type == TardisConsoleUnitControlValueType.ANIMATION) tag.putInt(name, (int) value);
            else if (entry.getKey().type == TardisConsoleUnitControlValueType.ANIMATION_DIRECT) tag.putInt(name, (int) value);
        }

        return tag;
    }

    public Object get(TardisConsoleUnitControlRole controlRole) {
        if (this.values.containsKey(controlRole)) {
            return this.values.get(controlRole);
        }

        Object value = null;
        if (controlRole.type == TardisConsoleUnitControlValueType.BOOLEAN) value = false;
        else if (controlRole.type == TardisConsoleUnitControlValueType.BOOLEAN_DIRECT) value = false;
        else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER) value = 0;
        else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER_DIRECT) value = 0;
        else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER_DIRECT_LIMITED) value = 0;
        else if (controlRole.type == TardisConsoleUnitControlValueType.ANIMATION) value = 0;
        else if (controlRole.type == TardisConsoleUnitControlValueType.ANIMATION_DIRECT) value = 0;

        return this.values.put(controlRole, value);
    }

    public boolean update(TardisConsoleUnitControlRole controlRole, Hand hand) {
        Object value = this.get(controlRole);

        if (controlRole.type == TardisConsoleUnitControlValueType.BOOLEAN) value = this.getUpdatedBoolean(controlRole, hand);
        else if (controlRole.type == TardisConsoleUnitControlValueType.BOOLEAN_DIRECT) value = this.getUpdatedBooleanDirect(controlRole, hand);
        else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER) value = this.getUpdatedNumber(controlRole, hand);
        else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER_DIRECT) value = this.getUpdatedNumberDirect(controlRole, hand);
        else if (controlRole.type == TardisConsoleUnitControlValueType.NUMBER_DIRECT_LIMITED) value = this.getUpdatedNumberDirectLimited(controlRole, hand);
        else if (controlRole.type == TardisConsoleUnitControlValueType.ANIMATION) value = this.getUpdatedAnimation(controlRole, hand);
        else if (controlRole.type == TardisConsoleUnitControlValueType.ANIMATION_DIRECT) value = this.getUpdatedAnimationDirect(controlRole, hand);

        this.values.put(controlRole, value);
        return true;
    }

    public void applyData(TardisStateManager tardis) {
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        this.values.put(TardisConsoleUnitControlRole.STARTER, flightSystem.inProgress());
        this.values.put(TardisConsoleUnitControlRole.MATERIALIZATION, materializationSystem.isMaterialized());
        this.values.put(TardisConsoleUnitControlRole.VERTICAL_SCANNING, materializationSystem.verticalScanning.ordinal());
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
            default -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
        });
    }

    private boolean getUpdatedBoolean(TardisConsoleUnitControlRole controlRole, Hand hand) {
        return !(boolean) this.get(controlRole);
    }

    private boolean getUpdatedBooleanDirect(TardisConsoleUnitControlRole controlRole, Hand hand) {
        return hand == Hand.MAIN_HAND;
    }

    private int getUpdatedNumber(TardisConsoleUnitControlRole controlRole, Hand hand) {
        return ((int) this.get(controlRole) + 1) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirect(TardisConsoleUnitControlRole controlRole, Hand hand) {
        return ((int) this.get(controlRole) + (hand == Hand.MAIN_HAND ? 1 : -1)) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirectLimited(TardisConsoleUnitControlRole controlRole, Hand hand) {
        return Math.max(0, Math.min((int) this.get(controlRole) + (hand == Hand.MAIN_HAND ? 1 : -1), controlRole.maxIntValue - 1));
    }

    private int getUpdatedAnimation(TardisConsoleUnitControlRole controlRole, Hand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue;
        return value;
    }

    private int getUpdatedAnimationDirect(TardisConsoleUnitControlRole controlRole, Hand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue * (hand == Hand.MAIN_HAND ? 1 : -1);
        return value;
    }
}
