package net.drgmes.dwm.items.screwdriver.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.network.server.ScrewdriverUpdatePacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.ScreenHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec2f;

import java.util.List;

public class ScrewdriverInterfaceMainScreen extends BaseScrewdriverInterfaceScreen {
    private final List<Screwdriver.EScrewdriverMode> modes;

    private ScrewdriverModesListWidget modesListWidget;
    private ScrewdriverModesListWidget.ScrewdriverModeEntry selected = null;

    public ScrewdriverInterfaceMainScreen(ItemStack screwdriverItemStack, boolean isMainHand) {
        super(DWM.TEXTS.SCREWDRIVER_INTERFACE_NAME, screwdriverItemStack, isMainHand);
        this.modes = List.of(Screwdriver.EScrewdriverMode.values());
    }

    @Override
    protected void init() {
        Vec2f modesListSize = this.getModesListSize();
        this.modesListWidget = new ScrewdriverModesListWidget(this, (int) modesListSize.x, (int) modesListSize.y, this.getModesListPos());

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
    public void renderAdditional(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.renderAdditional(matrixStack, mouseX, mouseY, delta);

        int modesListBackgroundColor = 0x40000000;
        Vec2f modesListPos1 = this.getModesListPos();
        Vec2f modesListPos2 = this.getModesListSize();
        this.fillBackgroundGradient(matrixStack, (int) modesListPos1.x, (int) modesListPos1.y, (int) (modesListPos1.x + modesListPos2.x), (int) (modesListPos1.y + modesListPos2.y), modesListBackgroundColor, modesListBackgroundColor);

        if (this.selected == null) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        int padding = 5;
        float lineHeight = mc.textRenderer.fontHeight;
        float maxTextLength = this.getBackgroundSize().x - modesListPos2.x - padding * 2 - BACKGROUND_BORDERS * 2;

        Vec2f modeTextPos = new Vec2f(modesListPos1.x + modesListPos2.x + padding, modesListPos1.y + padding + 1.5F);
        Text modeText = DWM.TEXTS.SCREWDRIVER_INTERFACE_BTN_MODE.apply(this.selected.mode).copy();

        modeTextPos = modeTextPos.add(ScreenHelper.drawMultiline(mc, matrixStack, modeText, modeTextPos, (int) lineHeight, (int) maxTextLength, 0xFFFFFF));

        float modeDescriptionScale = 0.75F;
        Vec2f modeDescriptionPos = new Vec2f(modeTextPos.x / modeDescriptionScale, (modeTextPos.y + padding * 2 - 0.5F) / modeDescriptionScale);
        Text modeDescription = this.selected.mode.getDescription();

        matrixStack.push();
        matrixStack.scale(modeDescriptionScale, modeDescriptionScale, modeDescriptionScale);
        ScreenHelper.drawMultiline(mc, matrixStack, modeDescription, modeDescriptionPos, (int) lineHeight, (int) (maxTextLength / modeDescriptionScale), 0xFFFFFF);
        matrixStack.pop();
    }

    protected void setSelected(ScrewdriverModesListWidget.ScrewdriverModeEntry entry) {
        this.selected = entry;
        this.apply();
    }

    protected void apply() {
        if (this.selected == null) return;

        this.mode = this.selected.mode;
        Screwdriver.setInteractionMode(this.screwdriverItemStack, this.selected.mode);
        new ScrewdriverUpdatePacket(this.screwdriverItemStack, this.isMainHand).sendToServer();
    }

    private Vec2f getModesListSize() {
        return new Vec2f(100, (int) this.getBackgroundSize().y - BACKGROUND_BORDERS * 2);
    }

    private Vec2f getModesListPos() {
        return this.getRenderPos(BACKGROUND_BORDERS, (int) this.getBackgroundSize().y - this.getModesListSize().y - BACKGROUND_BORDERS);
    }

    private static class ScrewdriverModesListWidget extends BaseListWidget {
        private final ScrewdriverInterfaceMainScreen parent;

        public ScrewdriverModesListWidget(ScrewdriverInterfaceMainScreen parent, int width, int height, Vec2f pos) {
            super(parent.client, parent.textRenderer, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();

            this.parent.modes.forEach((mode) -> {
                ScrewdriverModeEntry entry = new ScrewdriverModeEntry(mode);
                this.addEntry(entry);

                if (mode == this.parent.mode) {
                    this.setSelected(entry);
                    this.parent.selected = entry;
                }
            });
        }

        private class ScrewdriverModeEntry extends BaseListEntry {
            private final Screwdriver.EScrewdriverMode mode;

            public ScrewdriverModeEntry(Screwdriver.EScrewdriverMode mode) {
                super(Formatting.WHITE, Formatting.GOLD);
                this.mode = mode;
            }

            @Override
            public Text getText() {
                return this.mode.getTitle();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                ScrewdriverModesListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}
