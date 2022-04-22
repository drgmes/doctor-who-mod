package net.drgmes.dwm.common.tardis.consoles.controls;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.drgmes.dwm.caps.ITardisLevelData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;

public class TardisConsoleControlsStorage {
    public final Map<TardisConsoleControlRoles, Object> values = new HashMap<>();

    public TardisConsoleControlsStorage() {
        for (TardisConsoleControlRoles controlRole : TardisConsoleControlRoles.values()) {
            this.get(controlRole);
        }
    }

    public void save(CompoundTag tag) {
        CompoundTag controls = new CompoundTag();

        for (Entry<TardisConsoleControlRoles, Object> entry : this.values.entrySet()) {
            String name = entry.getKey().name();
            Object value = entry.getValue();

            if (entry.getKey().type == TardisConsoleControlRoleTypes.BOOLEAN) controls.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.BOOLEAN_DIRECT) controls.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.NUMBER) controls.putInt(name, (int) value);
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.NUMBER_DIRECT) controls.putInt(name, (int) value);
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.ANIMATION) controls.putInt(name, (int) value);
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.ANIMATION_DIRECT) controls.putInt(name, (int) value);
        }

        tag.put("controls", controls);
    }

    public void load(CompoundTag tag) {
        CompoundTag controls = tag.getCompound("controls");

        for (String key : controls.getAllKeys()) {
            TardisConsoleControlRoles controlRole = TardisConsoleControlRoles.valueOf(key);
            Object value = null;

            if (controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN) value = controls.getBoolean(key);
            else if (controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN_DIRECT) value = controls.getBoolean(key);
            else if (controlRole.type == TardisConsoleControlRoleTypes.NUMBER) value = controls.getInt(key);
            else if (controlRole.type == TardisConsoleControlRoleTypes.NUMBER_DIRECT) value = controls.getInt(key);
            else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION) value = controls.getInt(key);
            else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION_DIRECT) value = controls.getInt(key);

            this.values.put(controlRole, value);
        }
    }

    public Object get(TardisConsoleControlRoles controlRole) {
        if (this.values.containsKey(controlRole)) {
            return this.values.get(controlRole);
        }

        Object value = null;
        if (controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN) value = false;
        else if (controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN_DIRECT) value = false;
        else if (controlRole.type == TardisConsoleControlRoleTypes.NUMBER) value = 0;
        else if (controlRole.type == TardisConsoleControlRoleTypes.NUMBER_DIRECT) value = 0;
        else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION) value = 0;
        else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION_DIRECT) value = 0;

        return this.values.put(controlRole, value);
    }

    public boolean update(TardisConsoleControlRoles controlRole, InteractionHand hand) {
        Object value = this.get(controlRole);

        if (controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN) value = this.getUpdatedBoolean(controlRole, hand);
        else if (controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN_DIRECT) value = this.getUpdatedBooleanDirect(controlRole, hand);
        else if (controlRole.type == TardisConsoleControlRoleTypes.NUMBER) value = this.getUpdatedNumber(controlRole, hand);
        else if (controlRole.type == TardisConsoleControlRoleTypes.NUMBER_DIRECT) value = this.getUpdatedNumberDirect(controlRole, hand);
        else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION) value = this.getUpdatedAnimation(controlRole, hand);
        else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION_DIRECT) value = this.getUpdatedAnimationDirect(controlRole, hand);

        this.values.put(controlRole, value);
        return true;
    }

    public void applyTardisWorldStorage(ITardisLevelData storage) {
        this.values.put(TardisConsoleControlRoles.DOORS, storage.isDoorsOpened());
        this.values.put(TardisConsoleControlRoles.SHIELDS, storage.isShieldsEnabled());
        this.values.put(TardisConsoleControlRoles.ENERGY_ARTRON_HARVESTING, storage.isEnergyArtronHarvesting());
        this.values.put(TardisConsoleControlRoles.ENERGY_FORGE_HARVESTING, storage.isEnergyForgeHarvesting());
        this.values.put(TardisConsoleControlRoles.FACING, storage.getDestinationExteriorFacing().ordinal() - 2);
    }

    private boolean getUpdatedBoolean(TardisConsoleControlRoles controlRole, InteractionHand hand) {
        return !(boolean) this.get(controlRole);
    }

    private boolean getUpdatedBooleanDirect(TardisConsoleControlRoles controlRole, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND;
    }

    private int getUpdatedNumber(TardisConsoleControlRoles controlRole, InteractionHand hand) {
        return ((int) this.get(controlRole) + 1) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirect(TardisConsoleControlRoles controlRole, InteractionHand hand) {
        return ((int) this.get(controlRole) + (hand == InteractionHand.MAIN_HAND ? 1 : -1)) % controlRole.maxIntValue;
    }

    private int getUpdatedAnimation(TardisConsoleControlRoles controlRole, InteractionHand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue;
        return value;
    }

    private int getUpdatedAnimationDirect(TardisConsoleControlRoles controlRole, InteractionHand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue * (hand == InteractionHand.MAIN_HAND ? 1 : -1);
        return value;
    }
}
