package net.drgmes.dwm.blocks.tardis.others.tardisroomcreator.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsRoom;
import net.drgmes.dwm.common.tardis.ars.ArsRooms;
import net.drgmes.dwm.network.ServerboundTardisRoomsCreatorApplyPacket;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.GuiUtils;

public class TardisRoomCreatorScreen extends Screen implements IBaseScreen {
    private static final int LINE_PADDING = 3;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BACKGROUND_BORDERS = 24;

    private final BlockPos blockPos;
    private ArsCategory selectedCategory = null;

    private List<ArsCategory> categories = new ArrayList<>();
    private List<ArsRoom> rooms = new ArrayList<>();

    private ListWidget listWidget;
    private ListWidget.ListEntry selectedRoomEntry = null;

    private Button cancelButton;
    private Button acceptButton;

    private EditBox search;
    private String lastSearch;

    public TardisRoomCreatorScreen(BlockPos blockPos) {
        super(DWM.TEXTS.ARS_INTERFACE_NAME);

        this.blockPos = blockPos;
        this.reloadCategoriesList();
        this.reloadRoomsList();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Font getFont() {
        return this.font;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public Component getTitleComponent() {
        return this.getTitle();
    }

    @Override
    public ResourceLocation getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE;
    }

    @Override
    public Vec2 getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE_SIZE.scale(0.795F);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void blit(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY) {
        super.blit(poseStack, x, y, textureX, textureY, textureWidth, textureHeight, textureClipX, textureClipY);
    }

    @Override
    public Vec2 getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2;
        int buttonOffset = (int) (this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT + 1);

        Vec2 cancelButtonPos = this.getRenderPos(BACKGROUND_BORDERS - 1, buttonOffset);
        this.cancelButton = new Button((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, (b) -> this.onClose());

        Vec2 acceptButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 1, buttonOffset);
        this.acceptButton = new Button((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_GENERATE, (b) -> this.apply());

        int listGhostSpace = 10;
        int listWidth = (int) this.getBackgroundSize().x - BACKGROUND_BORDERS * 2;
        int listHeight = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS * 2 - 20 - BUTTON_HEIGHT - listGhostSpace;
        int listOffset = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT - listHeight - listGhostSpace + 1;

        Vec2 categoriesListPos = this.getRenderPos(BACKGROUND_BORDERS, listOffset);
        this.listWidget = new ListWidget(this, listWidth, listHeight, categoriesListPos);

        Vec2 searchPos = this.getRenderPos(BACKGROUND_BORDERS + 1, BACKGROUND_BORDERS + 1);
        this.search = new EditBox(this.font, (int) searchPos.x, (int) searchPos.y, listWidth - 2, 18, DWM.TEXTS.TELEPATHIC_INTERFACE_FLD_SEARCH);
        this.search.setFocus(false);
        this.search.setCanLoseFocus(true);

        this.addRenderableWidget(this.search);
        this.addRenderableWidget(this.listWidget);
        this.addRenderableWidget(this.cancelButton);
        this.addRenderableWidget(this.acceptButton);

        this.update();
    }

    @Override
    public void tick() {
        this.search.tick();
        this.listWidget.setSelected(this.selectedRoomEntry);

        if (!search.getValue().equals(lastSearch)) {
            this.reloadRoomsList();
            this.listWidget.refreshList();
            this.update();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.onDone();
        return super.mouseClicked(mouseX, mouseY, frame);
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(poseStack);
        this.renderBackground(poseStack, mouseX, mouseY);

        Vec2 pos = new Vec2(19, 5);
        Vec2 pos1 = this.getRenderPos(pos.x, pos.y);
        Vec2 pos2 = this.getRenderPos(pos.x + this.font.width(this.getTitle().getString()) + 9, pos.y + this.font.lineHeight + 1);
        int color = 0xFF4F5664;

        GuiUtils.drawGradientRect(poseStack.last().pose(), 0, (int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y, color, color);
        this.renderTitle(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void resize(Minecraft mc, int width, int height) {
        String search = this.search.getValue();
        ListWidget.ListEntry selectedRoomEntry = this.selectedRoomEntry;

        this.init(mc, width, height);
        this.search.setValue(search);
        this.selectedRoomEntry = selectedRoomEntry;

        if (!this.search.getValue().isEmpty()) {
            this.reloadRoomsList();
        }
    }

    @Override
    public void onClose() {
        this.onDone();
    }

    protected void onDone() {
        this.minecraft.setScreen(null);
    }

    protected void apply() {
        if (this.selectedRoomEntry != null) {
            ModPackets.INSTANCE.sendToServer(new ServerboundTardisRoomsCreatorApplyPacket(this.selectedRoomEntry.room, this.blockPos));
        }

        this.onDone();
    }

    protected void update() {
        this.lastSearch = this.search.getValue();
        this.search.active = this.rooms.size() > 0;
        this.acceptButton.active = this.selectedRoomEntry != null;
    }

    protected void setSelectedCategory(ListWidget.ListEntry entry) {
        if (entry.category != null) {
            this.selectedCategory = entry.category;
        }
        else if (this.selectedCategory != null && this.selectedCategory.getParent() != null) {
            this.selectedCategory = this.selectedCategory.getParent();
        }
        else {
            this.selectedCategory = null;
        }

        this.selectedRoomEntry = null;

        this.reloadCategoriesList();
        this.reloadRoomsList();
        this.update();

        this.listWidget.refreshList();
    }

    protected void setSelectedRoom(ListWidget.ListEntry entry) {
        this.selectedRoomEntry = entry == this.selectedRoomEntry ? null : entry;
        this.update();
    }

    protected void reloadCategoriesList() {
        List<ArsCategory> list = ArsCategories.CATEGORIES.values().stream().filter((category) -> category.getParent() == this.selectedCategory).toList();
        if (list.size() > 0) {
            list = new ArrayList<>(list);
            Collections.sort(list, new Comparator<ArsCategory>() {
                @Override
                public int compare(ArsCategory a, ArsCategory b) {
                    return a.getPath().compareTo(b.getPath());
                }
            });
        }

        this.categories = list;
    }

    protected void reloadRoomsList() {
        if (this.selectedCategory == null || !ArsRooms.ROOMS.containsKey(this.selectedCategory)) {
            this.rooms = new ArrayList<>();
            return;
        }

        List<ArsRoom> list = ArsRooms.ROOMS.get(this.selectedCategory).values().stream().toList();
        boolean hasSearch = this.search != null && this.search.getValue() != "";
        if (hasSearch) {
            String search = this.search.getValue().toLowerCase();
            this.lastSearch = this.search.getValue();
            list = list.stream().filter((room) -> room.getName().toLowerCase().contains(search)).toList();
        }

        if (list.size() > 0) {
            list = new ArrayList<>(list);
            Collections.sort(list, new Comparator<ArsRoom>() {
                @Override
                public int compare(ArsRoom a, ArsRoom b) {
                    return a.getName().compareTo(b.getName());
                }
            });
        }

        this.rooms = list;
    }

    private class ListWidget extends BaseListWidget {
        public final TardisRoomCreatorScreen parent;

        public ListWidget(TardisRoomCreatorScreen parent, int width, int height, Vec2 pos) {
            super(parent.minecraft, parent.font, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();
            if (this.parent.selectedCategory != null) this.addEntry(new ListEntry());
            this.parent.categories.forEach((category) -> this.addEntry(new ListEntry(category)));
            this.parent.rooms.forEach((room) -> this.addEntry(new ListEntry(room)));
        }

        public class ListEntry extends BaseListWidget.BaseListEntry {
            private final boolean isCategory;
            private final ArsCategory category;
            private final ArsRoom room;

            public ListEntry() {
                this.category = null;
                this.room = null;
                this.isCategory = true;
            }

            public ListEntry(ArsCategory category) {
                this.category = category;
                this.room = null;
                this.isCategory = true;
            }

            public ListEntry(ArsRoom room) {
                this.category = null;
                this.room = room;
                this.isCategory = false;
            }

            @Override
            public Component getNarration() {
                MutableComponent narration;

                if (this.isCategory) {
                    if (this.category != null) {
                        narration = this.category.getTitle().copy();
                        narration.setStyle(narration.getStyle().withColor(ChatFormatting.GOLD));
                    }
                    else {
                        narration = new TranslatableComponent("title." + DWM.MODID + ".ars.categories.back");
                        narration.setStyle(narration.getStyle().withColor(ChatFormatting.YELLOW));

                        ArsCategory category = ListWidget.this.parent.selectedCategory;
                        if (category != null) {
                            do {
                                narration = category.getTag().copy().append(" / ").append(narration);
                                category = category.getParent();
                            }
                            while (category != null);
                        }
                    }

                    return narration;
                }

                narration = this.room.getTitle().copy();
                narration.setStyle(narration.getStyle().withColor(ChatFormatting.AQUA));
                return narration;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
                if (this.isCategory) {
                    ListWidget.this.parent.setSelectedCategory(this);
                    return false;
                }

                ListWidget.this.parent.setSelectedRoom(this);
                return super.mouseClicked(mouseX, mouseY, partialTicks);
            }
        }
    }
}
