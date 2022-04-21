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

                if (controlRole == TardisConsoleControlRoles.STARTER && (boolean) value) this.activateStarter(model);
                else if (controlRole == TardisConsoleControlRoles.HANDBRAKE && (boolean) value) this.activateHandbrake(model);

                else if (controlRole.type == TardisConsoleControlRoleTypes.ANIMATION) {
                    if (controlEntry.type == TardisConsoleControlEntryTypes.BUTTON) this.animateButton(model, partialTicks, (int) value);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.LEVER) this.animateLever(model, partialTicks, (int) value);
                    if (controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR) this.animateRotator(model, partialTicks, controlRole.maxIntValue - (int) value);
                }
    
                else if (controlEntry.type == TardisConsoleControlEntryTypes.BUTTON && (boolean) value) this.activateButton(model);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.LEVER && (boolean) value) this.activateLever(model);
                else if (controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR) this.activateRotator(model, (int) value);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    protected abstract void activateButton(ModelPart model);

    protected abstract void animateButton(ModelPart model, float partialTicks, int phase);

    protected abstract void activateLever(ModelPart model);

    protected abstract void animateLever(ModelPart model, float partialTicks, int phase);

    protected abstract void activateRotator(ModelPart model, int phase);

    protected abstract void animateRotator(ModelPart model, float partialTicks, int phase);

    protected abstract void activateHandbrake(ModelPart model);

    protected abstract void activateStarter(ModelPart model);

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
