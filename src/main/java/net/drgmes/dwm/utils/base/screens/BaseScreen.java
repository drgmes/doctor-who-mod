package net.drgmes.dwm.utils.base.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class BaseScreen extends Screen implements IBaseScreen {
    protected static final int BUTTON_HEIGHT = 20;
    protected static final int BACKGROUND_BORDERS = 24;

    protected BaseScreen(Text title) {
        super(title);
    }

    @Override
    public void drawTexture(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight) {
        DrawableHelper.drawTexture(matrixStack, x, y, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }

    @Override
    public void fillGradient(MatrixStack matrixStack, int x, int y, int textureWidth, int textureHeight, int colorStart, int colorEnd) {
        DrawableHelper.fillGradient(matrixStack, x, y, textureWidth, textureHeight, colorStart, colorEnd, this.getZOffset());
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
    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    @Override
    public Text getTitleComponent() {
        return this.getTitle();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrixStack);
        this.renderElements(matrixStack, mouseX, mouseY, delta, 0xFF4F5664);
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        if (this.client != null) this.client.setScreen(null);
    }

    @Override
    protected void init() {
        if (this.client != null) this.client.keyboard.setRepeatEvents(true);
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
        if (this.onButtonCloseClick(mouseX, mouseY)) this.close();
        return super.mouseClicked(mouseX, mouseY, frame);
    }
}
