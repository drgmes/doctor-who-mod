package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.server.ArsDestroyerApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.helpers.ScreenHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public class TardisArsDestroyerScreen extends BaseScreen {
    private final TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    public TardisArsDestroyerScreen(TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
        super(DWM.TEXTS.ARS_INTERFACE_NAME);
        this.tardisArsDestroyerBlockEntity = tardisArsDestroyerBlockEntity;
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE_SIZE.multiply(0.795F);
    }

    @Override
    public Vec2f getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    protected void init() {
        super.init();

        int buttonHeight = 20;
        int buttonWidth = (int) (this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2) / 2;
        int buttonOffset = (int) (this.getBackgroundSize().y / 2 - this.getBackgroundBorderSize().y / 2 - buttonHeight / 2 + BUTTON_HEIGHT);

        Vec2f cancelButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2F - buttonWidth / 2F, buttonOffset + buttonHeight + 1);
        this.cancelButton = ScreenHelper.getButtonWidget((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, (b) -> this.close());

        Vec2f acceptButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2F - buttonWidth / 2F, buttonOffset);
        this.acceptButton = ScreenHelper.getButtonWidget((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_DESTROY, (b) -> this.apply());

        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);
        this.renderConfirmationMessage(context);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    protected void apply() {
        ArsStructure structure = this.tardisArsDestroyerBlockEntity.arsStructure;

        if (structure != null) {
            ArsCategory category = structure.getCategory();
            new ArsDestroyerApplyPacket(this.tardisArsDestroyerBlockEntity.getPos(), category != null ? category.getPath() : "", structure.getName()).sendToServer();
        }

        this.close();
    }

    protected void renderConfirmationMessage(DrawContext context) {
        if (this.tardisArsDestroyerBlockEntity == null || this.tardisArsDestroyerBlockEntity.arsStructure == null) return;

        Text text = Text.translatable("screen." + DWM.MODID + ".ars_interface.message");
        float textX = (this.getBackgroundSize().x - this.textRenderer.getWidth(text)) / 2;
        float textY = (this.getBackgroundSize().y - this.textRenderer.fontHeight * 3) / 2 - BUTTON_HEIGHT;
        Vec2f textPos = this.getRenderPos(textX, textY);
        ScreenHelper.drawText(text, this.textRenderer, context, textPos.x, textPos.y, 0xE0E0E0, true);

        MutableText name = this.tardisArsDestroyerBlockEntity.arsStructure.getTitle().copy().formatted(Formatting.GOLD);
        name.append(Text.literal("?").formatted(Formatting.WHITE));
        float nameX = (this.getBackgroundSize().x - this.textRenderer.getWidth(name)) / 2;
        Vec2f namePos = this.getRenderPos(nameX, textY + this.textRenderer.fontHeight);
        ScreenHelper.drawText(name, this.textRenderer, context, namePos.x, namePos.y, 0xE0E0E0, true);
    }
}
