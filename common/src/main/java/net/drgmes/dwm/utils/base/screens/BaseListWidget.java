package net.drgmes.dwm.utils.base.screens;

import com.google.common.collect.ImmutableList;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class BaseListWidget extends ElementListWidget<BaseListWidget.BaseListEntry> {
    private final Vector2i pos;
    private final int padding;
    private boolean shouldDrawBackground;

    public BaseListWidget(MinecraftClient mc, Vector2i pos, Vector2i size, int padding) {
        super(mc, size.x, size.y, pos.y, mc.textRenderer.fontHeight + padding * 2);

        this.pos = pos;
        this.padding = padding;
        this.shouldDrawBackground = true;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getDefaultScrollbarX() {
        return this.getRowRight() - 8;
    }

    @Override
    public void drawMenuListBackground(DrawContext context) {
        if (!this.shouldDrawBackground) return;
        int color = 0x40000000;

        context.fillGradient(this.getX() - 1, this.getY() - 1, this.getX() + this.getWidth() + 1, this.getBottom() + 1, color, color);
    }

    @Override
    public void drawHeaderAndFooterSeparators(DrawContext context) {
        int color = 0xFF231F26;
        float width = 0.795F;
        float margin = 1 - width;

        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getX() - margin, this.getY() - width - margin, this.getX() + this.getWidth() + margin, this.getY() - margin, color);
        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getX() - margin, this.getBottom() + margin, this.getX() + this.getWidth() + margin, this.getBottom() + width + margin, color);
        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getX() - width - margin, this.getY() - width - margin, this.getX() - margin, this.getBottom() + width + margin, color);
        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getX() + this.getWidth() + margin, this.getY() - width - margin, this.getX() + this.getWidth() + width + margin, this.getBottom() + width + margin, color);
    }

    public void init() {
        this.refreshList();
        this.setX(this.pos.x);
    }

    public void refreshList() {
        this.setScrollAmount(0);
        this.clearEntries();
    }

    public void setShouldDrawBackground(boolean flag) {
        this.shouldDrawBackground = flag;
    }

    public abstract class BaseListEntry extends Entry<BaseListEntry> {
        public Formatting chevronFormat;
        public Formatting selectedItemFormat;

        public abstract Text getText();

        public BaseListEntry(Formatting chevronFormat, Formatting selectedItemFormat) {
            this.chevronFormat = chevronFormat;
            this.selectedItemFormat = selectedItemFormat;
        }

        public BaseListEntry() {
            this(Formatting.WHITE, Formatting.RESET);
        }

        @Override
        public void render(DrawContext context, int entryIdx, int top, int left, int entryWidth, int height, int mouseX, int mouseY, boolean flag, float partialTick) {
            MutableText text = this.getText().copy();

            if (this.isSelected()) text = this.getSelectedPrependText().append(text.formatted(this.selectedItemFormat));
            else text = this.getPrependText().append(text);

            Vector2i pos = new Vector2i(left + padding, top + 2);
            RenderHelper.drawTextClipped(text, client.textRenderer, context, pos, width, 0xFFFFFF);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(new Selectable() {
                @Override
                public SelectionType getType() {
                    return SelectionType.NONE;
                }

                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, BaseListEntry.this.getText());
                }
            });
        }

        @Override
        public List<? extends Element> children() {
            return Collections.emptyList();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int delta) {
            setSelected(this);
            return false;
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        public boolean isSelected() {
            return getSelectedOrNull() == this;
        }

        public MutableText getPrependText() {
            return Text.empty();
        }

        public MutableText getSelectedPrependText() {
            return Text.empty().append(Text.literal("> ").formatted(this.chevronFormat, Formatting.BOLD));
        }
    }
}
