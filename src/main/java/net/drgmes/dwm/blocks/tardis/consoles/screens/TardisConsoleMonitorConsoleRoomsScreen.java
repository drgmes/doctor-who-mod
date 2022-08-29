package net.drgmes.dwm.blocks.tardis.consoles.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.network.TardisConsoleRemoteCallablePackets;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TardisConsoleMonitorConsoleRoomsScreen extends BaseTardisConsoleMonitorScreen {
    private static final Map<String, Identifier> LOADED_CONSOLE_IMAGES = new HashMap<>();

    private final String tardisId;
    private final String currentConsoleRoomId;

    private final List<TardisConsoleRoomEntry> consoleRooms;
    private int selectedConsoleRoomIndex;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;
    private ButtonWidget prevButton;
    private ButtonWidget nextButton;

    public TardisConsoleMonitorConsoleRoomsScreen(BaseTardisConsoleBlockEntity tardisConsoleBlockEntity, String tardisId, String consoleRoomId, int selectedConsoleRoomIndex, List<TardisConsoleRoomEntry> consoleRooms) {
        super(DWM.TEXTS.MONITOR_NAME_CONSOLE_ROOMS, tardisConsoleBlockEntity);

        this.tardisId = tardisId;
        this.currentConsoleRoomId = consoleRoomId;
        this.selectedConsoleRoomIndex = selectedConsoleRoomIndex;
        this.consoleRooms = Collections.unmodifiableList(consoleRooms);
    }

    @Override
    public void renderAdditional(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.renderAdditional(matrixStack, mouseX, mouseY, delta);
        this.renderImage(matrixStack);
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2 - 1;
        int buttonOffset = (int) (this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT - 1);

        Vec2f cancelButtonPos = this.getRenderPos(BACKGROUND_BORDERS + 1, buttonOffset);
        this.cancelButton = new ButtonWidget((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_CANCEL, (b) -> this.close());

        Vec2f acceptButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 2, buttonOffset);
        this.acceptButton = new ButtonWidget((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_ACCEPT, (b) -> this.apply());

        Vec2f prevButtonPos = this.getRenderPos(BACKGROUND_BORDERS + 1, buttonOffset - BUTTON_HEIGHT - 1);
        this.prevButton = new ButtonWidget((int) prevButtonPos.x, (int) prevButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_PREV, (b) -> {
            this.selectedConsoleRoomIndex = this.selectedConsoleRoomIndex + 1 < this.consoleRooms.size() ? this.selectedConsoleRoomIndex + 1 : 0;
            this.updateAcceptButton();
        });

        Vec2f nextButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 2, buttonOffset - BUTTON_HEIGHT - 1);
        this.nextButton = new ButtonWidget((int) nextButtonPos.x, (int) nextButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_BTN_CONSOLE_ROOMS_NEXT, (b) -> {
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

        if (!selectedConsoleRoom.name.equals(this.currentConsoleRoomId)) {
            PacketHelper.sendToServer(
                TardisConsoleRemoteCallablePackets.class,
                "applyTardisConsoleMonitorConsoleRoom",
                this.tardisId, selectedConsoleRoom.name
            );
        }

        super.apply();
    }

    private TardisConsoleRoomEntry getSelectedConsoleRoom() {
        return this.consoleRooms.get(this.selectedConsoleRoomIndex);
    }

    private void updateAcceptButton() {
        this.acceptButton.active = !this.getSelectedConsoleRoom().name.equals(this.currentConsoleRoomId);
    }

    private void renderImage(MatrixStack matrixStack) {
        float padding = BACKGROUND_BORDERS * 1.25F;
        TardisConsoleRoomEntry selectedConsoleRoom = this.getSelectedConsoleRoom();

        Vec2f titlePos = this.getRenderPos(padding, padding);
        this.getTextRenderer().drawWithShadow(matrixStack, selectedConsoleRoom.getTitle(), titlePos.x, titlePos.y, 0xE0E0E0);

        Vec2f imagePos = this.getRenderPos(padding, padding + this.getTextRenderer().fontHeight * 1.5F);
        Identifier consoleRoomImage = DWM.getIdentifier("images/console_rooms/" + selectedConsoleRoom.name + ".png");

        if (!selectedConsoleRoom.imageUrl.isEmpty()) {
            if (!LOADED_CONSOLE_IMAGES.containsKey(selectedConsoleRoom.name)) {
                LOADED_CONSOLE_IMAGES.put(selectedConsoleRoom.name, null);

                CommonHelper.runInThread("loadRemoteImage", () -> {
                    try {
                        LOADED_CONSOLE_IMAGES.put(selectedConsoleRoom.name, CommonHelper.loadRemoteImage(selectedConsoleRoom.name, new URL(selectedConsoleRoom.imageUrl)));
                    } catch (Exception ignored) {
                    }
                });

                return;
            }

            Identifier consoleImage = LOADED_CONSOLE_IMAGES.get(selectedConsoleRoom.name);
            if (consoleImage != null) this.drawImage(matrixStack, imagePos, new Vec2f(this.getBackgroundSize().x - padding * 2, 120), consoleImage);
            return;
        }

        this.drawImage(matrixStack, imagePos, new Vec2f(this.getBackgroundSize().x - padding * 2, 120), consoleRoomImage);
    }
}
