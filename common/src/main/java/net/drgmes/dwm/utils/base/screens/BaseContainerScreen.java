package net.drgmes.dwm.utils.base.screens;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public abstract class BaseContainerScreen<C extends ScreenHandler> extends AbstractInventoryScreen<C> implements IBaseScreen {
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
    public Identifier getBackground() {
        return null;
    }

    @Override
    public Vector2i getBackgroundSize() {
        return new Vector2i(0, 0);
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
    public Text getTitle() {
        return this.title;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context);
        this.renderElements(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.renderElementsAfter(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.close();
        return super.mouseClicked(mouseX, mouseY, frame);
    }
}
