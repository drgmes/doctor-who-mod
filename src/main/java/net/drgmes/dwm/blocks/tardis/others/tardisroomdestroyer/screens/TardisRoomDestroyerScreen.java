package net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer.TardisRoomDestroyerBlockEntity;
import net.drgmes.dwm.network.ServerboundTardisRoomsDestroyerApplyPacket;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.GuiUtils;

public class TardisRoomDestroyerScreen extends Screen implements IBaseScreen {
    private static final int BUTTON_HEIGHT = 20;
    private static final int BACKGROUND_BORDERS = 24;

    private final TardisRoomDestroyerBlockEntity tardisRoomDestroyerBlockEntity;

    private Button cancelButton;
    private Button acceptButton;

    public TardisRoomDestroyerScreen(TardisRoomDestroyerBlockEntity tardisRoomDestroyerBlockEntity) {
        super(DWM.TEXTS.ARS_INTERFACE_NAME);
        this.tardisRoomDestroyerBlockEntity = tardisRoomDestroyerBlockEntity;
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
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE;
    }

    @Override
    public Vec2 getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE_SIZE.scale(0.795F);
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
        return this.getRenderPos(23, 8);
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        int buttonHeight = 20;
        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2;
        int buttonOffset = (int) (this.getBackgroundSize().y / 2 - BACKGROUND_BORDERS / 2 - buttonHeight / 2 + BUTTON_HEIGHT);

        Vec2 acceptButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2 - buttonWidth / 2, buttonOffset);
        this.acceptButton = new Button((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_DESTROY, (b) -> this.apply());

        Vec2 cancelButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2 - buttonWidth / 2, buttonOffset + buttonHeight + 1);
        this.cancelButton = new Button((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, (b) -> this.onClose());

        this.addRenderableWidget(this.acceptButton);
        this.addRenderableWidget(this.cancelButton);
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
        this.renderConfirmationMessage(poseStack);

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

    protected void renderConfirmationMessage(PoseStack poseStack) {
        if (this.tardisRoomDestroyerBlockEntity == null || this.tardisRoomDestroyerBlockEntity.room == null) return;

        Component text = Component.translatable("screen." + DWM.MODID + ".ars_interface.message");
        float textX = (this.getBackgroundSize().x - this.font.width(text)) / 2;
        float textY = (this.getBackgroundSize().y - this.font.lineHeight * 3) / 2 - BUTTON_HEIGHT;
        Vec2 textPos = this.getRenderPos(textX, textY);
        this.getFont().drawShadow(poseStack, text, textPos.x, textPos.y, 0xE0E0E0);

        MutableComponent name = this.tardisRoomDestroyerBlockEntity.room.getTitle().copy();
        name.setStyle(name.getStyle().withColor(ChatFormatting.GOLD));
        name.append(Component.literal("?").withStyle(ChatFormatting.WHITE));
        float nameX = (this.getBackgroundSize().x - this.font.width(name)) / 2;
        Vec2 namePos = this.getRenderPos(nameX, textY + this.font.lineHeight);
        this.getFont().drawShadow(poseStack, name, namePos.x, namePos.y, 0xE0E0E0);
    }

    protected void onDone() {
        this.minecraft.setScreen(null);
    }

    protected void apply() {
        if (this.tardisRoomDestroyerBlockEntity.room != null) {
            ModPackets.INSTANCE.sendToServer(new ServerboundTardisRoomsDestroyerApplyPacket(this.tardisRoomDestroyerBlockEntity.room, this.tardisRoomDestroyerBlockEntity.getBlockPos()));
        }

        this.onDone();
    }
}
