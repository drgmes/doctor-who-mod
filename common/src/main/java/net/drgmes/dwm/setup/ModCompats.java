package net.drgmes.dwm.setup;

public class ModCompats {
    public static boolean clothConfig() {
        try {
            Class.forName("me.shedaniel.autoconfig.AutoConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean modMenu() {
        try {
            Class.forName("com.terraformersmc.modmenu.api.ModMenuApi");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean dimLib() {
        try {
            Class.forName("qouteall.dimlib.api.DimensionAPI");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean immersivePortals() {
        try {
            Class.forName("qouteall.imm_ptl.core.portal.Portal");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean techReborn() {
        try {
            Class.forName("team.reborn.energy.api.base.SimpleEnergyStorage");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean appliedEnergistics() {
        try {
            Class.forName("appeng.api.networking.energy.IEnergyService");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
