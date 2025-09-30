package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorHistoryClearPacket;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorHistoryClearConfirmationScreen extends BaseTardisConsoleUnitMonitorScreen {
    private final Screen parentScreen;
    private final String tardisId;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    public TardisConsoleUnitMonitorHistoryClearConfirmationScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_HISTORY_TITLE, tardisConsoleUnitBlockEntity);

        this.parentScreen = parentScreen;
        this.tardisId = tardisId;
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + 1, BUTTON_SIZE + 1);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_HISTORY_CLEAR_CONFIRMATION_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = acceptButtonPos.add(-BUTTON_SIZE - 1, 0);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_HISTORY_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.cancelButton);
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);
        this.renderConfirmationMessage(context);
    }

    @Override
    public void apply() {
        new TardisConsoleUnitMonitorHistoryClearPacket(this.tardisId).sendToServer();
        super.apply();
    }

    @Override
    public void back() {
        if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
        else super.back();
    }

    private void renderConfirmationMessage(DrawContext context) {
        int maxWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2;
        int textX = (int) Math.floor(this.getBackgroundSize().x / 2F);
        int textY = (int) Math.floor(this.getBackgroundSize().y / 2F) - BUTTON_SIZE;
        Vector2i textPos = this.getRenderPos(textX, textY);

        RenderHelper.drawTextMultilineCentered(DWM.TEXTS.MONITOR_HISTORY_CLEAR_CONFIRMATION_TEXT, this.textRenderer, context, textPos, this.textRenderer.fontHeight, maxWidth, 0xE0E0E0);
    }
}
