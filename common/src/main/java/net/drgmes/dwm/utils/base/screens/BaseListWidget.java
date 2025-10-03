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
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class BaseListWidget extends ElementListWidget<BaseListWidget.BaseListEntry> {
    public static final int SCROLLBAR_WIDTH = 12;
    public static final int SCROLLBAR_OFFSET = 4;

    private final Vector2i pos;
    private final int lineHeight;
    private boolean shouldDrawBackground;

    public BaseListWidget(MinecraftClient mc, Vector2i pos, Vector2i size, int lineHeight) {
        super(mc, size.x, size.y, pos.y, mc.textRenderer.fontHeight + lineHeight * 2);

        this.pos = pos;
        this.lineHeight = lineHeight;
        this.shouldDrawBackground = true;
    }

    @Override
    public int getRowWidth() {
        return this.width - SCROLLBAR_WIDTH;
    }

    @Override
    public int getRowLeft() {
        return this.getX() + 2;
    }

    @Override
    protected int getDefaultScrollbarX() {
        return this.getRowLeft() + this.getRowWidth() + SCROLLBAR_OFFSET;
    }

    @Override
    public void drawMenuListBackground(DrawContext context) {
        if (!this.shouldDrawBackground) return;

        int marginX = -1;
        int marginY = -1;
        int color = 0x40000000;
        context.fillGradient(this.getX() + marginX, this.getY() + marginY, this.getX() + this.getWidth() - marginX, this.getBottom() - marginY, color, color);
    }

    @Override
    public void drawHeaderAndFooterSeparators(DrawContext context) {
        Vector2f scale = this.getScale();
        float width = scale.x;
        float height = scale.y;
        float offsetX = -1 / scale.x;
        float offsetY = -1 / scale.y;
        int color = 0xFF231F26;

        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getX() + offsetX, this.getY() + offsetY, this.getX() + offsetX + width, this.getBottom() - offsetY, color); // Left
        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getRight() - offsetX - width, this.getY() + offsetY, this.getRight() - offsetX, this.getBottom() - offsetY, color); // Right

        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getX() + offsetX, this.getY() + offsetY, this.getX() + this.getWidth() - offsetX, this.getY() + offsetY + height, color); // Top
        RenderHelper.drawTessellatorRectangle(context.getMatrices(), this.getX() + offsetX, this.getBottom() - offsetY - height, this.getX() + this.getWidth() - offsetX, this.getBottom() - offsetY, color); // Bottom
    }

    public Vector2f getScale() {
        return new Vector2f(1, 1);
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
        public final Vector2i offset;
        public final Formatting chevronFormat;
        public final Formatting selectedItemFormat;

        public abstract Text getText();

        public BaseListEntry(Vector2i offset, Formatting chevronFormat, Formatting selectedItemFormat) {
            this.offset = offset;
            this.chevronFormat = chevronFormat;
            this.selectedItemFormat = selectedItemFormat;
        }

        public BaseListEntry(Formatting chevronFormat, Formatting selectedItemFormat) {
            this(new Vector2i(0, 0), chevronFormat, selectedItemFormat);
        }

        public BaseListEntry(Vector2i offset) {
            this(offset, Formatting.WHITE, Formatting.RESET);
        }

        public BaseListEntry() {
            this(Formatting.WHITE, Formatting.RESET);
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        public List<? extends Element> children() {
            return Collections.emptyList();
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
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            BaseListWidget.this.setSelected(this);
            return true;
        }

        @Override
        public void render(DrawContext context, int entryIdx, int top, int left, int entryWidth, int height, int mouseX, int mouseY, boolean flag, float partialTick) {
            MutableText text = this.getText().copy();

            if (this.isSelected()) text = this.getSelectedPrependText().append(text.formatted(this.selectedItemFormat));
            else text = this.getPrependText().append(text);

            Vector2i pos = new Vector2i(left + lineHeight, top + 2);
            RenderHelper.drawTextClipped(text, client.textRenderer, context, pos, width, 0xFFFFFF);
        }

        public int getHeight() {
            return BaseListWidget.this.itemHeight;
        }

        public Vector2i getOffset() {
            return this.offset;
        }

        public MutableText getPrependText() {
            return Text.empty();
        }

        public MutableText getSelectedPrependText() {
            return Text.empty().append(Text.literal("> ").formatted(this.chevronFormat, Formatting.BOLD));
        }

        public boolean isSelected() {
            return getSelectedOrNull() == this;
        }
    }
}
