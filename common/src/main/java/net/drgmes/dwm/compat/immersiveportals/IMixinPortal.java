package net.drgmes.dwm.compat.immersiveportals;

public interface IMixinPortal {
    String getTardisId();
    void setTardisId(String tardisId);

    IMixinPortal markAsTardisEntrance();
    IMixinPortal markAsTardisRoomsEntrance();
}
