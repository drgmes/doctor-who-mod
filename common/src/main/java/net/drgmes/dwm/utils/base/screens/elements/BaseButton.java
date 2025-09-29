package net.drgmes.dwm.utils.base.screens.elements;

import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public class BaseButton extends ButtonWidget {
    private final Identifier icon;
    private final int padding;

    public BaseButton(Vector2i pos, int size, int padding, Text tooltip, Identifier icon, PressAction onPress) {
        super(pos.x, pos.y, size, size, Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);

        this.icon = icon;
        this.padding = padding;
        this.setTooltip(Tooltip.of(tooltip));
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        // Render gradient overlay
        if (this.active) {
            int padding = 2;
            Vector2i bgPos1 = new Vector2i(this.getX() + padding, this.getY() + padding);
            Vector2i bgPos2 = new Vector2i(bgPos1.x + this.getWidth() - padding * 2, bgPos1.y + this.getHeight() - padding * 2);
            context.fillGradient(bgPos1.x, bgPos1.y, bgPos2.x, bgPos2.y, 0x40000000, 0x20000000);
        }

        Vector2i pos = new Vector2i(this.getX() + this.padding, this.getY() + this.padding);
        Vector2i size = new Vector2i(this.getWidth() - this.padding * 2, this.getHeight() - this.padding * 2);

        if (!this.active) context.setShaderColor(0.35F, 0.35F, 0.35F, 0.5F);
        RenderHelper.drawImage(context, pos, size, this.icon);
        if (!this.active) context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
