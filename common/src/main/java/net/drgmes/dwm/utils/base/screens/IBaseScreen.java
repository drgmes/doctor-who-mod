package net.drgmes.dwm.utils.base.screens;

import net.drgmes.dwm.utils.helpers.ScreenHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

@Environment(EnvType.CLIENT)
public interface IBaseScreen {
    int getWidth();
    int getHeight();

    Identifier getBackground();
    Vec2f getBackgroundSize();
    Vec2f getBackgroundBorderSize();

    TextRenderer getTextRenderer();
    Text getTitleComponent();

    default Vec2f getRenderStartPos() {
        return new Vec2f(
            (this.getWidth() - this.getBackgroundSize().x) / 2,
            (this.getHeight() - this.getBackgroundSize().y) / 2
        );
    }

    default Vec2f getRenderPos(float offsetX, float offsetY) {
        final Vec2f pos = this.getRenderStartPos();
        return new Vec2f(pos.x + offsetX, pos.y + offsetY);
    }

    default Vec2f getTitleRenderPos() {
        return this.getRenderPos(0, 0);
    }

    default void renderElements(DrawContext context, int mouseX, int mouseY, float delta, int color) {
        this.renderBackgroundImage(context);
        this.renderTitleBackground(context, color);
        this.renderTitle(context);
        this.renderAdditional(context, mouseX, mouseY, delta);
    }

    default void renderTitleBackground(DrawContext context, int color) {
        float titleWidth = this.getTextRenderer().getWidth(this.getTitleComponent().getString());
        Vec2f pos1 = this.getTitleRenderPos().add(new Vec2f(-4, 0));
        Vec2f pos2 = pos1.add(new Vec2f(titleWidth + 9, this.getTextRenderer().fontHeight + 1));
        context.fillGradient((int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y, color, color);
    }

    default void renderTitle(DrawContext context) {
        Vec2f pos = this.getTitleRenderPos();
        ScreenHelper.drawText(this.getTitleComponent(), this.getTextRenderer(), context, pos.x, pos.y, 0xE0E0E0, true);
    }

    default void renderBackgroundImage(DrawContext context) {
        this.drawImage(context, this.getRenderStartPos(), this.getBackgroundSize(), this.getBackground());
    }

    default void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    default void drawImage(DrawContext context, Vec2f pos, Vec2f size, Identifier image) {
        context.drawTexture(image, (int) pos.x, (int) pos.y, 0, 0, 0, (int) size.x, (int) size.y, (int) size.x, (int) size.y);
    }

    default boolean shouldCloseOnInventoryKey() {
        return false;
    }

    default boolean onButtonCloseClick(double mouseX, double mouseY) {
        return false;
//        return ScreenHelper.checkMouseInboundPosition(mouseX, mouseY, this.getCloseButtonPos(), DWM.TEXTURES.BUTTON_CLOSE_SIZE);
    }
}
