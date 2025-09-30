package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.net.URI;
import java.util.*;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorConsoleRoomsScreen extends BaseTardisConsoleUnitMonitorScreen {
    protected static final int LINE_PADDING = 3;

    private static final Map<String, Identifier> LOADED_CONSOLE_ROOMS_IMAGES = new HashMap<>();

    private final Screen parentScreen;
    private final String tardisId;
    private final String currentConsoleRoomId;
    private final List<TardisConsoleRoomEntry> consoleRooms = new ArrayList<>();

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    private ConsoleRoomsListWidget consoleRoomsListWidget;
    private ConsoleRoomsListWidget.ConsoleRoomEntry selected = null;

    public TardisConsoleUnitMonitorConsoleRoomsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, NbtCompound tag, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_CONSOLE_ROOMS_TITLE, tardisConsoleUnitBlockEntity);

        this.parentScreen = parentScreen;
        this.tardisId = tardisId;
        this.currentConsoleRoomId = tag.getCompound("tardisTag").getString("consoleRoom");

        NbtCompound roomsTag = tag.getCompound("roomsTag");
        List<String> keys = new ArrayList<>(roomsTag.getKeys());

        keys.sort(Comparator.comparing((key) -> key));
        keys.forEach((key) -> this.consoleRooms.add(TardisConsoleRoomEntry.fromNbt(roomsTag.getCompound(key))));
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        this.consoleRoomsListWidget = new ConsoleRoomsListWidget(this, this.getConsoleRoomsListPos(), this.getConsoleRoomsListSize());

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + 1, BUTTON_SIZE + 1);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_CONSOLE_ROOMS_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = acceptButtonPos.add(-BUTTON_SIZE - 1, 0);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_CONSOLE_ROOMS_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.consoleRoomsListWidget);
        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.cancelButton);
        this.update();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.consoleRoomsListWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);
        if (this.selected == null) return;

        int listWidth = this.getConsoleRoomsListSize().x;
        int imageWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - listWidth - 2;
        int imageHeight = (int) Math.floor(imageWidth * 0.595F);

        Vector2i imagePos = this.getLeftTopRenderPos(listWidth + 2, 0);
        Identifier localConsoleRoomImage = DWM.getIdentifier("images/tardis/console_rooms/" + this.selected.consoleRoom.name + ".png");

        RenderHelper.drawTessellatorRectangle(context.getMatrices(), imagePos.x - 1, imagePos.y + imageHeight + 1, imagePos.x + imageWidth + 1, imagePos.y + imageHeight + 1.795F, 0xFF231F26);

        if (!this.selected.consoleRoom.imageUrl.isEmpty()) {
            if (!LOADED_CONSOLE_ROOMS_IMAGES.containsKey(this.selected.consoleRoom.name)) {
                LOADED_CONSOLE_ROOMS_IMAGES.put(this.selected.consoleRoom.name, null);

                CommonHelper.runInThread("loadRemoteImage_" + this.selected.consoleRoom.name, () -> {
                    try {
                        LOADED_CONSOLE_ROOMS_IMAGES.put(this.selected.consoleRoom.name, CommonHelper.loadRemoteImage(this.selected.consoleRoom.name, new URI(this.selected.consoleRoom.imageUrl).toURL()));
                    } catch (Exception ignored) {
                        LOADED_CONSOLE_ROOMS_IMAGES.remove(this.selected.consoleRoom.name);
                    }
                });

                return;
            }

            Identifier remoteConsoleRoomImage = LOADED_CONSOLE_ROOMS_IMAGES.get(this.selected.consoleRoom.name);
            if (remoteConsoleRoomImage != null) RenderHelper.drawImage(context, imagePos, new Vector2i(imageWidth, imageHeight), remoteConsoleRoomImage);
            return;
        }

        RenderHelper.drawImage(context, imagePos, new Vector2i(imageWidth, imageHeight), localConsoleRoomImage);
    }

    @Override
    public void tick() {
        this.consoleRoomsListWidget.setSelected(this.selected);
    }

    @Override
    public void apply() {
        if (this.selected != null && !this.selected.consoleRoom.name.equals(this.currentConsoleRoomId)) {
            this.client.setScreen(new TardisConsoleUnitMonitorConsoleRoomsConfirmationScreen(this.tardisConsoleUnitBlockEntity, this.tardisId, this.selected.consoleRoom.name, this));
        }
    }

    @Override
    public void back() {
        if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
        else super.back();
    }

    private Vector2i getConsoleRoomsListPos() {
        return this.getLeftTopRenderPos(0, 0);
    }

    private Vector2i getConsoleRoomsListSize() {
        return new Vector2i(125, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2);
    }

    private void update() {
        this.acceptButton.active = this.selected != null && !this.selected.consoleRoom.name.equals(this.currentConsoleRoomId);
    }

    private void setSelected(ConsoleRoomsListWidget.ConsoleRoomEntry entry) {
        this.selected = entry;
        this.update();
    }

    private static class ConsoleRoomsListWidget extends BaseListWidget {
        private final TardisConsoleUnitMonitorConsoleRoomsScreen parent;

        public ConsoleRoomsListWidget(TardisConsoleUnitMonitorConsoleRoomsScreen parent, Vector2i pos, Vector2i size) {
            super(parent.client, pos, size, LINE_PADDING);
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

            this.parent.consoleRooms.forEach((consoleRoom) -> {
                ConsoleRoomEntry entry = new ConsoleRoomEntry(consoleRoom);
                this.addEntry(entry);

                boolean isEqualCurrent = this.parent.selected == null && Objects.equals(consoleRoom.name, this.parent.currentConsoleRoomId);
                boolean isEqualSelected = this.parent.selected != null && Objects.equals(consoleRoom.name, this.parent.selected.consoleRoom.name);

                if (isEqualCurrent || isEqualSelected) {
                    this.setSelected(entry);
                    this.parent.selected = entry;
                }
            });
        }

        private class ConsoleRoomEntry extends BaseListEntry {
            private final TardisConsoleRoomEntry consoleRoom;

            public ConsoleRoomEntry(TardisConsoleRoomEntry consoleRoom) {
                super(Formatting.WHITE, Formatting.GOLD);
                this.consoleRoom = consoleRoom;
            }

            @Override
            public Text getText() {
                return this.consoleRoom.getTitle();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                ConsoleRoomsListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}
