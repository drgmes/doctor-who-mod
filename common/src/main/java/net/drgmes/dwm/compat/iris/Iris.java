package net.drgmes.dwm.compat.iris;

import net.irisshaders.iris.api.v0.IrisApi;

public class Iris {
    public static boolean isShaderPackInUse() {
        return IrisApi.getInstance().isShaderPackInUse();
    }
}
