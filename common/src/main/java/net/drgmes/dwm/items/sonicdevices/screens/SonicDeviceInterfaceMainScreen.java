package net.drgmes.dwm.items.sonicdevices.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.network.server.SonicDeviceUpdatePacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Vector2i;

import java.util.List;

public class SonicDeviceInterfaceMainScreen extends BaseSonicDeviceInterfaceScreen {
    private final List<SonicDevice.EMode> modes;

    private SonicDeviceModesListWidget modesListWidget;
    private SonicDeviceModesListWidget.SonicDeviceModeEntry selected = null;

    public SonicDeviceInterfaceMainScreen(ItemStack sonicDeviceItemStack, String slot) {
        super(DWM.TEXTS.SONIC_DEVICE_INTERFACE_NAME, sonicDeviceItemStack, slot);
        this.modes = List.of(SonicDevice.EMode.values());
    }

    @Override
    protected void init() {
        Vector2i modesListSize = this.getModesListSize();
        this.modesListWidget = new SonicDeviceModesListWidget(this, modesListSize.x, modesListSize.y, this.getModesListPos());

        this.addDrawableChild(this.modesListWidget);
        super.init();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.modesListWidget.refreshList();
    }

    @Override
    public void tick() {
        this.modesListWidget.setSelected(this.selected);
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);

        int modesListBackgroundColor = 0x40000000;
        Vector2i modesListPos1 = this.getModesListPos();
        Vector2i modesListPos2 = this.getModesListSize();
        context.fillGradient(modesListPos1.x, modesListPos1.y, modesListPos1.x + modesListPos2.x, modesListPos1.y + modesListPos2.y, modesListBackgroundColor, modesListBackgroundColor);

        if (this.selected == null) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        int padding = 5;
        int lineHeight = mc.textRenderer.fontHeight;
        int maxTextLength = this.getBackgroundSize().x - modesListPos2.x - padding * 2 - this.getBackgroundBorderSize().x * 2;

        Vector2i modeTextPos = new Vector2i(modesListPos1.x + modesListPos2.x + padding, (int) Math.floor(modesListPos1.y + padding + 1.5F));
        Text modeText = DWM.TEXTS.SONIC_DEVICE_INTERFACE_BTN_MODE.apply(this.selected.mode).copy();

        modeTextPos = modeTextPos.add(RenderHelper.drawTextMultiline(modeText, mc.textRenderer, context, modeTextPos, lineHeight, maxTextLength, 0xFFFFFF));

        float modeDescriptionScale = 0.75F;
        Vector2i modeDescriptionPos = new Vector2i((int) Math.floor(modeTextPos.x / modeDescriptionScale), (int) Math.floor((modeTextPos.y + padding * 2 - 0.5F) / modeDescriptionScale));
        Text modeDescription = this.selected.mode.getDescription();

        context.getMatrices().push();
        context.getMatrices().scale(modeDescriptionScale, modeDescriptionScale, modeDescriptionScale);
        RenderHelper.drawTextMultiline(modeDescription, mc.textRenderer, context, modeDescriptionPos, lineHeight, (int) Math.floor(maxTextLength / modeDescriptionScale), 0xFFFFFF);
        context.getMatrices().pop();
    }

    protected void setSelected(SonicDeviceModesListWidget.SonicDeviceModeEntry entry) {
        this.selected = entry;
        this.apply();
    }

    protected void apply() {
        if (this.selected == null) return;

        this.mode = this.selected.mode;
        SonicDevice.setInteractionMode(this.sonicDeviceItemStack, this.selected.mode);
        new SonicDeviceUpdatePacket(this.sonicDeviceItemStack, this.slot).sendToServer();
    }

    private Vector2i getModesListSize() {
        return new Vector2i(100, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2);
    }

    private Vector2i getModesListPos() {
        return this.getRenderPos(this.getBackgroundBorderSize().x, this.getBackgroundSize().y - this.getModesListSize().y - this.getBackgroundBorderSize().y);
    }

    private static class SonicDeviceModesListWidget extends BaseListWidget {
        private final SonicDeviceInterfaceMainScreen parent;

        public SonicDeviceModesListWidget(SonicDeviceInterfaceMainScreen parent, int width, int height, Vector2i pos) {
            super(parent.client, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();

            this.parent.modes.forEach((mode) -> {
                SonicDeviceModeEntry entry = new SonicDeviceModeEntry(mode);
                this.addEntry(entry);

                if (mode == this.parent.mode) {
                    this.setSelected(entry);
                    this.parent.selected = entry;
                }
            });
        }

        private class SonicDeviceModeEntry extends BaseListEntry {
            private final SonicDevice.EMode mode;

            public SonicDeviceModeEntry(SonicDevice.EMode mode) {
                super(Formatting.WHITE, Formatting.GOLD);
                this.mode = mode;
            }

            @Override
            public Text getText() {
                return this.mode.getTitle();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                SonicDeviceModesListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}
