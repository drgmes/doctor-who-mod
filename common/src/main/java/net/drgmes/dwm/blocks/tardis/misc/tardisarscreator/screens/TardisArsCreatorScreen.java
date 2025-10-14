package net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.server.ArsCreatorApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TardisArsCreatorScreen extends BaseScreen {
    private final BlockPos blockPos;
    private final Map<String, ArsCategory> arsCategories;
    private final Map<String, ArsStructure> arsStructures;

    private boolean isInited;

    private ArsCategory selectedArsCategory = null;
    private List<ArsCategory> filteredArsCategories = new ArrayList<>();
    private List<ArsStructure> filteredArsStructures = new ArrayList<>();

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    private TextFieldWidget searchField;
    private String lastSearch;

    private ListWidget listWidget;
    private ListWidget.ListEntry selectedArsStructureEntry = null;

    public TardisArsCreatorScreen(BlockPos blockPos, Map<String, ArsCategory> arsCategories, Map<String, ArsStructure> arsStructures) {
        super(DWM.TEXTS.ARS_INTERFACE_TITLE);

        this.blockPos = blockPos;
        this.arsCategories = arsCategories;
        this.arsStructures = arsStructures;

        this.reloadArsCategoriesList();
        this.reloadArsStructuresList();
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ARS.CREATOR_INTERFACE;
    }

    @Override
    public Vector2i getBackgroundOriginSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS.CREATOR_INTERFACE_SIZE;
    }

    @Override
    public Vector2i getBackgroundBorderOriginSize() {
        return new Vector2i(14, 27);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.searchField.isFocused();
    }

    @Override
    public void init() {
        super.init();

        this.listWidget = new ListWidget(this, this.getListPos(), this.getListSize());

        Vector2i searchFieldPos = this.getLeftTopRenderPos(1, 1);
        this.searchField = new TextFieldWidget(this.textRenderer, searchFieldPos.x, searchFieldPos.y, this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - 2, INPUT_HEIGHT, DWM.TEXTS.ARS_INTERFACE_SEARCH);

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.ARS_INTERFACE_BTN_GENERATE, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = this.getLeftBottomRenderPos(SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.listWidget);
        this.addDrawableChild(this.searchField);
        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.cancelButton);

        this.isInited = true;
        this.update();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        String search = this.searchField.getText();
        ListWidget.ListEntry selectedArsStructureEntry = this.selectedArsStructureEntry;

        super.resize(mc, width, height);
        this.searchField.setText(search);
        this.selectedArsStructureEntry = selectedArsStructureEntry;

        if (!this.searchField.getText().isEmpty()) {
            this.reloadArsCategoriesList();
            this.reloadArsStructuresList();
        }
    }

    @Override
    public void tick() {
        this.listWidget.setSelected(this.selectedArsStructureEntry);

        if (!this.searchField.getText().equals(lastSearch)) {
            this.selectedArsStructureEntry = null;
            this.reloadArsCategoriesList();
            this.reloadArsStructuresList();
            this.listWidget.refreshList();
            this.update();
        }
    }

    protected void apply() {
        if (this.selectedArsStructureEntry != null) {
            new ArsCreatorApplyPacket(this.blockPos, this.selectedArsStructureEntry.arsStructure.name).sendToServer();
        }

        this.close();
    }

    private Vector2i getListPos() {
        return this.getLeftTopRenderPos(0, INPUT_HEIGHT + 4);
    }

    private Vector2i getListSize() {
        return new Vector2i(this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - SCREEN_MARGIN * 2 - INPUT_HEIGHT - BUTTON_SIZE - 6);
    }

    private boolean hasSearch() {
        return this.searchField != null && !Objects.equals(this.searchField.getText(), "");
    }

    private void update() {
        if (!this.isInited) return;

        this.lastSearch = this.searchField.getText();
        this.acceptButton.active = this.selectedArsStructureEntry != null;
    }

    private void reloadArsCategoriesList() {
        List<ArsCategory> list;

        if (this.hasSearch()) {
            String search = this.searchField.getText().toLowerCase();
            this.lastSearch = this.searchField.getText();
            list = this.arsCategories.values().stream().filter((arsCategory) -> arsCategory.getTitle().getString().toLowerCase().contains(search)).toList();
        }
        else {
            list = this.arsCategories.values().stream().filter((arsCategory) -> arsCategory.parent.equals(this.selectedArsCategory != null ? this.selectedArsCategory.name : "")).toList();
        }

        if (!list.isEmpty()) {
            list = new ArrayList<>(list);
            list.sort(Comparator.comparing((arsCategory) -> arsCategory.order));
        }

        this.filteredArsCategories = list;
    }

    private void reloadArsStructuresList() {
        List<ArsStructure> list;

        if (this.hasSearch()) {
            String search = this.searchField.getText().toLowerCase();
            this.lastSearch = this.searchField.getText();
            list = this.arsStructures.values().stream().filter((arsStructure) -> arsStructure.getTitle().getString().toLowerCase().contains(search)).toList();
        }
        else {
            list = this.arsStructures.values().stream().filter((arsStructure) -> arsStructure.category.equals(this.selectedArsCategory != null ? this.selectedArsCategory.name : "")).toList();
        }

        if (!list.isEmpty()) {
            list = new ArrayList<>(list);
            list.sort(Comparator.comparing((arsStructure) -> arsStructure.order));
        }

        this.filteredArsStructures = list;
    }

    private void setSelectedArsCategory(ListWidget.ListEntry entry) {
        this.selectedArsCategory = entry.arsCategory != null ? entry.arsCategory : this.arsCategories.getOrDefault(this.selectedArsCategory.parent, null);
        this.selectedArsStructureEntry = null;
        this.searchField.setText("");

        this.reloadArsCategoriesList();
        this.reloadArsStructuresList();
        this.update();

        this.listWidget.refreshList();
    }

    private void setSelectedArsStructure(ListWidget.ListEntry entry) {
        this.selectedArsStructureEntry = entry == this.selectedArsStructureEntry ? null : entry;
        this.update();
    }

    private static class ListWidget extends BaseListWidget {
        public final TardisArsCreatorScreen parent;

        public ListWidget(TardisArsCreatorScreen parent, Vector2i pos, Vector2i size) {
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

            if (this.parent.selectedArsCategory != null && !ListWidget.this.parent.hasSearch()) this.addEntry(new ListEntry());
            this.parent.filteredArsCategories.forEach((arsCategory) -> this.addEntry(new ListEntry(arsCategory)));
            this.parent.filteredArsStructures.forEach((arsStructure) -> this.addEntry(new ListEntry(arsStructure)));
        }

        private class ListEntry extends BaseListEntry {
            private final boolean isCategory;
            private final ArsCategory arsCategory;
            private final ArsStructure arsStructure;

            public ListEntry() {
                this.arsCategory = null;
                this.arsStructure = null;
                this.isCategory = true;
            }

            public ListEntry(ArsCategory arsCategory) {
                this.arsCategory = arsCategory;
                this.arsStructure = null;
                this.isCategory = true;
            }

            public ListEntry(ArsStructure arsStructure) {
                this.arsCategory = null;
                this.arsStructure = arsStructure;
                this.isCategory = false;
            }

            @Override
            public Text getText() {
                if (this.isCategory) {
                    if (this.arsCategory != null) {
                        return this.arsCategory.getTitle().copy().formatted(Formatting.GOLD);
                    }

                    MutableText text = DWM.TEXTS.ARS_CATEGORIES_BACK.copy();
                    ArsCategory arsCategory = ListWidget.this.parent.selectedArsCategory;

                    if (arsCategory != null) {
                        do {
                            text = arsCategory.getTag().copy().append(" / ").append(text);
                            arsCategory = ListWidget.this.parent.arsCategories.getOrDefault(arsCategory.parent, null);
                        }
                        while (arsCategory != null);
                    }

                    return text;
                }

                if (this.arsStructure != null) {
                    return this.arsStructure.getTitle().copy().formatted(Formatting.AQUA);
                }

                return Text.empty();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (!super.mouseClicked(mouseX, mouseY, button)) return false;

                if (this.isCategory) ListWidget.this.parent.setSelectedArsCategory(this);
                else ListWidget.this.parent.setSelectedArsStructure(this);
                return true;
            }
        }
    }
}
