package net.drgmes.dwm.utils.base.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.joml.Vector2f;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public abstract class BaseContainerScreen<C extends ScreenHandler> extends AbstractInventoryScreen<C> implements IBaseScreen {
    protected Vector2f cachedScale;

    private Vector2i cachedBackgroundSize;
    private Vector2i cachedBackgroundBorderSize;

    public BaseContainerScreen(C menu, PlayerInventory inventory, Text component) {
        super(menu, inventory, component);

        Vector2i backgroundSize = this.getBackgroundSize();
        this.backgroundWidth = backgroundSize.x;
        this.backgroundHeight = backgroundSize.y;
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
    public Text getTitle() {
        return this.title;
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        this.cachedScale = null;
        this.cachedBackgroundSize = null;
        this.cachedBackgroundBorderSize = null;

        super.resize(mc, width, height);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
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
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.close();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void back() {
        this.close();
    }
}
