package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.server.ArsDestroyerApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class TardisArsDestroyerScreen extends BaseScreen {
    private final BlockPos blockPos;
    private final ArsStructure arsStructure;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    public TardisArsDestroyerScreen(BlockPos blockPos, ArsStructure arsStructure) {
        super(DWM.TEXTS.ARS_INTERFACE_TITLE);

        this.blockPos = blockPos;
        this.arsStructure = arsStructure;
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ARS.DESTROYER_INTERFACE;
    }

    @Override
    public Vector2i getBackgroundOriginSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS.DESTROYER_INTERFACE_SIZE;
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.ARS_INTERFACE_BTN_DESTROY, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = acceptButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
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

    protected void apply() {
        if (this.arsStructure != null) {
            new ArsDestroyerApplyPacket(this.blockPos, this.arsStructure.name).sendToServer();
        }

        this.close();
    }

    protected void renderConfirmationMessage(DrawContext context) {
        if (this.blockPos == null || this.arsStructure == null) return;

        MutableText name = this.arsStructure.getTitle().copy().formatted(Formatting.GOLD);
        name.append(Text.literal("?").formatted(Formatting.WHITE));

        int maxWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2;
        int textX = (int) Math.floor(this.getBackgroundSize().x / 2F);
        int textY = (int) Math.floor(this.getBackgroundSize().y / 2F) - BUTTON_SIZE;
        Vector2i textPos = this.getRenderPos(textX, textY);

        textPos = textPos.add(RenderHelper.drawTextMultilineCentered(DWM.TEXTS.ARS_INTERFACE_MESSAGE, this.textRenderer, context, textPos, this.textRenderer.fontHeight, maxWidth, 0xE0E0E0));
        RenderHelper.drawTextMultilineCentered(name, this.textRenderer, context, textPos, this.textRenderer.fontHeight, maxWidth, 0xE0E0E0);
    }
}
