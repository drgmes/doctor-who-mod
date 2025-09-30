package net.drgmes.dwm.items.sonicdevices.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.enums.SonicDeviceMode;
import net.drgmes.dwm.network.server.SonicDeviceModeUpdatePacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SonicDeviceInterfaceMainScreen extends BaseSonicDeviceInterfaceScreen {
    private SonicDeviceModesListWidget modesListWidget;
    private SonicDeviceModesListWidget.SonicDeviceModeEntry selected = null;

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

        this.modesListWidget = new SonicDeviceModesListWidget(this, this.getModesListPos(), this.getModesListSize());
        this.addDrawableChild(this.modesListWidget);
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.modesListWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);
        if (this.selected == null) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        Vector2i modesListSize = this.getModesListSize();

        int lineHeight = mc.textRenderer.fontHeight;
        int maxTextWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - SCREEN_MARGIN * 2 - modesListSize.x - 2;

        Text modeText = DWM.TEXTS.SONIC_DEVICE_INTERFACE_BTN_MODE.apply(this.selected.mode).copy();
        Vector2i modeTextPos = this.getLeftTopRenderPos(modesListSize.x + SCREEN_MARGIN + 2, SCREEN_MARGIN + 2);
        modeTextPos = modeTextPos.add(RenderHelper.drawTextMultiline(modeText, mc.textRenderer, context, modeTextPos, lineHeight, maxTextWidth, 0xFFFFFF));

        float modeDescriptionScale = 0.75F;
        Text modeDescription = this.selected.mode.getDescription();
        Vector2i modeDescriptionPos = modeTextPos.div(modeDescriptionScale).add(0, lineHeight + 2);

        context.getMatrices().push();
        context.getMatrices().scale(modeDescriptionScale, modeDescriptionScale, modeDescriptionScale);
        RenderHelper.drawTextMultiline(modeDescription, mc.textRenderer, context, modeDescriptionPos, lineHeight, (int) Math.floor(maxTextWidth / modeDescriptionScale), 0xFFFFFF);
        context.getMatrices().pop();
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

    private void setSelected(SonicDeviceModesListWidget.SonicDeviceModeEntry entry) {
        this.selected = entry;
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
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                SonicDeviceModesListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}
