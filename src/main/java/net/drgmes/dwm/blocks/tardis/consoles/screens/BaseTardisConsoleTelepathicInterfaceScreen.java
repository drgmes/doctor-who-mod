package net.drgmes.dwm.blocks.tardis.consoles.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.GuiUtils;

public abstract class BaseTardisConsoleTelepathicInterfaceScreen extends Screen implements IBaseScreen {
    protected static final int LINE_PADDING = 3;
    protected static final int BUTTON_HEIGHT = 20;
    protected static final int BACKGROUND_BORDERS = 24;

    protected final BaseTardisConsoleBlockEntity tardisConsoleBlockEntity;

    protected Button cancelButton;
    protected Button acceptButton;

    public BaseTardisConsoleTelepathicInterfaceScreen(BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
        super(DWM.TEXTS.TELEPATHIC_INTERFACE_NAME);
        this.tardisConsoleBlockEntity = tardisConsoleBlockEntity;
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
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE;
    }

    @Override
    public Vec2 getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE_SIZE.scale(0.795F);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void blit(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY) {
        super.blit(poseStack, x, y, textureX, textureY, textureWidth, textureHeight, textureClipX, textureClipY);
    }

    @Override
    public Vec2 getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2;
        int buttonOffset = (int) (this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT + 1);

        Vec2 cancelButtonPos = this.getRenderPos(BACKGROUND_BORDERS - 1, buttonOffset);
        this.cancelButton = new Button((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_CANCEL, (b) -> this.onClose());

        Vec2 acceptButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 1, buttonOffset);
        this.acceptButton = new Button((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_ACCEPT, (b) -> this.apply());

        this.addRenderableWidget(this.cancelButton);
        this.addRenderableWidget(this.acceptButton);
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
        this.tardisConsoleBlockEntity.setChanged();
        this.minecraft.setScreen((Screen) null);
    }

    protected void apply() {
        this.onDone();
    }
}
