package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorConsoleRoomApplyPacket;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.ScreenHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TardisConsoleUnitMonitorConsoleRoomsScreen extends BaseTardisConsoleUnitMonitorScreen {
    private static final Map<String, Identifier> LOADED_CONSOLE_ROOMS_IMAGES = new HashMap<>();

    private final String tardisId;
    private final String currentConsoleRoomId;

    private final List<TardisConsoleRoomEntry> consoleRooms;
    private int selectedConsoleRoomIndex;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;
    private ButtonWidget prevButton;
    private ButtonWidget nextButton;

    public TardisConsoleUnitMonitorConsoleRoomsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, String consoleRoomId, int selectedConsoleRoomIndex, List<TardisConsoleRoomEntry> consoleRooms) {
        super(DWM.TEXTS.MONITOR_NAME_CONSOLE_ROOMS, tardisConsoleUnitBlockEntity);

        this.tardisId = tardisId;
        this.currentConsoleRoomId = consoleRoomId;
        this.selectedConsoleRoomIndex = selectedConsoleRoomIndex;
        this.consoleRooms = Collections.unmodifiableList(consoleRooms);
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2 - 1;
        int buttonOffset = (int) (this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT - 1);

        Vec2f cancelButtonPos = this.getRenderPos(BACKGROUND_BORDERS + 1, buttonOffset);
        this.cancelButton = ScreenHelper.getButtonWidget((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_CANCEL, (b) -> this.close());

        Vec2f acceptButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 2, buttonOffset);
        this.acceptButton = ScreenHelper.getButtonWidget((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_ACCEPT, (b) -> this.apply());

        Vec2f prevButtonPos = this.getRenderPos(BACKGROUND_BORDERS + 1, buttonOffset - BUTTON_HEIGHT - 1);
        this.prevButton = ScreenHelper.getButtonWidget((int) prevButtonPos.x, (int) prevButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_PREV, (b) -> {
            this.selectedConsoleRoomIndex = this.selectedConsoleRoomIndex + 1 < this.consoleRooms.size() ? this.selectedConsoleRoomIndex + 1 : 0;
            this.updateAcceptButton();
        });

        Vec2f nextButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 2, buttonOffset - BUTTON_HEIGHT - 1);
        this.nextButton = ScreenHelper.getButtonWidget((int) nextButtonPos.x, (int) nextButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_NEXT, (b) -> {
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
        float padding = BACKGROUND_BORDERS * 1.25F;
        TardisConsoleRoomEntry selectedConsoleRoom = this.getSelectedConsoleRoom();

        if (selectedConsoleRoom == null) return;

        Vec2f titlePos = this.getRenderPos(padding, padding);
        ScreenHelper.draw(selectedConsoleRoom.getTitle(), this.textRenderer, context, titlePos.x, titlePos.y, 0xE0E0E0, true);

        Vec2f imagePos = this.getRenderPos(padding, padding + this.getTextRenderer().fontHeight * 1.5F);
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
            if (remoteConsoleRoomImage != null) this.drawImage(context, imagePos, new Vec2f(this.getBackgroundSize().x - padding * 2, 120), remoteConsoleRoomImage);
            return;
        }

        this.drawImage(context, imagePos, new Vec2f(this.getBackgroundSize().x - padding * 2, 120), localConsoleRoomImage);
    }
}
