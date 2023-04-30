package net.drgmes.dwm.utils.base.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

@Environment(EnvType.CLIENT)
public interface IBaseScreen {
    void drawTexture(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight);
    void fillBackgroundGradient(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight, int colorStart, int colorEnd);

    int getWidth();
    int getHeight();

    TextRenderer getTextRenderer();
    Identifier getBackground();
    Vec2f getBackgroundSize();
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

    default void renderElements(MatrixStack matrixStack, int mouseX, int mouseY, float delta, int color) {
        this.renderBackgroundImage(matrixStack);
        this.renderTitleBackground(matrixStack, color);
        this.renderTitle(matrixStack);
        this.renderAdditional(matrixStack, mouseX, mouseY, delta);
    }

    default void renderTitleBackground(MatrixStack matrixStack, int color) {
        float titleWidth = this.getTextRenderer().getWidth(this.getTitleComponent().getString());
        Vec2f pos1 = this.getTitleRenderPos().add(new Vec2f(-4, 0));
        Vec2f pos2 = pos1.add(new Vec2f(titleWidth + 9, this.getTextRenderer().fontHeight + 1));
        this.fillBackgroundGradient(matrixStack, (int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y, color, color);
    }

    default void renderTitle(MatrixStack matrixStack) {
        Vec2f pos = this.getTitleRenderPos();
        this.getTextRenderer().drawWithShadow(matrixStack, this.getTitleComponent(), pos.x, pos.y, 0xE0E0E0);
    }

    default void renderBackgroundImage(MatrixStack matrixStack) {
        this.drawImage(matrixStack, this.getRenderStartPos(), this.getBackgroundSize(), this.getBackground());
    }

    default void renderAdditional(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
    }

    default void drawImage(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight, Identifier image) {
        matrixStack.push();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, image);
        drawTexture(matrixStack, x, y, textureWidth, textureHeight);
        matrixStack.pop();
    }

    default void drawImage(MatrixStack matrixStack, Vec2f pos, Vec2f size, Identifier image) {
        this.drawImage(matrixStack, (int) pos.x, (int) pos.y, (int) size.x, (int) size.y, image);
    }

    default boolean onButtonCloseClick(double mouseX, double mouseY) {
        return false;
//        return ScreenHelper.checkMouseInboundPosition(mouseX, mouseY, this.getCloseButtonPos(), DWM.TEXTURES.BUTTON_CLOSE_SIZE);
    }
}
