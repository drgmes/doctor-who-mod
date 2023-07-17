package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TardisConsoleUnitMonitorConsoleMainScreen extends BaseTardisConsoleUnitMonitorScreen {
    private enum EActions {
        SETTINGS(DWM.TEXTS.MONITOR_ACTION_SETTINGS, true, ModBlocks.TARDIS_EXTERIOR_POLICE_BOX::getBlock, (screen) -> {
        }),

        WAYPOINTS(DWM.TEXTS.MONITOR_ACTION_WAYPOINTS, true, () -> Items.COMPASS, (screen) -> {
        }),

        RESEARCHER(DWM.TEXTS.MONITOR_ACTION_RESEARCHER, true, () -> Items.SPYGLASS, (screen) -> {
        }),

        ROOMS(DWM.TEXTS.MONITOR_ACTION_CONSOLE_ROOMS, ModBlocks.TARDIS_ARS_CREATOR::getBlockItem, (screen) -> {
            screen.client.setScreen(new TardisConsoleUnitMonitorConsoleRoomsScreen(screen.tardisConsoleUnitBlockEntity, screen.tardisId, screen.tag, screen));
        }),

        EXTERNAL_MONITOR(DWM.TEXTS.MONITOR_ACTION_EXTERNAL_MONITOR, true, () -> Blocks.OBSERVER, (screen) -> {
        });

        private final Text title;
        private final boolean disabled;
        private final Supplier<ItemConvertible> iconSupplier;
        private final Consumer<TardisConsoleUnitMonitorConsoleMainScreen> onPress;

        EActions(Text title, boolean disabled, Supplier<ItemConvertible> iconSupplier, Consumer<TardisConsoleUnitMonitorConsoleMainScreen> onPress) {
            this.title = title;
            this.disabled = disabled;
            this.iconSupplier = iconSupplier;
            this.onPress = onPress;
        }

        EActions(Text title, Supplier<ItemConvertible> iconSupplier, Consumer<TardisConsoleUnitMonitorConsoleMainScreen> onPress) {
            this(title, false, iconSupplier, onPress);
        }
    }

    private final NbtCompound tag;
    private final String tardisId;
    private final String owner;
    private final Map<EActions, ButtonWidget> buttons = new HashMap<>();

    public TardisConsoleUnitMonitorConsoleMainScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, String owner, NbtCompound tag) {
        super(DWM.TEXTS.MONITOR_TITLE, tardisConsoleUnitBlockEntity);

        this.tardisId = tardisId;
        this.owner = owner;
        this.tag = tag;
    }

    @Override
    protected void init() {
        super.init();
        this.buttons.clear();

        int cols = 6;
        int bgSize = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2;

        int buttonPadding = 2;
        int buttonsCount = EActions.values().length;
        int buttonSize = bgSize / cols - buttonPadding;
        int buttonsMargin = buttonsCount >= cols ? buttonPadding : (bgSize - buttonSize * buttonsCount) / buttonsCount - 1;

        int index = 0;
        int startButtonPosX = this.getBackgroundSize().x / 2 - ((buttonSize + buttonsMargin) * Math.min(cols, buttonsCount) - buttonsMargin) / 2;
        int startButtonPosY = this.getBackgroundSize().y - this.getBackgroundBorderSize().y - buttonSize - buttonsMargin / 2;
        Vector2i startButtonPos = this.getRenderPos(startButtonPosX, startButtonPosY);

        for (EActions action : EActions.values()) {
            ButtonWidget button = RenderHelper.getButtonWidget(
                startButtonPos.x + (buttonSize + buttonsMargin) * (index % cols),
                startButtonPos.y - (buttonSize + buttonsMargin) * ((int) Math.ceil((double) ++index / cols) - 1),
                buttonSize,
                buttonSize,
                Text.empty(),
                (b) -> action.onPress.accept(this)
            );

            button.active = !action.disabled;
            button.setTooltip(Tooltip.of(action.title));
            this.addDrawableChild(button);
            buttons.put(action, button);
        }
    }

    @Override
    public void renderElements(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderElements(context, mouseX, mouseY, delta);
        this.renderData(context);
    }

    @Override
    public void renderElementsAfter(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderElementsAfter(context, mouseX, mouseY, delta);
        this.renderCustomButtons(context);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    private void renderData(DrawContext context) {
        List<Text> datas = new ArrayList<>();
        datas.add(Text.empty().append(DWM.TEXTS.MONITOR_DATA_ID.copy().append(": ").formatted(Formatting.AQUA)).append(Text.literal(this.tardisId).formatted(Formatting.RESET)));
        datas.add(Text.empty().append(DWM.TEXTS.MONITOR_DATA_OWNER.copy().append(": ").formatted(Formatting.AQUA)).append(Text.literal(this.owner).formatted(Formatting.RESET)));

        float scale = 0.915F;
        int index = 0;
        int padding = 5;
        int maxTextLength = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - padding * 2;
        Vector2i pos = this.getRenderPos(this.getBackgroundBorderSize().x + padding, this.getBackgroundBorderSize().y + padding);
        Vector2i scaledPos = pos.div(scale, new Vector2i());

        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);

        for (Text line : datas) {
            scaledPos.add(RenderHelper.drawTextMultiline(line, this.textRenderer, context, scaledPos, this.textRenderer.fontHeight, (int) Math.floor(maxTextLength / scale), 0xE0E0E0));
            scaledPos.add(0, padding / 2);
        }

        context.getMatrices().pop();
    }

    private void renderCustomButtons(DrawContext context) {
        int color = 0xD5000000;

        for (Map.Entry<EActions, ButtonWidget> entry : this.buttons.entrySet()) {
            int padding = 3;
            Vector2i bgPos1 = new Vector2i(entry.getValue().getX() + padding, entry.getValue().getY() + padding);
            Vector2i bgPos2 = new Vector2i(bgPos1.x + entry.getValue().getWidth() - padding * 2, bgPos1.y + entry.getValue().getHeight() - padding * 2);
            context.fillGradient(bgPos1.x, bgPos1.y, bgPos2.x, bgPos2.y, color, color);

            float scale = 1.25F;
            Vec2f iconOffset = new Vec2f((float) entry.getValue().getWidth() / 2 - 9, (float) entry.getValue().getHeight() / 2 - 9);
            Vector2i iconPos = new Vector2i((int) Math.floor(entry.getValue().getX() / scale), (int) Math.floor(entry.getValue().getY() / scale));
            context.getMatrices().push();
            context.getMatrices().scale(scale, scale, 1);
            context.getMatrices().translate(iconOffset.x / scale, iconOffset.y / scale, 0);
            context.drawItem(new ItemStack(entry.getKey().iconSupplier.get()), iconPos.x, iconPos.y);
            context.getMatrices().pop();
        }
    }
}
