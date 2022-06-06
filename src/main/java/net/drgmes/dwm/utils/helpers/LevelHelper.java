package net.drgmes.dwm.utils.helpers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class LevelHelper {
    public static Rotation getRotation(Direction direction) {
        return switch (direction) {
            case WEST -> Rotation.CLOCKWISE_90;
            case EAST -> Rotation.COUNTERCLOCKWISE_90;
            case NORTH -> Rotation.CLOCKWISE_180;
            default -> Rotation.NONE;
        };
    }
}
