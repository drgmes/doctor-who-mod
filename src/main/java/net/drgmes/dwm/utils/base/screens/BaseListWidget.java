package net.drgmes.dwm.utils.base.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.phys.Vec2;

public abstract class BaseListWidget extends ObjectSelectionList<BaseListWidget.BaseListEntry> {
    private final Font font;
    private final Vec2 pos;
    private final int padding;

    public BaseListWidget(Minecraft mc, Font font, int width, int height, int padding, Vec2 pos) {
        super(mc, width, height, (int) pos.y, (int) pos.y + height, font.lineHeight + padding * 2);

        this.font = font;
        this.pos = pos;
        this.padding = padding;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 5;
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    public void init() {
        this.refreshList();
        this.setLeftPos((int) this.pos.x);
        this.setRenderBackground(false);
        this.setRenderTopAndBottom(false);
    }

    public void refreshList() {
        this.setScrollAmount(0);
        this.clearEntries();
    }

    public abstract class BaseListEntry extends ObjectSelectionList.Entry<BaseListEntry> {
        @Override
        public Component getNarration() {
            return null;
        }

        @Override
        public void render(PoseStack poseStack, int entryIdx, int top, int left, int entryWidth, int height, int mouseX, int mouseY, boolean flag, float partialTick) {
            Component name = this.getNarration();
            font.drawShadow(poseStack, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, width))), left + padding, top + 2, 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
            BaseListWidget.this.setSelected(this);
            return false;
        }
    }
}
