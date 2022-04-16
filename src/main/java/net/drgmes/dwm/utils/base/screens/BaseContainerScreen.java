package net.drgmes.dwm.utils.base.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.drgmes.dwm.DWM;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaseContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IBaseScreen {
    protected int frame = 0;

    public BaseContainerScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        this.imageWidth = this.getBackgroundSize()[0];
        this.imageHeight = this.getBackgroundSize()[1];
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Font getFont() {
        return this.font;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public ResourceLocation getBackground() {
        return DWM.TEXTURES.GUI_TEST;
    }

    @Override
    public int[] getBackgroundSize() {
        return DWM.TEXTURES.GUI_TEST_SIZE;
    }

    @Override
    public void blit(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY) {
        super.blit(poseStack, x, y, textureX, textureY, textureWidth, textureHeight, textureClipX, textureClipY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.onClose();
        return super.mouseClicked(mouseX, mouseY, frame);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(poseStack);
        this.renderElements(poseStack, mouseX, mouseY, partialTicks);
        super.render(poseStack, mouseX, mouseY, partialTicks);

        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    }
}
