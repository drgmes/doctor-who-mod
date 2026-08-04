package net.drgmes.dwm.compat.immersiveportals;

import net.drgmes.dwm.compat.iris.Iris;
import net.drgmes.dwm.setup.ModCompats;
import qouteall.imm_ptl.core.render.context_management.PortalRendering;

/**
 * Iris breaks Immersive Portals clipping, so the exterior TARDIS mesh can show through the
 * portal. Hide that mesh while IP is rendering through a portal with shaders active.
 */
public final class ImmersivePortalsIrisCompat {
    private ImmersivePortalsIrisCompat() {
    }

    public static boolean shouldHideExteriorShellForPortalView() {
        if (!ModCompats.immersivePortals() || !ModCompats.iris()) return false;
        if (!Iris.isShaderPackInUse()) return false;
        return PortalRendering.isRendering();
    }
}
