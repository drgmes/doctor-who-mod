package net.drgmes.dwm.utils.base.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

@Environment(EnvType.CLIENT)
public abstract class BaseContainerScreen<T extends ScreenHandler> extends AbstractInventoryScreen<T> implements IBaseScreen {
    public BaseContainerScreen(T menu, PlayerInventory inventory, Text component) {
        super(menu, inventory, component);

        Vec2f backgroundSize = this.getBackgroundSize();
        this.backgroundWidth = (int) backgroundSize.x;
        this.backgroundHeight = (int) backgroundSize.y;
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
    public Identifier getBackground() {
        return null;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return new Vec2f(0, 0);
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.renderBackground(matrixStack);
        this.renderElements(matrixStack, mouseX, mouseY, delta, 0xFF4F5664);
        super.render(matrixStack, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float delta, int mouseX, int mouseY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.close();
        return super.mouseClicked(mouseX, mouseY, frame);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
