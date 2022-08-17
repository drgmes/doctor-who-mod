package net.drgmes.dwm.blocks.tardis.consoles.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public abstract class BaseTardisConsoleTelepathicInterfaceScreen extends Screen implements IBaseScreen {
    protected static final int LINE_PADDING = 3;
    protected static final int BUTTON_HEIGHT = 20;
    protected static final int BACKGROUND_BORDERS = 24;

    protected final BaseTardisConsoleBlockEntity tardisConsoleBlockEntity;

    protected ButtonWidget acceptButton;
    protected ButtonWidget cancelButton;

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
    public Text getTitleComponent() {
        return this.getTitle();
    }

    @Override
    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE_SIZE.multiply(0.795F);
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

        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.onDone();
    }

    @Override
    protected void init() {
        if (this.client != null) this.client.keyboard.setRepeatEvents(true);

        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2;
        int buttonOffset = (int) (this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT + 1);

        Vec2f acceptButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 1, buttonOffset);
        this.acceptButton = new ButtonWidget((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_ACCEPT, (b) -> this.apply());

        Vec2f cancelButtonPos = this.getRenderPos(BACKGROUND_BORDERS - 1, buttonOffset);
        this.cancelButton = new ButtonWidget((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_CANCEL, (b) -> this.close());

        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);
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

    protected void onDone() {
        this.tardisConsoleBlockEntity.markDirty();
        if (this.client != null) this.client.setScreen(null);
    }

    protected void apply() {
        this.onDone();
    }
}
