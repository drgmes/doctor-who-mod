package net.drgmes.dwm.utils.base.screens;

import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public interface IBaseScreen {
    int getWidth();
    int getHeight();

    Identifier getBackground();
    Vector2i getBackgroundSize();
    Vector2i getBackgroundBorderSize();

    TextRenderer getTextRenderer();
    Text getTitleComponent();

    default Vector2i getRenderStartPos() {
        return new Vector2i(this.getWidth() - this.getBackgroundSize().x, this.getHeight() - this.getBackgroundSize().y).div(2);
    }

    default Vector2i getRenderPos(int offsetX, int offsetY) {
        return new Vector2i(offsetX, offsetY).add(this.getRenderStartPos());
    }

    default Vector2i getTitleRenderPos() {
        return this.getRenderPos(0, 0);
    }

    default int getTitleBackgroundColor() {
        return 0xFF4F5664;
    }

    default void renderElements(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundImage(context);
        this.renderTitleBackground(context);
        this.renderTitle(context);
        this.renderAdditional(context, mouseX, mouseY, delta);
    }

    default void renderElementsAfter(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    default void renderTitleBackground(DrawContext context) {
        int color = this.getTitleBackgroundColor();
        int titleWidth = this.getTextRenderer().getWidth(this.getTitleComponent().getString());
        Vector2i pos1 = new Vector2i(-4, 0).add(this.getTitleRenderPos());
        Vector2i pos2 = new Vector2i(titleWidth + 9, this.getTextRenderer().fontHeight + 1).add(pos1);
        context.fillGradient(pos1.x, pos1.y, pos2.x, pos2.y, color, color);
    }

    default void renderTitle(DrawContext context) {
        Vector2i pos = this.getTitleRenderPos();
        RenderHelper.drawText(this.getTitleComponent(), this.getTextRenderer(), context, pos.x, pos.y, 0xE0E0E0, true);
    }

    default void renderBackgroundImage(DrawContext context) {
        this.drawImage(context, this.getRenderStartPos(), this.getBackgroundSize(), this.getBackground());
    }

    default void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    default void drawImage(DrawContext context, Vector2i pos, Vector2i size, Identifier image) {
        context.drawTexture(image, pos.x, pos.y, 0, 0, 0, size.x, size.y, size.x, size.y);
    }

    default boolean shouldCloseOnInventoryKey() {
        return false;
    }

    default boolean onButtonCloseClick(double mouseX, double mouseY) {
        return false;
//        return ScreenHelper.checkMouseInboundPosition(mouseX, mouseY, this.getCloseButtonPos(), DWM.TEXTURES.BUTTON_CLOSE_SIZE);
    }
}
