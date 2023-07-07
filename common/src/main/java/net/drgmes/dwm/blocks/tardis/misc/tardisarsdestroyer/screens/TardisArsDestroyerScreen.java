package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.server.ArsDestroyerApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

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
        return DWM.TEXTURES.GUI.TARDIS.ARS.DESTROYER_INTERFACE;
    }

    @Override
    public Vector2i getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS.DESTROYER_INTERFACE_SIZE.div(1 / 0.795F, new Vector2i());
    }

    @Override
    public Vector2i getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    protected void init() {
        super.init();

        int buttonHeight = 20;
        int buttonWidth = this.getBackgroundSize().x / 2 - this.getBackgroundBorderSize().x - 1;
        int buttonOffset = this.getBackgroundSize().y / 2 - this.getBackgroundBorderSize().y / 2 - buttonHeight / 2 + BUTTON_HEIGHT;

        Vector2i cancelButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2 - buttonWidth / 2, buttonOffset + buttonHeight + 1);
        this.cancelButton = RenderHelper.getButtonWidget(cancelButtonPos.x, cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, (b) -> this.close());

        Vector2i acceptButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2 - buttonWidth / 2, buttonOffset);
        this.acceptButton = RenderHelper.getButtonWidget(acceptButtonPos.x, acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_DESTROY, (b) -> this.apply());

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
        int textX = (int) Math.floor((this.getBackgroundSize().x - this.textRenderer.getWidth(text)) / 2F);
        int textY = (int) Math.floor((this.getBackgroundSize().y - this.textRenderer.fontHeight * 3) / 2F) - BUTTON_HEIGHT;
        Vector2i textPos = this.getRenderPos(textX, textY);
        RenderHelper.drawText(text, this.textRenderer, context, textPos.x, textPos.y, 0xE0E0E0, true);

        MutableText name = this.tardisArsDestroyerBlockEntity.arsStructure.getTitle().copy().formatted(Formatting.GOLD);
        name.append(Text.literal("?").formatted(Formatting.WHITE));
        int nameX = (int) Math.floor((this.getBackgroundSize().x - this.textRenderer.getWidth(name)) / 2F);
        Vector2i namePos = this.getRenderPos(nameX, textY + this.textRenderer.fontHeight);
        RenderHelper.drawText(name, this.textRenderer, context, namePos.x, namePos.y, 0xE0E0E0, true);
    }
}
