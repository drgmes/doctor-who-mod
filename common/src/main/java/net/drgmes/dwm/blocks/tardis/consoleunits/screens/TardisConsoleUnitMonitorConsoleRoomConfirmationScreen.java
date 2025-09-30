package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorConsoleRoomApplyPacket;
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
public class TardisConsoleUnitMonitorConsoleRoomConfirmationScreen extends BaseTardisConsoleUnitMonitorScreen {
    private final Screen parentScreen;
    private final String tardisId;
    private final String selectedConsoleRoomId;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    public TardisConsoleUnitMonitorConsoleRoomConfirmationScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, String selectedConsoleRoomId, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_CONSOLE_ROOMS_TITLE, tardisConsoleUnitBlockEntity);

        this.parentScreen = parentScreen;
        this.tardisId = tardisId;
        this.selectedConsoleRoomId = selectedConsoleRoomId;
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_CONSOLE_ROOMS_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = acceptButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_CONSOLE_ROOMS_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
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
        new TardisConsoleUnitMonitorConsoleRoomApplyPacket(this.tardisId, this.selectedConsoleRoomId).sendToServer();
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

        textPos = textPos.add(RenderHelper.drawTextMultilineCentered(DWM.TEXTS.MONITOR_CONSOLE_ROOMS_CONFIRMATION_TEXT_1, this.textRenderer, context, textPos, this.textRenderer.fontHeight, maxWidth, 0xE0E0E0));
        RenderHelper.drawTextMultilineCentered(DWM.TEXTS.MONITOR_CONSOLE_ROOMS_CONFIRMATION_TEXT_2, this.textRenderer, context, textPos, this.textRenderer.fontHeight, maxWidth, 0xE0E0E0);
    }
}
