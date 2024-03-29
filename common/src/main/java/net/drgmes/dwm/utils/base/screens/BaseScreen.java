package net.drgmes.dwm.utils.base.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.joml.Vector2i;

public abstract class BaseScreen extends Screen implements IBaseScreen {
    protected static final int BUTTON_HEIGHT = 20;

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
    public Vector2i getBackgroundBorderSize() {
        return new Vector2i(24, 24);
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
//        if (this.client != null) this.client.keyboard.setRepeatEvents(true);
    }

    @Override
    public void removed() {
//        if (this.client != null) this.client.keyboard.setRepeatEvents(false);
    }

    @Override
    public void close() {
        if (this.client != null) this.client.setScreen(null);
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        this.init(mc, width, height);
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
            this.close();
        }

        return true;
    }
}
