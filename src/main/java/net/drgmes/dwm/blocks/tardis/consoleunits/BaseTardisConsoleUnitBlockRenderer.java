package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlEntry;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRoleType;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlEntry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public abstract class BaseTardisConsoleUnitBlockRenderer<C extends BaseTardisConsoleUnitBlockEntity> implements BlockEntityRenderer<C> {
    protected final BlockEntityRendererFactory.Context ctx;

    public BaseTardisConsoleUnitBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    protected void animate(C tile, ModelPart modelRoot, float delta) {
        try {
            for (ETardisConsoleUnitControlRole controlRole : ETardisConsoleUnitControlRole.values()) {
                if (!tile.consoleType.controlEntries.containsKey(controlRole)) continue;

                TardisConsoleControlEntry controlEntry = tile.consoleType.controlEntries.get(controlRole);
                ModelPart model = this.getModelPart(modelRoot, controlEntry.modelPath);
                Object value = tile.controlsStorage.get(controlRole);

                if (controlRole == ETardisConsoleUnitControlRole.STARTER && (boolean) value) this.activateStarter(model, controlRole);
                else if (controlRole == ETardisConsoleUnitControlRole.HANDBRAKE && (boolean) value) this.activateHandbrake(model, controlRole);

                else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION) {
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.LEVER) this.animateLever(model, (int) value, controlRole, delta);
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.BUTTON) this.animateButton(model, (int) value, controlRole, delta);
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.SLIDER) this.animateSlider(model, (int) value, controlRole, delta);
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.ROTATOR) this.animateRotator(model, controlRole.maxIntValue - (int) value, controlRole, delta);
                }
                else if (controlRole.type == ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT) {
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.LEVER) this.animateLever(model, (int) value, controlRole, delta);
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.BUTTON) this.animateButton(model, (int) value, controlRole, delta);
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.SLIDER) this.animateSlider(model, (int) value, controlRole, delta);
                    if (controlEntry.type == ETardisConsoleUnitControlEntry.ROTATOR) this.animateRotator(model, controlRole.maxIntValue + (int) value, controlRole, delta);
                }
                else if (controlEntry.type == ETardisConsoleUnitControlEntry.LEVER && value instanceof Boolean) this.activateLever(model, (boolean) value, controlRole, delta);
                else if (controlEntry.type == ETardisConsoleUnitControlEntry.LEVER && value instanceof Integer) this.activateLever(model, (int) value, controlRole, delta);
                else if (controlEntry.type == ETardisConsoleUnitControlEntry.BUTTON && value instanceof Boolean) this.activateButton(model, (boolean) value, controlRole, delta);
                else if (controlEntry.type == ETardisConsoleUnitControlEntry.BUTTON && value instanceof Integer) this.activateButton(model, (int) value, controlRole, delta);
                else if (controlEntry.type == ETardisConsoleUnitControlEntry.SLIDER && value instanceof Boolean) this.activateSlider(model, (boolean) value, controlRole, delta);
                else if (controlEntry.type == ETardisConsoleUnitControlEntry.SLIDER && value instanceof Integer) this.activateSlider(model, (int) value, controlRole, delta);
                else if (controlEntry.type == ETardisConsoleUnitControlEntry.ROTATOR) this.activateRotator(model, (int) value, controlRole, delta);
            }
        } catch (Exception e) {
            DWM.LOGGER.error("Error in animating ModelPart (" + e.getMessage() + ")");
        }
    }

    protected abstract void activateLever(ModelPart model, boolean value, ETardisConsoleUnitControlRole controlRole, float delta);
    protected abstract void activateLever(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);
    protected abstract void animateLever(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);

    protected abstract void activateButton(ModelPart model, boolean value, ETardisConsoleUnitControlRole controlRole, float delta);
    protected abstract void activateButton(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);
    protected abstract void animateButton(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);

    protected abstract void activateSlider(ModelPart model, boolean value, ETardisConsoleUnitControlRole controlRole, float delta);
    protected abstract void activateSlider(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);
    protected abstract void animateSlider(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);

    protected abstract void activateRotator(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);
    protected abstract void animateRotator(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta);

    protected abstract void activateHandbrake(ModelPart model, ETardisConsoleUnitControlRole controlRole);
    protected abstract void activateStarter(ModelPart model, ETardisConsoleUnitControlRole controlRole);

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
