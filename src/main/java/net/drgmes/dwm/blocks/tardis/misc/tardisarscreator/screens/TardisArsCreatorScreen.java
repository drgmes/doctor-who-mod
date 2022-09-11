package net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.network.ArsRemoteCallablePackets;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TardisArsCreatorScreen extends BaseScreen {
    private static final int LINE_PADDING = 3;

    private final BlockPos blockPos;
    private ArsCategory selectedArsCategory = null;

    private List<ArsCategory> arsCategories = new ArrayList<>();
    private List<ArsStructure> arsStructures = new ArrayList<>();

    private ListWidget listWidget;
    private ListWidget.ListEntry selectedArsStructureEntry = null;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    private TextFieldWidget search;
    private String lastSearch;

    public TardisArsCreatorScreen(BlockPos blockPos) {
        super(DWM.TEXTS.ARS_INTERFACE_NAME);

        this.blockPos = blockPos;
        this.reloadCategoriesList();
        this.reloadArsStructuresList();
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.ARS_INTERFACE_SIZE.multiply(0.795F);
    }

    @Override
    public Vec2f getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    public void tick() {
        this.search.tick();
        this.listWidget.setSelected(this.selectedArsStructureEntry);

        if (!search.getText().equals(lastSearch)) {
            this.selectedArsStructureEntry = null;
            this.reloadArsStructuresList();
            this.listWidget.refreshList();
            this.update();
        }
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2 - 1;
        int buttonOffset = (int) (this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT - 1);

        Vec2f cancelButtonPos = this.getRenderPos(BACKGROUND_BORDERS + 1, buttonOffset);
        this.cancelButton = new ButtonWidget((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, (b) -> this.close());

        Vec2f acceptButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 2, buttonOffset);
        this.acceptButton = new ButtonWidget((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_GENERATE, (b) -> this.apply());

        int listGhostSpace = 10;
        int listWidth = (int) this.getBackgroundSize().x - BACKGROUND_BORDERS * 2;
        int listHeight = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS * 2 - 20 - BUTTON_HEIGHT - listGhostSpace;
        int listOffset = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT - listHeight - listGhostSpace - 1;

        Vec2f categoriesListPos = this.getRenderPos(BACKGROUND_BORDERS, listOffset);
        this.listWidget = new ListWidget(this, listWidth, listHeight, categoriesListPos);

        Vec2f searchPos = this.getRenderPos(BACKGROUND_BORDERS + 1, BACKGROUND_BORDERS + 1);
        this.search = new TextFieldWidget(this.textRenderer, (int) searchPos.x, (int) searchPos.y, listWidth - 2, 18, DWM.TEXTS.ARS_INTERFACE_FLD_SEARCH);

        this.addDrawableChild(this.listWidget);
        this.addDrawableChild(this.search);
        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);

        this.update();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        String search = this.search.getText();
        ListWidget.ListEntry selectedArsStructureEntry = this.selectedArsStructureEntry;

        this.init(mc, width, height);
        this.search.setText(search);
        this.selectedArsStructureEntry = selectedArsStructureEntry;

        if (!this.search.getText().isEmpty()) {
            this.reloadArsStructuresList();
        }
    }

    protected void apply() {
        if (this.selectedArsStructureEntry != null) {
            PacketHelper.sendToServer(
                ArsRemoteCallablePackets.class,
                "applyArsCreator",
                this.blockPos, this.selectedArsStructureEntry.arsStructure.getCategory().getPath(), this.selectedArsStructureEntry.arsStructure.getName()
            );
        }

        this.close();
    }

    protected void update() {
        this.lastSearch = this.search.getText();
        this.search.active = this.arsStructures.size() > 0;
        this.acceptButton.active = this.selectedArsStructureEntry != null;
    }

    protected void setSelectedArsCategory(ListWidget.ListEntry entry) {
        if (entry.arsCategory != null) this.selectedArsCategory = entry.arsCategory;
        else if (this.selectedArsCategory != null && this.selectedArsCategory.getParent() != null) this.selectedArsCategory = this.selectedArsCategory.getParent();
        else this.selectedArsCategory = null;

        this.selectedArsStructureEntry = null;

        this.reloadCategoriesList();
        this.reloadArsStructuresList();
        this.update();

        this.listWidget.refreshList();
    }

    protected void setSelectedArsStructure(ListWidget.ListEntry entry) {
        this.selectedArsStructureEntry = entry == this.selectedArsStructureEntry ? null : entry;
        this.update();
    }

    protected void reloadCategoriesList() {
        List<ArsCategory> list = ArsCategories.CATEGORIES.values().stream().filter((arsCategory) -> arsCategory.getParent() == this.selectedArsCategory).toList();
        if (list.size() > 0) {
            list = new ArrayList<>(list);
            list.sort(Comparator.comparing(ArsCategory::getPath));
        }

        this.arsCategories = list;
    }

    protected void reloadArsStructuresList() {
        if (this.selectedArsCategory == null || !ArsStructures.STRUCTURES.containsKey(this.selectedArsCategory)) {
            this.arsStructures = new ArrayList<>();
            return;
        }

        List<ArsStructure> list = ArsStructures.STRUCTURES.get(this.selectedArsCategory).values().stream().toList();
        boolean hasSearch = this.search != null && !Objects.equals(this.search.getText(), "");
        if (hasSearch) {
            String search = this.search.getText().toLowerCase();
            this.lastSearch = this.search.getText();
            list = list.stream().filter((arsStructure) -> arsStructure.getName().toLowerCase().contains(search)).toList();
        }

        if (list.size() > 0) {
            list = new ArrayList<>(list);
            list.sort(Comparator.comparing(ArsStructure::getName));
        }

        this.arsStructures = list;
    }

    private static class ListWidget extends BaseListWidget {
        public final TardisArsCreatorScreen parent;

        public ListWidget(TardisArsCreatorScreen parent, int width, int height, Vec2f pos) {
            super(parent.client, parent.textRenderer, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();
            if (this.parent.selectedArsCategory != null) this.addEntry(new ListEntry());
            this.parent.arsCategories.forEach((arsCategory) -> this.addEntry(new ListEntry(arsCategory)));
            this.parent.arsStructures.forEach((arsStructure) -> this.addEntry(new ListEntry(arsStructure)));
        }

        public class ListEntry extends BaseListEntry {
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

                    MutableText text = Text.translatable("title." + DWM.MODID + ".ars.categories.back").formatted(Formatting.YELLOW);
                    ArsCategory arsCategory = ListWidget.this.parent.selectedArsCategory;

                    if (arsCategory != null) {
                        do {
                            text = arsCategory.getTag().copy().append(" / ").append(text);
                            arsCategory = arsCategory.getParent();
                        }
                        while (arsCategory != null);
                    }

                    return text;
                }

                return this.arsStructure == null
                    ? Text.empty()
                    : this.arsStructure.getTitle().copy().formatted(Formatting.AQUA);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                if (this.isCategory) {
                    ListWidget.this.parent.setSelectedArsCategory(this);
                    return false;
                }

                ListWidget.this.parent.setSelectedArsStructure(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}
