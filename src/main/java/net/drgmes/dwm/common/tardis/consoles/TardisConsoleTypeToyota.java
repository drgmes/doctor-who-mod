package net.drgmes.dwm.common.tardis.consoles;

import net.drgmes.dwm.common.tardis.TardisConsoleType;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleTypeToyota extends TardisConsoleType {
    public TardisConsoleTypeToyota() {
        this.addControl(TardisConsoleControlRoles.STARTER, new Vec3(0.75F, 0.5F, -0.345F));
        this.addControl(TardisConsoleControlRoles.HANDBRAKE, new Vec3(0.75F, 0.5F, 0.345F));

        this.addControl(TardisConsoleControlRoles.SHIELDS, new Vec3(-0.725F, 0.5F, -0.345F));
        this.addControl(TardisConsoleControlRoles.DOORS, new Vec3(-0.725F, 0.5F, 0.345F));

        this.addControl(TardisConsoleControlRoles.RANDOMIZER, new Vec3(-0.4545F, 0.5F, 0.7225F));
        this.addControl(TardisConsoleControlRoles.FACING, new Vec3(0.4545F, 0.5F, 0.7225F));
    }
}
