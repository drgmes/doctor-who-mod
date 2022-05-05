package net.drgmes.dwm.entities.tardis.consoles.renderers;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BaseTardisConsoleBlockRenderer implements BlockEntityRenderer<BaseTardisConsoleBlockEntity> {
    protected BlockEntityRendererProvider.Context ctx;

    public BaseTardisConsoleBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    protected void animate(BaseTardisConsoleBlockEntity tile, ModelPart modelRoot, float partialTicks) {
        try {
            for (TardisConsoleControlRoles controlRole : TardisConsoleControlRoles.values()) {
                if (!tile.consoleType.controlEntries.containsKey(controlRole)) continue;

                TardisConsoleControlEntry controlEntry = tile.consoleType.controlEntries.get(controlRole);
                ModelPart model = this.getModelPart(modelRoot, controlEntry.modelPath);
                Object value = tile.controlsStorage.get(controlRole);

                if (controlRole == TardisConsoleControlRoles.STARTER && (boolean) value) this.activateStarter(model, controlRole);
                else if (controlRole == TardisConsoleControlRoles.HANDBRAKE && (boolean) value) this.activateHandbrake(model, controlRole);

                else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION) {
                    if (controlEntry.type == TardisConsoleControlEntryTypes.LEVER) this.animateLever(model, (int) value, controlRole, partialTicks);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.BUTTON) this.animateButton(model, (int) value, controlRole, partialTicks);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.SLIDER) this.animateSlider(model, (int) value, controlRole, partialTicks);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR) this.animateRotator(model, controlRole.maxIntValue - (int) value, controlRole, partialTicks);
                }

                else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION_DIRECT) {
                    if (controlEntry.type == TardisConsoleControlEntryTypes.LEVER) this.animateLever(model, (int) value, controlRole, partialTicks);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.BUTTON) this.animateButton(model, (int) value, controlRole, partialTicks);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.SLIDER) this.animateSlider(model, (int) value, controlRole, partialTicks);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR) this.animateRotator(model, controlRole.maxIntValue + (int) value, controlRole, partialTicks);
                }
    
                else if (controlEntry.type == TardisConsoleControlEntryTypes.LEVER && value instanceof Boolean) this.activateLever(model, (boolean) value, controlRole, partialTicks);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.LEVER && value instanceof Integer) this.activateLever(model, (int) value, controlRole, partialTicks);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.BUTTON && value instanceof Boolean) this.activateButton(model, (boolean) value, controlRole, partialTicks);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.BUTTON && value instanceof Integer) this.activateButton(model, (int) value, controlRole, partialTicks);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.SLIDER && value instanceof Boolean) this.activateSlider(model, (boolean) value, controlRole, partialTicks);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.SLIDER && value instanceof Integer) this.activateSlider(model, (int) value, controlRole, partialTicks);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR) this.activateRotator(model, (int) value, controlRole, partialTicks);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    protected abstract void activateLever(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks);
    protected abstract void activateLever(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);
    protected abstract void animateLever(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);

    protected abstract void activateButton(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks);
    protected abstract void activateButton(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);
    protected abstract void animateButton(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);

    protected abstract void activateSlider(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks);
    protected abstract void activateSlider(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);
    protected abstract void animateSlider(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);

    protected abstract void activateRotator(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);
    protected abstract void animateRotator(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks);

    protected abstract void activateHandbrake(ModelPart model, TardisConsoleControlRoles controlRole);
    protected abstract void activateStarter(ModelPart model, TardisConsoleControlRoles controlRole);

    private ModelPart getModelPart(ModelPart modelRoot, String path) {
        ModelPart model = modelRoot;

        for (String modelName : path.split("/")) {
            String prevModelName = "";
            for (String modelNamePart : modelName.split("\\$")) {
                prevModelName = prevModelName + modelNamePart;
                model = model.getChild(prevModelName);
            }
        }

        return model;
    }
}
