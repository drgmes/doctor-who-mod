package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorConsoleRoomApplyPacket;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorConsoleRoomsConfirmationScreen extends BaseTardisConsoleUnitMonitorScreen {
    private final Screen parentScreen;
    private final String tardisId;
    private final String selectedConsoleRoomId;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    public TardisConsoleUnitMonitorConsoleRoomsConfirmationScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, String selectedConsoleRoomId, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_ACTION_CONSOLE_ROOMS, tardisConsoleUnitBlockEntity);

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

        int buttonWidth = this.getBackgroundSize().x / 2 - this.getBackgroundBorderSize().x - 1;
        int buttonOffset = this.getBackgroundSize().y - this.getBackgroundBorderSize().y - BUTTON_HEIGHT - 1;

        Vector2i cancelButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + 1, buttonOffset);
        this.cancelButton = RenderHelper.getButtonWidget(cancelButtonPos.x, cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_CONSOLE_ROOMS_CANCEL, (b) -> {
            if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
            else this.close();
        });

        Vector2i acceptButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + buttonWidth + 2, buttonOffset);
        this.acceptButton = RenderHelper.getButtonWidget(acceptButtonPos.x, acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_CONSOLE_ROOMS_ACCEPT, (b) -> {
            this.apply();
        });

        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);
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

    private void renderConfirmationMessage(DrawContext context) {
        int maxWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2;
        int textX = (int) Math.floor(this.getBackgroundSize().x / 2F);
        int textY = (int) Math.floor(this.getBackgroundSize().y / 2F) - BUTTON_HEIGHT;
        Vector2i textPos = this.getRenderPos(textX, textY);

        textPos = textPos.add(RenderHelper.drawTextMultilineCentered(DWM.TEXTS.MONITOR_CONSOLE_ROOMS_CONFIRMATION_1, this.textRenderer, context, textPos, this.textRenderer.fontHeight, maxWidth, 0xE0E0E0));
        RenderHelper.drawTextMultilineCentered(DWM.TEXTS.MONITOR_CONSOLE_ROOMS_CONFIRMATION_2, this.textRenderer, context, textPos, this.textRenderer.fontHeight, maxWidth, 0xE0E0E0);
    }
}
