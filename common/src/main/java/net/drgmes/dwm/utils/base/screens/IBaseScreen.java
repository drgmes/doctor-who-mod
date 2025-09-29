package net.drgmes.dwm.utils.base.screens;

import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public interface IBaseScreen {
    int getWidth();
    int getHeight();

    Identifier getBackground();
    TextRenderer getTextRenderer();
    Text getTitleComponent();

    default Vector2f getScale() {
        return new Vector2f(1, 1);
    }

    default Vector2i getFullSize(int padding) {
        Window window = MinecraftClient.getInstance().getWindow();
        return new Vector2i(window.getScaledWidth() - padding, window.getScaledHeight() - padding);
    }

    default Vector2i getFullSize() {
        return this.getFullSize(6);
    }

    default Vector2i getBackgroundOriginSize() {
        return this.getFullSize();
    }

    default Vector2i getBackgroundBorderOriginSize() {
        return new Vector2i(30, 30);
    }

    default Vector2i getBackgroundSize() {
        Vector2i fullSize = this.getFullSize();
        Vector2i originSize = this.getBackgroundOriginSize();

        int width = originSize.x;
        int height = originSize.y;

        if (width > fullSize.x) {
            height = (int) Math.ceil(height * ((float) fullSize.x / width));
            width = fullSize.x;
        }

        if (height > fullSize.y) {
            width = (int) Math.ceil(width * ((float) fullSize.y / height));
            height = fullSize.y;
        }

        return new Vector2i(Math.min(width, fullSize.x), Math.min(height, fullSize.y));
    }

    default Vector2i getBackgroundBorderSize() {
        Vector2i backgroundSize = this.getBackgroundSize();
        Vector2i backgroundOriginSize = this.getBackgroundOriginSize();
        Vector2i borderOriginSize = this.getBackgroundBorderOriginSize();

        int width = borderOriginSize.x;
        int height = borderOriginSize.y;

        if (backgroundOriginSize.x > backgroundSize.x) {
            width = (int) Math.ceil(width * ((float) backgroundSize.x / backgroundOriginSize.x));
        }

        if (backgroundOriginSize.y > backgroundSize.y) {
            height = (int) Math.ceil(height * ((float) backgroundSize.y / backgroundOriginSize.y));
        }

        return new Vector2i(width, height);
    }

    default Vector2i getRenderStartPos() {
        return new Vector2i(this.getWidth() - this.getBackgroundSize().x, this.getHeight() - this.getBackgroundSize().y).div(2);
    }

    default Vector2i getRenderPos(int offsetX, int offsetY) {
        return new Vector2i(offsetX, offsetY).add(this.getRenderStartPos());
    }

    default Vector2i getLeftTopRenderPos(int offsetX, int offsetY) {
        return this.getRenderPos(this.getBackgroundBorderSize().x + offsetX, this.getBackgroundBorderSize().y + offsetY);
    }

    default Vector2i getLeftBottomRenderPos(int offsetX, int offsetY) {
        return this.getRenderPos(this.getBackgroundBorderSize().x + offsetX, this.getBackgroundSize().y - this.getBackgroundBorderSize().y - offsetY);
    }

    default Vector2i getRightTopRenderPos(int offsetX, int offsetY) {
        return this.getRenderPos(this.getBackgroundSize().x - this.getBackgroundBorderSize().x - offsetX, this.getBackgroundBorderSize().y + offsetY);
    }

    default Vector2i getRightBottomRenderPos(int offsetX, int offsetY) {
        return this.getRenderPos(this.getBackgroundSize().x - this.getBackgroundBorderSize().x - offsetX, this.getBackgroundSize().y - this.getBackgroundBorderSize().y - offsetY);
    }

    default Vector2i getTitleRenderPos() {
        return this.getRenderPos(24, 9);
    }

    default int getTitleBackgroundColor() {
        return 0xFF4F5664;
    }

    default void renderElements(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundImage(context);
        this.renderAdditional(context, mouseX, mouseY, delta);
        this.renderTitleBackground(context);
        this.renderTitle(context);
    }

    default void renderElementsAfter(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    default void renderTitleBackground(DrawContext context) {
        int color = this.getTitleBackgroundColor();
        int titleWidth = this.getTextRenderer().getWidth(this.getTitleComponent().getString());
        Vector2i pos1 = new Vector2i(-4, 2).add(this.getTitleRenderPos());
        Vector2i pos2 = new Vector2i(titleWidth + 9, 5).add(pos1);
        context.fillGradient(pos1.x, pos1.y, pos2.x, pos2.y, color, color);
    }

    default void renderTitle(DrawContext context) {
        Vector2i pos = this.getTitleRenderPos();
        context.drawText(this.getTextRenderer(), this.getTitleComponent(), pos.x, pos.y, 0xE0E0E0, true);
    }

    default void renderBackgroundImage(DrawContext context) {
        RenderHelper.drawImage(context, this.getRenderStartPos(), this.getBackgroundSize(), this.getBackground());
    }

    default void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    default boolean shouldCloseOnInventoryKey() {
        return false;
    }

    default boolean onButtonCloseClick(double mouseX, double mouseY) {
        return false;
        //return ScreenHelper.checkMouseInboundPosition(mouseX, mouseY, this.getCloseButtonPos(), DWM.TEXTURES.BUTTON_CLOSE_SIZE);
    }
}
