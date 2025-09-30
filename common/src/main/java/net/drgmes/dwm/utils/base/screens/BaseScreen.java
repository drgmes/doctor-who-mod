package net.drgmes.dwm.utils.base.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public abstract class BaseScreen extends Screen implements IBaseScreen {
    protected static final int SCREEN_MARGIN = 4;
    protected static final int LINE_HEIGHT = 3;

    protected static final int INPUT_HEIGHT = 18;
    protected static final int INPUT_MARGIN = 2;

    protected static final int BUTTON_SIZE = 20;
    protected static final int BUTTON_PADDING = 3;
    protected static final int BUTTON_MARGIN = 1;

    protected Vector2f cachedScale;

    private Vector2i cachedBackgroundSize;
    private Vector2i cachedBackgroundBorderSize;

    protected BaseScreen(Text title) {
        super(title);
    }

    @Override
    public boolean shouldPause() {
        return false;
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
    public Vector2f getScale() {
        if (this.cachedScale != null) return this.cachedScale;
        return IBaseScreen.super.getScale();
    }

    @Override
    public Vector2i getBackgroundSize() {
        if (this.cachedBackgroundSize != null) return this.cachedBackgroundSize;

        Vector2i size = IBaseScreen.super.getBackgroundSize();
        Vector2i originSize = this.getBackgroundOriginSize();

        this.cachedScale = new Vector2f((float) size.x / originSize.x, (float) size.y / originSize.y);
        this.cachedBackgroundSize = size;
        return size;
    }

    @Override
    public Vector2i getBackgroundBorderSize() {
        if (this.cachedBackgroundBorderSize != null) return this.cachedBackgroundBorderSize;
        this.cachedBackgroundBorderSize = IBaseScreen.super.getBackgroundBorderSize();
        return this.cachedBackgroundBorderSize;
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
    protected void init() {
        //if (this.client != null) this.client.keyboard.setRepeatEvents(true);
    }

    @Override
    public void removed() {
        //if (this.client != null) this.client.keyboard.setRepeatEvents(false);
    }

    @Override
    public void close() {
        if (this.client != null) this.client.setScreen(null);
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        this.cachedScale = null;
        this.cachedBackgroundSize = null;
        this.cachedBackgroundBorderSize = null;

        super.resize(mc, width, height);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        this.renderElements(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.renderElementsAfter(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.close();
        return super.mouseClicked(mouseX, mouseY, frame);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (this.shouldCloseOnInventoryKey() && this.client != null && this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            this.back();
        }

        return true;
    }

    public void back() {
        this.close();
    }
}
