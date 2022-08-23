package net.drgmes.dwm.types;

public interface IMixinPortal {
    String getTardisId();
    void setTardisId(String tardisId);

    IMixinPortal markAsTardisEntrance();
    IMixinPortal markAsTardisRoomsEntrance();
}
