package net.drgmes.dwm.items.sonicdevices.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.enums.SonicDeviceMode;
import net.drgmes.dwm.network.server.SonicDeviceModeUpdatePacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public class SonicDeviceInterfaceMainScreen extends BaseSonicDeviceInterfaceScreen {
    private SonicDeviceModesListWidget modesListWidget;
    private SonicDeviceModesListWidget.SonicDeviceModeEntry selected = null;
    private SonicDeviceModeContentWidget modeContentWidget;

    public SonicDeviceInterfaceMainScreen(ItemStack sonicDeviceItemStack, String slot) {
        super(DWM.TEXTS.SONIC_DEVICE_INTERFACE_TITLE, sonicDeviceItemStack, slot);
    }

    @Override
    public void tick() {
        this.modesListWidget.setSelected(this.selected);
    }

    @Override
    protected void init() {
        super.init();

        this.modeContentWidget = new SonicDeviceModeContentWidget(this, this.getModeContentListPos(), this.getModeContentListSize());
        this.modesListWidget = new SonicDeviceModesListWidget(this, this.getModesListPos(), this.getModesListSize());

        this.addDrawableChild(this.modesListWidget);
        this.addDrawableChild(this.modeContentWidget);
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.modesListWidget.refreshList();
        this.modeContentWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);
        if (this.selected == null) return;

        boolean hasScroll = this.modeContentWidget.getMaxScroll() > 0;
        int scroll = (int) this.modeContentWidget.getScrollAmount();

        Vector2i modeContentListPos = this.getModeContentListPos();
        Vector2i modeContentListSize = this.getModeContentListSize();
        Vector2i linePos = new Vector2i(modeContentListPos.x + SCREEN_MARGIN, modeContentListPos.y + this.textRenderer.fontHeight + SCREEN_MARGIN * 2 - scroll);

        if (linePos.y < this.getRenderPos(0, this.getBackgroundBorderSize().y).y) return;
        RenderHelper.drawTessellatorRectangle(context.getMatrices(), linePos.x, linePos.y, linePos.x + modeContentListSize.x - SCREEN_MARGIN * 2 - (hasScroll ? BaseListWidget.SCROLLBAR_WIDTH - BaseListWidget.SCROLLBAR_OFFSET : 0), linePos.y + 1, CommonHelper.getColorWithAlpha(0xFFFFFFFF, 0.75F));
    }

    protected void apply() {
        if (this.selected == null) return;

        this.mode = this.selected.mode;
        SonicDevice.setInteractionMode(this.sonicDeviceItemStack, this.selected.mode);
        new SonicDeviceModeUpdatePacket(this.selected.mode.name(), this.slot).sendToServer();
    }

    private Vector2i getModesListPos() {
        return this.getLeftTopRenderPos(0, 0);
    }

    private Vector2i getModesListSize() {
        return new Vector2i(100, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2);
    }

    private Vector2i getModeContentListPos() {
        return new Vector2i(this.getModesListPos().x + this.getModesListSize().x + 2, this.getModesListPos().y);
    }

    private Vector2i getModeContentListSize() {
        return new Vector2i(this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - this.getModesListSize().x - 2, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2);
    }

    private void setSelected(SonicDeviceModesListWidget.SonicDeviceModeEntry entry) {
        this.selected = entry;
        this.modeContentWidget.refreshList();
        this.apply();
    }

    private static class SonicDeviceModesListWidget extends BaseListWidget {
        private final SonicDeviceInterfaceMainScreen parent;

        public SonicDeviceModesListWidget(SonicDeviceInterfaceMainScreen parent, Vector2i pos, Vector2i size) {
            super(parent.client, pos, size, LINE_HEIGHT);
            this.parent = parent;
            this.init();
        }

        @Override
        public Vector2f getScale() {
            return this.parent.cachedScale;
        }

        @Override
        public void refreshList() {
            super.refreshList();

            List.of(SonicDeviceMode.values()).forEach((mode) -> {
                SonicDeviceModeEntry entry = new SonicDeviceModeEntry(mode);
                this.addEntry(entry);

                if (mode == this.parent.mode) {
                    this.setSelected(entry);
                    this.parent.setSelected(entry);
                }
            });
        }

        private class SonicDeviceModeEntry extends BaseListEntry {
            private final SonicDeviceMode mode;

            public SonicDeviceModeEntry(SonicDeviceMode mode) {
                super(Formatting.WHITE, Formatting.GOLD);
                this.mode = mode;
            }

            @Override
            public Text getText() {
                return this.mode.getTitle();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (!super.mouseClicked(mouseX, mouseY, button)) return false;
                SonicDeviceModesListWidget.this.parent.setSelected(this);
                return true;
            }
        }
    }

    private static class SonicDeviceModeContentWidget extends BaseListWidget {
        private final SonicDeviceInterfaceMainScreen parent;

        public SonicDeviceModeContentWidget(SonicDeviceInterfaceMainScreen parent, Vector2i pos, Vector2i size) {
            super(parent.client, pos, size, 1);
            this.parent = parent;

            this.setShouldDrawBackground(false);
            this.init();
        }

        @Override
        public Vector2f getScale() {
            return this.parent.cachedScale;
        }

        @Override
        public void refreshList() {
            super.refreshList();
            if (this.parent.selected == null) return;

            SonicDeviceMode mode = this.parent.selected.mode;
            int maxTextWidth = this.parent.getModeContentListSize().x - SCREEN_MARGIN * 2 - 2;

            Text modeText = DWM.TEXTS.SONIC_DEVICE_INTERFACE_BTN_MODE.apply(mode).copy();
            List<OrderedText> titleLines = Language.getInstance().reorder(this.parent.textRenderer.getTextHandler().wrapLines(modeText, maxTextWidth, Style.EMPTY));
            titleLines.forEach((line) -> this.addEntry(new SonicDeviceModeContentLineEntry(line)));

            this.addEntry(new SonicDeviceModeContentLineEntry(Text.literal("").asOrderedText()));

            float modeDescriptionScale = 0.75F;
            Text modeDescription = mode.getDescription();
            List<OrderedText> descriptionLines = Language.getInstance().reorder(this.parent.textRenderer.getTextHandler().wrapLines(modeDescription, (int) (maxTextWidth / modeDescriptionScale), Style.EMPTY));
            descriptionLines.forEach((line) -> this.addEntry(new SonicDeviceModeContentLineEntry(line, modeDescriptionScale)));

            if (mode == SonicDeviceMode.SCAN) {
                this.renderRegistryKeys(SonicDevice.getDiscoveredWorlds(this.parent.sonicDeviceItemStack), DWM.TEXTS.SONIC_DEVICE_INTERFACE_SCAN_DIMENSIONS_TITLE);
                this.renderRegistryKeys(SonicDevice.getDiscoveredBiomes(this.parent.sonicDeviceItemStack), DWM.TEXTS.SONIC_DEVICE_INTERFACE_SCAN_BIOMES_TITLE);
                this.renderRegistryKeys(SonicDevice.getDiscoveredStructures(this.parent.sonicDeviceItemStack), DWM.TEXTS.SONIC_DEVICE_INTERFACE_SCAN_STRUCTURES_TITLE);
            }
        }

        @Override
        protected int getMaxPosition() {
            AtomicInteger height = new AtomicInteger();
            this.children().forEach((entry) -> height.addAndGet(entry.getHeight() + entry.getOffset().y));
            return this.headerHeight + this.itemHeight + height.get() - 4;
        }

        @Override
        protected int getRowTop(int index) {
            AtomicInteger height = new AtomicInteger(this.children().get(index).getOffset().y);
            if (index > 0) this.children().subList(0, index).forEach((entry) -> height.addAndGet(entry.getHeight() + entry.getOffset().y));
            return this.getY() + 4 - (int) this.getScrollAmount() + this.headerHeight + height.get();
        }

        @Override
        protected int getRowBottom(int index) {
            BaseListEntry entry = this.getEntry(index);
            return this.getRowTop(index) + entry.getHeight() + entry.getOffset().y;
        }

        @Override
        public void renderEntry(DrawContext context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
            this.getEntry(index).render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, false, delta);
        }

        private void renderRegistryKeys(List<? extends RegistryKey<?>> registryKeys, Text title) {
            if (registryKeys.isEmpty()) return;

            float scale = 0.75F;
            this.addEntry(new SonicDeviceModeContentLineEntry(Text.literal("").asOrderedText()));
            this.addEntry(new SonicDeviceModeContentLineEntry(title.copy().formatted(Formatting.GOLD).asOrderedText()));

            registryKeys.forEach((registryKey) -> {
                this.addEntry(new SonicDeviceModeContentLineEntry(Text.literal(CommonHelper.formatRegistryKey(registryKey)).formatted(Formatting.AQUA).asOrderedText(), scale));
            });
        }

        private class SonicDeviceModeContentLineEntry extends BaseListEntry {
            protected final OrderedText line;
            protected final float scale;

            public SonicDeviceModeContentLineEntry(OrderedText line, float scale, Vector2i offset) {
                super(offset);
                this.line = line;
                this.scale = scale;
            }

            public SonicDeviceModeContentLineEntry(OrderedText line, float scale) {
                this.line = line;
                this.scale = scale;
            }

            public SonicDeviceModeContentLineEntry(OrderedText line) {
                this(line, 1F);
            }

            @Override
            public Text getText() {
                return Text.empty();
            }

            @Override
            public int getHeight() {
                return (int) Math.ceil(super.getHeight() * this.scale);
            }

            @Override
            public void render(DrawContext context, int entryIdx, int y, int x, int entryWidth, int height, int mouseX, int mouseY, boolean isSelected, float partialTick) {
                Vector2i pos = new Vector2i(x + this.offset.x + 2, y + 2).div(this.scale);

                context.getMatrices().push();
                context.getMatrices().scale(this.scale, this.scale, this.scale);
                context.drawText(SonicDeviceModeContentWidget.this.parent.textRenderer, this.line, pos.x, pos.y, 0xFFFFFF, true);
                context.getMatrices().pop();
            }
        }
    }
}
