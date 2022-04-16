package net.drgmes.dwm.utils.base.screens;

import java.util.HashMap;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface IBaseScreen {
    public static final Supplier<HashMap<String, Integer>> createData = () -> new HashMap<>();

    int getWidth();

    int getHeight();

    Font getFont();

    Component getTitle();

    ResourceLocation getBackground();

    int[] getBackgroundSize();

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

    default int[] getRenderStartPos() {
        return new int[]{
            (this.getWidth() - this.getBackgroundSize()[0] + 12) / 2,
            (this.getHeight() - this.getBackgroundSize()[1]) / 2
        };
    }

    default int[] getRenderPos(int offsetX, int offsetY) {
        final int[] startPos = this.getRenderStartPos();
        final int x = startPos[0] + offsetX;
        final int y = startPos[1] + offsetY;

        return new int[]{ x, y };
    }

    default void renderImage(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY, ResourceLocation image) {
        poseStack.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, image);
        blit(poseStack, x, y, textureX, textureY, textureWidth, textureHeight, textureClipX, textureClipY);
        poseStack.popPose();
    }

    default void renderImage(PoseStack poseStack, int[] pos, int[] size, ResourceLocation image) {
        this.renderImage(poseStack, pos[0], pos[1], 0, 0, size[0], size[1], size[0], size[1], image);
    }

    default void renderTitle(PoseStack poseStack) {
        int[] pos = this.getRenderPos(60, 7);
        this.getFont().drawShadow(poseStack, this.getTitle(), pos[0], pos[1], 0xE0E0E0);
    }

    default void renderBackground(PoseStack poseStack, int mouseX, int mouseY) {
        this.renderImage(poseStack, this.getRenderStartPos(), this.getBackgroundSize(), this.getBackground());
    }

    default void renderAdditional(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
    }
}
