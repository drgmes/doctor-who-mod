package net.drgmes.dwm.utils.base.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.util.HashMap;
import java.util.function.Supplier;

public interface IBaseScreen {
    Supplier<HashMap<String, Integer>> createData = () -> new HashMap<>();

    int getWidth();

    int getHeight();

    Font getFont();

    Component getTitleComponent();

    ResourceLocation getBackground();

    Vec2 getBackgroundSize();

    void blit(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY);

    default boolean onButtonCloseClick(double mouseX, double mouseY) {
        return false;
        // return ScreenHelper.checkMouseInboundPosition(mouseX, mouseY, this.getCloseButtonPos(), DWM.TEXTURES.BUTTON_CLOSE_SIZE);
    }

    default void renderElements(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack, mouseX, mouseY);
        this.renderAdditional(poseStack, mouseX, mouseY, partialTicks);
        this.renderTitle(poseStack);
    }

    default Vec2 getRenderStartPos() {
        return new Vec2(
            (this.getWidth() - this.getBackgroundSize().x) / 2,
            (this.getHeight() - this.getBackgroundSize().y) / 2
        );
    }

    default Vec2 getRenderPos(float offsetX, float offsetY) {
        final Vec2 pos = this.getRenderStartPos();
        return new Vec2(pos.x + offsetX, pos.y + offsetY);
    }

    default Vec2 getTitleRenderPos() {
        return this.getRenderPos(0, 0);
    }

    default void renderImage(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY, ResourceLocation image) {
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, image);
        blit(poseStack, x, y, textureX, textureY, textureWidth, textureHeight, textureClipX, textureClipY);
        poseStack.popPose();
    }

    default void renderImage(PoseStack poseStack, float x, float y, float textureX, float textureY, float textureWidth, float textureHeight, float textureClipX, float textureClipY, ResourceLocation image) {
        this.renderImage(poseStack, (int) x, (int) y, (int) textureX, (int) textureY, (int) textureWidth, (int) textureHeight, (int) textureClipX, (int) textureClipY, image);
    }

    default void renderImage(PoseStack poseStack, Vec2 pos, Vec2 size, ResourceLocation image) {
        this.renderImage(poseStack, pos.x, pos.y, 0, 0, size.x, size.y, size.x, size.y, image);
    }

    default void renderTitle(PoseStack poseStack) {
        Vec2 pos = this.getTitleRenderPos();
        this.getFont().drawShadow(poseStack, this.getTitleComponent(), pos.x, pos.y, 0xE0E0E0);
    }

    default void renderBackground(PoseStack poseStack, int mouseX, int mouseY) {
        this.renderImage(poseStack, this.getRenderStartPos(), this.getBackgroundSize(), this.getBackground());
    }

    default void renderAdditional(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    }
}
