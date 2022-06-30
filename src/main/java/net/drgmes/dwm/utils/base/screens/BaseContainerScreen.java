package net.drgmes.dwm.utils.base.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BaseContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IBaseScreen {
    protected int frame = 0;

    public BaseContainerScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        Vec2 backgroundSize = this.getBackgroundSize();
        this.imageWidth = (int) backgroundSize.x;
        this.imageHeight = (int) backgroundSize.y;
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
    public Component getTitleComponent() {
        return this.getTitle();
    }

    @Override
    public ResourceLocation getBackground() {
        return null;
    }

    @Override
    public Vec2 getBackgroundSize() {
        return new Vec2(0, 0);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void blit(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY) {
        GuiComponent.blit(poseStack, x, y, textureX, textureY, textureWidth, textureHeight, textureClipX, textureClipY);
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
