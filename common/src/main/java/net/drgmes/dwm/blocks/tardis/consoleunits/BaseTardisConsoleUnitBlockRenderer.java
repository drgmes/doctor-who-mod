package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlEntry;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRoleType;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.compat.ModCompats;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public abstract class BaseTardisConsoleUnitBlockRenderer<C extends BaseTardisConsoleUnitBlockEntity> implements BlockEntityRenderer<C> {
    protected final BlockEntityRendererFactory.Context ctx;

    protected int SCREEN_SIZE = 239;
    protected int SCREEN_PARAM_SUBSTRING = 16;

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

    protected void printStringsToScreen(MatrixStack matrixStack, VertexConsumerProvider buffer, String[] lines) {
        TextRenderer textRenderer = this.ctx.getTextRenderer();

        for (int i = 0; i < lines.length; i++) {
            textRenderer.draw(
                lines[i],
                0,
                i * textRenderer.fontHeight,
                0xFFFFFF,
                true,
                matrixStack.peek().getPositionMatrix(),
                buffer,
                TextRenderer.TextLayerType.NORMAL,
                0,
                240
            );
        }
    }

    protected void renderScreen(BaseTardisConsoleUnitBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer) {
        if (!tile.tardisStateManager.isValid() || tile.tardisStateManager.isBroken()) return;

        switch (tile.monitorPage) {
            case 1 -> this.renderScreenPage2(tile, matrixStack, buffer, tile.tardisStateManager);
            default -> this.renderScreenPage1(tile, matrixStack, buffer, tile.tardisStateManager);
        }
    }

    private void renderScreenPage1(BaseTardisConsoleUnitBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, TardisStateManager tardis) {
        String NONE = "-";

        String flight = DWM.TEXTS.MONITOR_STATE_NO.getString();
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        if (flightSystem.inProgress()) flight = flightSystem.getProgressPercent() + "%";

        String materialized = DWM.TEXTS.MONITOR_STATE_YES.getString();
        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        if (materializationSystem.inProgress()) materialized = materializationSystem.getProgressPercent() + "%";
        else if (!materializationSystem.isMaterialized()) materialized = DWM.TEXTS.MONITOR_STATE_NO.getString();

        BlockPos prevExteriorPosition = tardis.getPreviousExteriorPosition();
        BlockPos currExteriorPosition = tardis.getCurrentExteriorPosition();
        BlockPos destExteriorPosition = tardis.getDestinationExteriorPosition();
        String posPrevName = prevExteriorPosition.getX() + " " + prevExteriorPosition.getY() + " " + prevExteriorPosition.getZ();
        String posCurrName = currExteriorPosition.getX() + " " + currExteriorPosition.getY() + " " + currExteriorPosition.getZ();
        String posDestName = destExteriorPosition.getX() + " " + destExteriorPosition.getY() + " " + destExteriorPosition.getZ();
        String facingPrevName = tardis.getPreviousExteriorFacing().name().toUpperCase();
        String facingCurrName = tardis.getCurrentExteriorFacing().name().toUpperCase();
        String facingDestName = tardis.getDestinationExteriorFacing().name().toUpperCase();
        String dimPrevName = tardis.getPreviousExteriorDimension().getValue().getPath().toUpperCase();
        String dimCurrName = tardis.getCurrentExteriorDimension().getValue().getPath().toUpperCase();
        String dimDestName = tardis.getDestinationExteriorDimension().getValue().getPath().toUpperCase();

        this.printStringsToScreen(matrixStack, buffer, new String[]{
            this.buildScreenParamText("flight", !flightSystem.isEnabled() ? NONE : flight),
            this.buildScreenParamText("materialized", !materializationSystem.isEnabled() ? NONE : materialized),
            "",
            this.buildScreenParamText("position.prev", !flightSystem.isEnabled() ? NONE : posPrevName),
            this.buildScreenParamText("position.curr", !flightSystem.isEnabled() ? NONE : posCurrName),
            this.buildScreenParamText("position.dest", !flightSystem.isEnabled() ? NONE : posDestName),
            "",
            this.buildScreenParamText("facing.prev", !flightSystem.isEnabled() ? NONE : facingPrevName),
            this.buildScreenParamText("facing.curr", !flightSystem.isEnabled() ? NONE : facingCurrName),
            this.buildScreenParamText("facing.dest", !flightSystem.isEnabled() ? NONE : facingDestName),
            "",
            this.buildScreenParamText("dimension.prev", !flightSystem.isEnabled() ? NONE : dimPrevName.replace("_", " ")),
            this.buildScreenParamText("dimension.curr", !flightSystem.isEnabled() ? NONE : dimCurrName.replace("_", " ")),
            this.buildScreenParamText("dimension.dest", !flightSystem.isEnabled() ? NONE : dimDestName.replace("_", " "))
        });
    }

    private void renderScreenPage2(BaseTardisConsoleUnitBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, TardisStateManager tardis) {
        String NONE = "-";

        boolean isShieldsEnabled = tardis.getSystem(TardisSystemShields.class).isEnabled();
        String shieldsState = tardis.isShieldsEnabled() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String shieldsOxygenState = tardis.isShieldsOxygenEnabled() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String shieldsFireProofState = tardis.isShieldsFireProofEnabled() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String shieldsMedicalState = tardis.isShieldsMedicalEnabled() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String shieldsMiningState = tardis.isShieldsMiningEnabled() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String shieldsGravitationState = tardis.isShieldsGravitationEnabled() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String shieldsSpecialState = tardis.isShieldsSpecialEnabled() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String fuelHarvestingState = tardis.isFuelHarvesting() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();
        String energyHarvestingState = tardis.isEnergyHarvesting() ? DWM.TEXTS.MONITOR_STATE_ON.getString() : DWM.TEXTS.MONITOR_STATE_OFF.getString();

        int fuelAmount = tardis.getFuelAmount();
        String fuelAmountText = String.valueOf(fuelAmount > 1000 ? String.format("%.1f", (float) fuelAmount / 1000) : fuelAmount);
        fuelAmountText += (fuelAmount > 1000 ? "k" : "");
        fuelAmountText += " AE";
        fuelAmountText = fuelAmountText.replace(",", ".");

        int energyAmount = tardis.getEnergyAmount();
        String energyAmountText = String.valueOf(energyAmount > 1000 ? String.format("%.1f", (float) energyAmount / 1000) : energyAmount);
        energyAmountText += (energyAmount > 1000 ? "k" : "");
        energyAmountText += " E";
        energyAmountText = energyAmountText.replace(",", ".");

        this.printStringsToScreen(matrixStack, buffer, new String[]{
            this.buildScreenParamText("shields", !isShieldsEnabled ? NONE : shieldsState),
            this.buildScreenParamText("shields_oxygen", !isShieldsEnabled ? NONE : shieldsOxygenState),
            this.buildScreenParamText("shields_fire_proof", !isShieldsEnabled ? NONE : shieldsFireProofState),
            this.buildScreenParamText("shields_medical", !isShieldsEnabled ? NONE : shieldsMedicalState),
            this.buildScreenParamText("shields_mining", !isShieldsEnabled ? NONE : shieldsMiningState),
            this.buildScreenParamText("shields_gravitation", !isShieldsEnabled ? NONE : shieldsGravitationState),
            this.buildScreenParamText("shields_special", !isShieldsEnabled ? NONE : shieldsSpecialState),
            "",
            this.buildScreenParamText("fuel_harvesting", fuelHarvestingState),
            this.buildScreenParamText("energy_harvesting", energyHarvestingState),
            "",
            this.buildScreenParamText("fuel", fuelAmountText),
            ModCompats.techReborn() ? this.buildScreenParamText("energy", energyAmountText) : "",
        });
    }

    private String buildScreenParamText(String title, String appendInput) {
        TextRenderer textRenderer = this.ctx.getTextRenderer();

        String append = appendInput.substring(0, Math.min(SCREEN_PARAM_SUBSTRING, appendInput.length()));
        append += appendInput.length() > append.length() ? "..." : "";

        String prepend = Text.translatable("title." + DWM.MODID + ".monitor.state." + title).getString() + ": ";
        return prepend + " ".repeat((SCREEN_SIZE - textRenderer.getWidth(prepend + append)) / textRenderer.getWidth(" ")) + append;
    }

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
