package net.drgmes.dwm.common.tardis.consoles.controls;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TardisConsoleControlsStorage {
    public final Map<ETardisConsoleControlRole, Object> values = new HashMap<>();

    public TardisConsoleControlsStorage() {
        for (ETardisConsoleControlRole controlRole : ETardisConsoleControlRole.values()) {
            this.get(controlRole);
        }
    }

    public NbtCompound save(NbtCompound tag) {
        NbtCompound controls = new NbtCompound();

        for (Entry<ETardisConsoleControlRole, Object> entry : this.values.entrySet()) {
            String name = entry.getKey().name();
            Object value = entry.getValue();

            if (entry.getKey().type == ETardisConsoleControlRoleType.BOOLEAN) controls.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == ETardisConsoleControlRoleType.BOOLEAN_DIRECT) controls.putBoolean(name, (boolean) value);
            else if (entry.getKey().type == ETardisConsoleControlRoleType.NUMBER) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleControlRoleType.NUMBER_DIRECT) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleControlRoleType.NUMBER_DIRECT_BLOCK) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleControlRoleType.ANIMATION) controls.putInt(name, (int) value);
            else if (entry.getKey().type == ETardisConsoleControlRoleType.ANIMATION_DIRECT) controls.putInt(name, (int) value);
        }

        tag.put("controls", controls);
        return tag;
    }

    public void load(NbtCompound tag) {
        NbtCompound controls = tag.getCompound("controls");

        for (String key : controls.getKeys()) {
            ETardisConsoleControlRole controlRole = ETardisConsoleControlRole.valueOf(key);
            Object value = null;

            if (controlRole.type == ETardisConsoleControlRoleType.BOOLEAN) value = controls.getBoolean(key);
            else if (controlRole.type == ETardisConsoleControlRoleType.BOOLEAN_DIRECT) value = controls.getBoolean(key);
            else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER_DIRECT) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER_DIRECT_BLOCK) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleControlRoleType.ANIMATION) value = controls.getInt(key);
            else if (controlRole.type == ETardisConsoleControlRoleType.ANIMATION_DIRECT) value = controls.getInt(key);

            this.values.put(controlRole, value);
        }
    }

    public Object get(ETardisConsoleControlRole controlRole) {
        if (this.values.containsKey(controlRole)) {
            return this.values.get(controlRole);
        }

        Object value = null;
        if (controlRole.type == ETardisConsoleControlRoleType.BOOLEAN) value = false;
        else if (controlRole.type == ETardisConsoleControlRoleType.BOOLEAN_DIRECT) value = false;
        else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER) value = 0;
        else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER_DIRECT) value = 0;
        else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER_DIRECT_BLOCK) value = 0;
        else if (controlRole.type == ETardisConsoleControlRoleType.ANIMATION) value = 0;
        else if (controlRole.type == ETardisConsoleControlRoleType.ANIMATION_DIRECT) value = 0;

        return this.values.put(controlRole, value);
    }

    public boolean update(ETardisConsoleControlRole controlRole, Hand hand) {
        Object value = this.get(controlRole);

        if (controlRole.type == ETardisConsoleControlRoleType.BOOLEAN) value = this.getUpdatedBoolean(controlRole, hand);
        else if (controlRole.type == ETardisConsoleControlRoleType.BOOLEAN_DIRECT) value = this.getUpdatedBooleanDirect(controlRole, hand);
        else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER) value = this.getUpdatedNumber(controlRole, hand);
        else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER_DIRECT) value = this.getUpdatedNumberDirect(controlRole, hand);
        else if (controlRole.type == ETardisConsoleControlRoleType.NUMBER_DIRECT_BLOCK) value = this.getUpdatedNumberDirectBlock(controlRole, hand);
        else if (controlRole.type == ETardisConsoleControlRoleType.ANIMATION) value = this.getUpdatedAnimation(controlRole, hand);
        else if (controlRole.type == ETardisConsoleControlRoleType.ANIMATION_DIRECT) value = this.getUpdatedAnimationDirect(controlRole, hand);

        this.values.put(controlRole, value);
        return true;
    }

    private boolean getUpdatedBoolean(ETardisConsoleControlRole controlRole, Hand hand) {
        return !(boolean) this.get(controlRole);
    }

    private boolean getUpdatedBooleanDirect(ETardisConsoleControlRole controlRole, Hand hand) {
        return hand == Hand.MAIN_HAND;
    }

    private int getUpdatedNumber(ETardisConsoleControlRole controlRole, Hand hand) {
        return ((int) this.get(controlRole) + 1) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirect(ETardisConsoleControlRole controlRole, Hand hand) {
        return ((int) this.get(controlRole) + (hand == Hand.MAIN_HAND ? 1 : -1)) % controlRole.maxIntValue;
    }

    private int getUpdatedNumberDirectBlock(ETardisConsoleControlRole controlRole, Hand hand) {
        return Math.max(0, Math.min((int) this.get(controlRole) + (hand == Hand.MAIN_HAND ? 1 : -1), controlRole.maxIntValue - 1));
    }

    private int getUpdatedAnimation(ETardisConsoleControlRole controlRole, Hand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue;
        return value;
    }

    private int getUpdatedAnimationDirect(ETardisConsoleControlRole controlRole, Hand hand) {
        int value = (int) this.get(controlRole);
        if (value == 0) return controlRole.maxIntValue * (hand == Hand.MAIN_HAND ? 1 : -1);
        return value;
    }
}
