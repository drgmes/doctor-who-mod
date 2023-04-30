package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TardisConsoleControlsStorage {
    public final Map<ETardisConsoleUnitControlRole, Object> values = new HashMap<>();

    public TardisConsoleControlsStorage() {
        for (ETardisConsoleUnitControlRole controlRole : ETardisConsoleUnitControlRole.values()) {
            this.get(controlRole);
        }
    }

    public NbtCompound save(NbtCompound tag) {
        NbtCompound controls = new NbtCompound();

        for (Entry<ETardisConsoleUnitControlRole, Object> entry : this.values.entrySet()) {
            String name = entry.getKey().name();
            Object value = entry.getValue();

            if (entry.getKey().type == ETardisConsoleUnitControlRoleType.BOOLEAN) controls.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT) controls.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == ETardisConsoleUnitControlRoleType.NUMBER) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleUnitControlRoleType.ANIMATION) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT) controls.putInt(name, (int) value);
        }

        tag.put("controls", controls);
        return tag;
    }

    public void load(NbtCompound tag) {
        NbtCompound controls = tag.getCompound("controls");

        for (String key : controls.getKeys()) {
            ETardisConsoleUnitControlRole controlRole = ETardisConsoleUnitControlRole.valueOf(key);
            Object value = null;

            if (controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN) value = controls.getBoolean(key);
            else if (controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT) value = controls.getBoolean(key);
            else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT) value = controls.getInt(key);

            this.values.put(controlRole, value);
        }
    }

    public Object get(ETardisConsoleUnitControlRole controlRole) {
        if (this.values.containsKey(controlRole)) {
            return this.values.get(controlRole);
        }

        Object value = null;
        if (controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN) value = false;
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT) value = false;
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER) value = 0;
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT) value = 0;
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK) value = 0;
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION) value = 0;
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT) value = 0;

        return this.values.put(controlRole, value);
    }

    public boolean update(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        Object value = this.get(controlRole);

        if (controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN) value = this.getUpdatedBoolean(controlRole, hand);
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT) value = this.getUpdatedBooleanDirect(controlRole, hand);
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER) value = this.getUpdatedNumber(controlRole, hand);
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT) value = this.getUpdatedNumberDirect(controlRole, hand);
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK) value = this.getUpdatedNumberDirectBlock(controlRole, hand);
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION) value = this.getUpdatedAnimation(controlRole, hand);
        else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT) value = this.getUpdatedAnimationDirect(controlRole, hand);

        this.values.put(controlRole, value);
        return true;
    }

    private boolean getUpdatedBoolean(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        return !(boolean) this.get(controlRole);
    }

    private boolean getUpdatedBooleanDirect(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        return hand == Hand.MAIN_HAND;
    }

    private int getUpdatedNumber(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        return ((int) this.get(controlRole) + 1) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirect(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        return ((int) this.get(controlRole) + (hand == Hand.MAIN_HAND ? 1 : -1)) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirectBlock(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        return Math.max(0, Math.min((int) this.get(controlRole) + (hand == Hand.MAIN_HAND ? 1 : -1), controlRole.maxIntValue - 1));
    }

    private int getUpdatedAnimation(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue;
        return value;
    }

    private int getUpdatedAnimationDirect(ETardisConsoleUnitControlRole controlRole, Hand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue * (hand == Hand.MAIN_HAND ? 1 : -1);
        return value;
    }
}
