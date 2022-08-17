package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.network.ArsRemoteCallablePackets;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public class TardisArsDestroyerScreen extends Screen implements IBaseScreen {
    private static final int BUTTON_HEIGHT = 20;
    private static final int BACKGROUND_BORDERS = 24;

    private final TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    public TardisArsDestroyerScreen(TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
        super(DWM.TEXTS.ARS_INTERFACE_NAME);
        this.tardisArsDestroyerBlockEntity = tardisArsDestroyerBlockEntity;
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
    public Text getTitleComponent() {
        return this.getTitle();
    }

    @Override
    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE_SIZE.multiply(0.795F);
    }

    @Override
    public void drawTexture(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight) {
        DrawableHelper.drawTexture(matrixStack, x, y, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }

    @Override
    public Vec2f getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    public Text getTitle() {
        return this.title;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrixStack);
        this.renderBackground(matrixStack, mouseX, mouseY);

        Vec2f pos = new Vec2f(19, 5);
        Vec2f pos1 = this.getRenderPos(pos.x, pos.y);
        Vec2f pos2 = this.getRenderPos(pos.x + this.textRenderer.getWidth(this.getTitle().getString()) + 9, pos.y + this.textRenderer.fontHeight + 1);
        int color = 0xFF4F5664;

        this.fillGradient(matrixStack, (int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y, color, color);
        this.renderTitle(matrixStack);
        this.renderConfirmationMessage(matrixStack);

        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.onDone();
    }

    @Override
    protected void init() {
        if (this.client != null) this.client.keyboard.setRepeatEvents(true);

        int buttonHeight = 20;
        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2;
        int buttonOffset = (int) (this.getBackgroundSize().y / 2 - BACKGROUND_BORDERS / 2 - buttonHeight / 2 + BUTTON_HEIGHT);

        Vec2f acceptButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2F - buttonWidth / 2F, buttonOffset);
        this.acceptButton = new ButtonWidget((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_DESTROY, (b) -> this.apply());

        Vec2f cancelButtonPos = this.getRenderPos(this.getBackgroundSize().x / 2F - buttonWidth / 2F, buttonOffset + buttonHeight + 1);
        this.cancelButton = new ButtonWidget((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, (b) -> this.close());

        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.cancelButton);
    }

    @Override
    public void removed() {
        if (this.client != null) this.client.keyboard.setRepeatEvents(false);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        this.init(mc, width, height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.onDone();
        return super.mouseClicked(mouseX, mouseY, frame);
    }

    protected void renderConfirmationMessage(MatrixStack matrixStack) {
        if (this.tardisArsDestroyerBlockEntity == null || this.tardisArsDestroyerBlockEntity.arsStructure == null) return;

        Text text = Text.translatable("screen." + DWM.MODID + ".ars_interface.message");
        float textX = (this.getBackgroundSize().x - this.textRenderer.getWidth(text)) / 2;
        float textY = (this.getBackgroundSize().y - this.textRenderer.fontHeight * 3) / 2 - BUTTON_HEIGHT;
        Vec2f textPos = this.getRenderPos(textX, textY);
        this.textRenderer.drawWithShadow(matrixStack, text, textPos.x, textPos.y, 0xE0E0E0);

        MutableText name = this.tardisArsDestroyerBlockEntity.arsStructure.getTitle().copy().formatted(Formatting.GOLD);
        name.append(Text.literal("?").formatted(Formatting.WHITE));
        float nameX = (this.getBackgroundSize().x - this.textRenderer.getWidth(name)) / 2;
        Vec2f namePos = this.getRenderPos(nameX, textY + this.textRenderer.fontHeight);
        this.textRenderer.drawWithShadow(matrixStack, name, namePos.x, namePos.y, 0xE0E0E0);
    }

    protected void onDone() {
        if (this.client != null) this.client.setScreen(null);
    }

    protected void apply() {
        if (this.tardisArsDestroyerBlockEntity.arsStructure != null) {
            PacketHelper.sendToServer(
                ArsRemoteCallablePackets.class,
                "applyArsDestroyer",
                this.tardisArsDestroyerBlockEntity.getPos(), this.tardisArsDestroyerBlockEntity.arsStructure.getCategory().getPath(), this.tardisArsDestroyerBlockEntity.arsStructure.getName()
            );
        }

        this.onDone();
    }
}
