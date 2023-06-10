package net.drgmes.dwm.utils.base.screens;

import com.google.common.collect.ImmutableList;
import net.drgmes.dwm.utils.helpers.ScreenHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec2f;

import java.util.Collections;
import java.util.List;

public abstract class BaseListWidget extends ElementListWidget<BaseListWidget.BaseListEntry> {
    private final TextRenderer textRenderer;
    private final Vec2f pos;
    private final int padding;

    public BaseListWidget(MinecraftClient mc, TextRenderer textRenderer, int width, int height, int padding, Vec2f pos) {
        super(mc, width, height, (int) pos.y, (int) pos.y + height, textRenderer.fontHeight + padding * 2);

        this.textRenderer = textRenderer;
        this.pos = pos;
        this.padding = padding;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.getRowRight() - 8;
    }

    public void init() {
        this.refreshList();
        this.setLeftPos((int) this.pos.x);
        this.setRenderHorizontalShadows(false);
        this.setRenderBackground(false);
    }

    public void refreshList() {
        this.setScrollAmount(0);
        this.clearEntries();
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
        public void render(MatrixStack matrixStack, int entryIdx, int top, int left, int entryWidth, int height, int mouseX, int mouseY, boolean flag, float partialTick) {
            MutableText text = this.getText().copy();
            if (this.isSelected()) text = Text.empty().append(Text.literal("> ").formatted(this.chevronFormat, Formatting.BOLD)).append(text.formatted(this.selectedItemFormat));

            Vec2f pos = new Vec2f(left + padding, top + 2);
            ScreenHelper.drawClipped(client, matrixStack, text, pos, (int) textRenderer.fontHeight + padding, width, 0xFFFFFF);
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
    }
}
