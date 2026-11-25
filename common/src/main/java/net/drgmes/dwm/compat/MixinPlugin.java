package net.drgmes.dwm.compat;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE = "net.drgmes.dwm.mixin.";

    private static final Map<String, String> CONDITIONAL_MIXINS = Map.of(
        MIXIN_PACKAGE + "MixinPortal", "qouteall.imm_ptl.core.portal.Portal",
        MIXIN_PACKAGE + "MixinServerTeleportationManager", "qouteall.imm_ptl.core.teleportation.ServerTeleportationManager"
    );

    private final Map<String, Boolean> classPresenceCache = new HashMap<>();

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String requiredClass = CONDITIONAL_MIXINS.get(mixinClassName);
        return requiredClass == null || this.isClassPresent(requiredClass);
    }

    private boolean isClassPresent(String className) {
        return this.classPresenceCache.computeIfAbsent(className, (name) -> {
            try {
                MixinService.getService().getBytecodeProvider().getClassNode(name.replace('.', '/'));
                return true;
            } catch (Throwable e) {
                return false;
            }
        });
    }
}
