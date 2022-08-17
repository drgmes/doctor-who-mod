package net.drgmes.dwm.utils.base.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public interface IBaseScreen {
    int getWidth();
    int getHeight();

    Text getTitleComponent();
    TextRenderer getTextRenderer();
    Identifier getBackground();
    Vec2f getBackgroundSize();

    void drawTexture(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight);

    default boolean onButtonCloseClick(double mouseX, double mouseY) {
        return false;
        // return ScreenHelper.checkMouseInboundPosition(mouseX, mouseY, this.getCloseButtonPos(), DWM.TEXTURES.BUTTON_CLOSE_SIZE);
    }

    default void renderElements(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrixStack, mouseX, mouseY);
        this.renderAdditional(matrixStack, mouseX, mouseY, delta);
        this.renderTitle(matrixStack);
    }

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

    default void renderImage(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight, Identifier image) {
        matrixStack.push();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, image);
        drawTexture(matrixStack, x, y, textureWidth, textureHeight);
        matrixStack.pop();
    }

    default void renderImage(MatrixStack matrixStack, Vec2f pos, Vec2f size, Identifier image) {
        this.renderImage(matrixStack, (int) pos.x, (int) pos.y, (int) size.x, (int) size.y, image);
    }

    default void renderTitle(MatrixStack matrixStack) {
        Vec2f pos = this.getTitleRenderPos();
        this.getTextRenderer().drawWithShadow(matrixStack, this.getTitleComponent(), pos.x, pos.y, 0xE0E0E0);
    }

    default void renderBackground(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.renderImage(matrixStack, this.getRenderStartPos(), this.getBackgroundSize(), this.getBackground());
    }

    default void renderAdditional(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
    }
}
