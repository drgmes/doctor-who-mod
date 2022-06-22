package net.drgmes.dwm.items.screwdriver.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.screwdriver.Screwdriver.ScrewdriverMode;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.GuiUtils;

public abstract class BaseScrewdriverInterfaceScreen extends Screen implements IBaseScreen {
    protected static final int PADDING = 5;
    protected static final int BACKGROUND_BORDERS = 19;
    protected static final int BUTTON_HEIGHT = 20;

    protected final ItemStack screwdriverItemStack;
    protected final boolean isMainHand;

    protected ScrewdriverMode mode;

    public BaseScrewdriverInterfaceScreen(ItemStack screwdriverItemStack, boolean isMainHand) {
        super(DWM.TEXTS.SCREWDRIVER_INTERFACE_NAME);

        this.screwdriverItemStack = screwdriverItemStack;
        this.isMainHand = isMainHand;

        this.mode = Screwdriver.getInteractionMode(this.screwdriverItemStack);
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
        return DWM.TEXTURES.GUI.SCREWDRIVER.INTERFACE_MAIN;
    }

    @Override
    public Vec2 getBackgroundSize() {
        return DWM.TEXTURES.GUI.SCREWDRIVER.INTERFACE_MAIN_SIZE.scale(0.65F);
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
    public Vec2 getTitleRenderPos() {
        return this.getRenderPos(23, 6);
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.onDone();
        return super.mouseClicked(mouseX, mouseY, frame);
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(poseStack);
        this.renderBackground(poseStack, mouseX, mouseY);

        Vec2 pos = new Vec2(19, 5);
        Vec2 pos1 = this.getRenderPos(pos.x, pos.y);
        Vec2 pos2 = this.getRenderPos(pos.x + this.font.width(this.getTitle().getString()) + 9, pos.y + this.font.lineHeight + 1);
        int color = 0xFF4F5664;

        GuiUtils.drawGradientRect(poseStack.last().pose(), 0, (int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y, color, color);
        this.renderTitle(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void resize(Minecraft mc, int width, int height) {
        this.init(mc, width, height);
    }

    @Override
    public void onClose() {
        this.onDone();
    }

    protected void onDone() {
        this.minecraft.setScreen(null);
    }
}
