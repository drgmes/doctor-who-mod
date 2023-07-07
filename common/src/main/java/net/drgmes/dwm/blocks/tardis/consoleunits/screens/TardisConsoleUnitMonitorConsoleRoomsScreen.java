package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorConsoleRoomApplyPacket;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.net.URL;
import java.util.*;

public class TardisConsoleUnitMonitorConsoleRoomsScreen extends BaseTardisConsoleUnitMonitorScreen {
    private static final Map<String, Identifier> LOADED_CONSOLE_ROOMS_IMAGES = new HashMap<>();

    private final Screen screen;
    private final String tardisId;
    private final String currentConsoleRoomId;

    private final List<TardisConsoleRoomEntry> consoleRooms = new ArrayList<>();
    private int selectedConsoleRoomIndex = 0;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;
    private ButtonWidget prevButton;
    private ButtonWidget nextButton;

    public TardisConsoleUnitMonitorConsoleRoomsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, NbtCompound tag, @Nullable Screen screen) {
        super(DWM.TEXTS.MONITOR_ACTION_CONSOLE_ROOMS, tardisConsoleUnitBlockEntity);

        this.screen = screen;
        this.tardisId = tardisId;
        this.currentConsoleRoomId = tag.getCompound("tardisTag").getString("consoleRoom");

        NbtCompound roomsTag = tag.getCompound("roomsTag");
        List<String> keys = new ArrayList<>(roomsTag.getKeys().stream().toList());

        keys.sort(Comparator.comparing((key) -> key));
        keys.forEach((key) -> this.consoleRooms.add(TardisConsoleRoomEntry.create(roomsTag.getCompound(key), false)));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = this.getBackgroundSize().x / 2 - this.getBackgroundBorderSize().x - 1;
        int buttonOffset = this.getBackgroundSize().y - this.getBackgroundBorderSize().y - BUTTON_HEIGHT - 1;

        Vector2i cancelButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + 1, buttonOffset);
        this.cancelButton = RenderHelper.getButtonWidget(cancelButtonPos.x, cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_CANCEL, (b) -> {
            if (this.screen != null) this.client.setScreen(this.screen);
            else this.close();
        });

        Vector2i acceptButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + buttonWidth + 2, buttonOffset);
        this.acceptButton = RenderHelper.getButtonWidget(acceptButtonPos.x, acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i prevButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + 1, buttonOffset - BUTTON_HEIGHT - 1);
        this.prevButton = RenderHelper.getButtonWidget(prevButtonPos.x, prevButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_PREV, (b) -> {
            this.selectedConsoleRoomIndex = this.selectedConsoleRoomIndex + 1 < this.consoleRooms.size() ? this.selectedConsoleRoomIndex + 1 : 0;
            this.updateAcceptButton();
        });

        Vector2i nextButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + buttonWidth + 2, buttonOffset - BUTTON_HEIGHT - 1);
        this.nextButton = RenderHelper.getButtonWidget(nextButtonPos.x, nextButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_NEXT, (b) -> {
            this.selectedConsoleRoomIndex = this.selectedConsoleRoomIndex - 1 >= 0 ? this.selectedConsoleRoomIndex - 1 : this.consoleRooms.size() - 1;
            this.updateAcceptButton();
        });

        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.prevButton);
        this.addDrawableChild(this.nextButton);

        this.updateAcceptButton();
    }

    @Override
    protected void apply() {
        TardisConsoleRoomEntry selectedConsoleRoom = this.getSelectedConsoleRoom();

        if (selectedConsoleRoom != null && !selectedConsoleRoom.name.equals(this.currentConsoleRoomId)) {
            new TardisConsoleUnitMonitorConsoleRoomApplyPacket(this.tardisId, selectedConsoleRoom.name).sendToServer();
        }

        super.apply();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);
        this.renderImage(context);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Nullable
    private TardisConsoleRoomEntry getSelectedConsoleRoom() {
        return this.consoleRooms.size() > this.selectedConsoleRoomIndex ? this.consoleRooms.get(this.selectedConsoleRoomIndex) : null;
    }

    private void updateAcceptButton() {
        TardisConsoleRoomEntry selectedConsoleRoom = this.getSelectedConsoleRoom();
        this.acceptButton.active = selectedConsoleRoom != null && !selectedConsoleRoom.name.equals(this.currentConsoleRoomId);
    }

    private void renderImage(DrawContext context) {
        TardisConsoleRoomEntry selectedConsoleRoom = this.getSelectedConsoleRoom();
        int paddingX = (int) Math.floor(this.getBackgroundBorderSize().x * 1.25F);
        int paddingY = (int) Math.floor(this.getBackgroundBorderSize().y * 1.25F);

        if (selectedConsoleRoom == null) return;

        Vector2i titlePos = this.getRenderPos(paddingX, paddingY);
        RenderHelper.drawText(selectedConsoleRoom.getTitle(), this.textRenderer, context, titlePos.x, titlePos.y, 0xE0E0E0, true);

        Vector2i imagePos = this.getRenderPos(paddingX, (int) Math.floor(paddingY + this.getTextRenderer().fontHeight * 1.5F));
        Identifier localConsoleRoomImage = DWM.getIdentifier("images/tardis/console_rooms/" + selectedConsoleRoom.name + ".png");

        if (!selectedConsoleRoom.imageUrl.isEmpty()) {
            if (!LOADED_CONSOLE_ROOMS_IMAGES.containsKey(selectedConsoleRoom.name)) {
                LOADED_CONSOLE_ROOMS_IMAGES.put(selectedConsoleRoom.name, null);

                CommonHelper.runInThread("loadRemoteImage_" + selectedConsoleRoom.name, () -> {
                    try {
                        LOADED_CONSOLE_ROOMS_IMAGES.put(selectedConsoleRoom.name, CommonHelper.loadRemoteImage(selectedConsoleRoom.name, new URL(selectedConsoleRoom.imageUrl)));
                    } catch (Exception ignored) {
                        LOADED_CONSOLE_ROOMS_IMAGES.remove(selectedConsoleRoom.name);
                    }
                });

                return;
            }

            Identifier remoteConsoleRoomImage = LOADED_CONSOLE_ROOMS_IMAGES.get(selectedConsoleRoom.name);
            if (remoteConsoleRoomImage != null) this.drawImage(context, imagePos, new Vector2i(this.getBackgroundSize().x - paddingX * 2, 120), remoteConsoleRoomImage);
            return;
        }

        this.drawImage(context, imagePos, new Vector2i(this.getBackgroundSize().x - paddingX * 2, 120), localConsoleRoomImage);
    }
}
