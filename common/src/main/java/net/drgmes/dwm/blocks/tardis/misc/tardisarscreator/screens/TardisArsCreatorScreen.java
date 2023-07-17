package net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.server.ArsCreatorApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector2i;

import java.util.*;

public class TardisArsCreatorScreen extends BaseScreen {
    private static final int LINE_PADDING = 3;

    private final BlockPos blockPos;
    private final Map<String, ArsCategory> arsCategories;
    private final Map<String, ArsStructure> arsStructures;

    private ArsCategory selectedArsCategory = null;
    private List<ArsCategory> filteredArsCategories = new ArrayList<>();
    private List<ArsStructure> filteredArsStructures = new ArrayList<>();

    private ListWidget listWidget;
    private ListWidget.ListEntry selectedArsStructureEntry = null;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    private TextFieldWidget search;
    private String lastSearch;

    public TardisArsCreatorScreen(BlockPos blockPos, Map<String, ArsCategory> arsCategories, Map<String, ArsStructure> arsStructures) {
        super(DWM.TEXTS.ARS_INTERFACE_NAME);

        this.blockPos = blockPos;
        this.arsCategories = arsCategories;
        this.arsStructures = arsStructures;

        this.reloadCategoriesList();
        this.reloadArsStructuresList();
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ARS.CREATOR_INTERFACE;
    }

    @Override
    public Vector2i getBackgroundSize() {
        int padding = 5;
        Window window = MinecraftClient.getInstance().getWindow();
        return new Vector2i(window.getScaledWidth() - padding, window.getScaledHeight() - padding);
    }

    @Override
    public Vector2i getBackgroundBorderSize() {
        Vector2i size = this.getBackgroundSize();
        Vector2i originSize = DWM.TEXTURES.GUI.TARDIS.ARS.CREATOR_INTERFACE_SIZE.div(1 / 0.795F, new Vector2i());
        return new Vector2i((int) Math.floor(12 * ((float) size.x / originSize.x)), (int) Math.floor(22 * ((float) size.y / originSize.y)));
    }

    @Override
    public Vector2i getTitleRenderPos() {
        return this.getRenderPos(20, 7);
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = this.getBackgroundSize().x / 2 - this.getBackgroundBorderSize().x - 1;
        int buttonOffset = this.getBackgroundSize().y - this.getBackgroundBorderSize().y - BUTTON_HEIGHT - 1;

        Vector2i cancelButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x, buttonOffset);
        this.cancelButton = RenderHelper.getButtonWidget(cancelButtonPos.x, cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_CANCEL, (b) -> this.close());

        Vector2i acceptButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + buttonWidth + 1, buttonOffset);
        this.acceptButton = RenderHelper.getButtonWidget(acceptButtonPos.x, acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.ARS_INTERFACE_BTN_GENERATE, (b) -> this.apply());

        int listWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2;
        int listHeight = this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - 20 - BUTTON_HEIGHT - 3;
        int listOffset = this.getBackgroundSize().y - this.getBackgroundBorderSize().y - BUTTON_HEIGHT - listHeight - 2;

        Vector2i categoriesListPos = this.getRenderPos(this.getBackgroundBorderSize().x, listOffset);
        this.listWidget = new ListWidget(this, listWidth, listHeight, categoriesListPos);

        Vector2i searchPos = this.getRenderPos(this.getBackgroundBorderSize().x + 1, this.getBackgroundBorderSize().y + 2);
        this.search = new TextFieldWidget(this.textRenderer, searchPos.x, searchPos.y, listWidth - 2, 18, DWM.TEXTS.ARS_INTERFACE_FLD_SEARCH);

        this.addDrawableChild(this.listWidget);
        this.addDrawableChild(this.search);
        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);

        this.update();
    }

    @Override
    public void tick() {
        this.search.tick();
        this.listWidget.setSelected(this.selectedArsStructureEntry);

        if (!this.search.getText().equals(lastSearch)) {
            this.selectedArsStructureEntry = null;
            this.reloadCategoriesList();
            this.reloadArsStructuresList();
            this.listWidget.refreshList();
            this.update();
        }
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        String search = this.search.getText();
        ListWidget.ListEntry selectedArsStructureEntry = this.selectedArsStructureEntry;

        this.init(mc, width, height);
        this.search.setText(search);
        this.selectedArsStructureEntry = selectedArsStructureEntry;

        if (!this.search.getText().isEmpty()) {
            this.reloadCategoriesList();
            this.reloadArsStructuresList();
        }
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.search.isFocused();
    }

    protected void apply() {
        if (this.selectedArsStructureEntry != null) {
            new ArsCreatorApplyPacket(this.blockPos, this.selectedArsStructureEntry.arsStructure.name).sendToServer();
        }

        this.close();
    }

    protected void update() {
        this.lastSearch = this.search.getText();
        this.acceptButton.active = this.selectedArsStructureEntry != null;
    }

    protected void setSelectedArsCategory(ListWidget.ListEntry entry) {
        this.selectedArsCategory = entry.arsCategory != null ? entry.arsCategory : this.arsCategories.getOrDefault(this.selectedArsCategory.parent, null);
        this.selectedArsStructureEntry = null;
        this.search.setText("");

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
        List<ArsCategory> list;

        if (this.hasSearch()) {
            String search = this.search.getText().toLowerCase();
            this.lastSearch = this.search.getText();
            list = this.arsCategories.values().stream().filter((arsCategory) -> arsCategory.getTitle().getString().toLowerCase().contains(search)).toList();
        }
        else {
            list = this.arsCategories.values().stream().filter((arsCategory) -> arsCategory.parent.equals(this.selectedArsCategory != null ? this.selectedArsCategory.name : "")).toList();
        }

        if (list.size() > 0) {
            list = new ArrayList<>(list);
            list.sort(Comparator.comparing((arsCategory) -> arsCategory.name));
        }

        this.filteredArsCategories = list;
    }

    protected void reloadArsStructuresList() {
        List<ArsStructure> list;

        if (this.hasSearch()) {
            String search = this.search.getText().toLowerCase();
            this.lastSearch = this.search.getText();
            list = this.arsStructures.values().stream().filter((arsStructure) -> arsStructure.getTitle().getString().toLowerCase().contains(search)).toList();
        }
        else {
            list = this.arsStructures.values().stream().filter((arsStructure) -> arsStructure.category.equals(this.selectedArsCategory != null ? this.selectedArsCategory.name : "")).toList();
        }

        if (list.size() > 0) {
            list = new ArrayList<>(list);
            list.sort(Comparator.comparing((arsStructure) -> arsStructure.name));
        }

        this.filteredArsStructures = list;
    }

    protected boolean hasSearch() {
        return this.search != null && !Objects.equals(this.search.getText(), "");
    }

    private static class ListWidget extends BaseListWidget {
        public final TardisArsCreatorScreen parent;

        public ListWidget(TardisArsCreatorScreen parent, int width, int height, Vector2i pos) {
            super(parent.client, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();
            if (this.parent.selectedArsCategory != null && !ListWidget.this.parent.hasSearch()) this.addEntry(new ListEntry());
            this.parent.filteredArsCategories.forEach((arsCategory) -> this.addEntry(new ListEntry(arsCategory)));
            this.parent.filteredArsStructures.forEach((arsStructure) -> this.addEntry(new ListEntry(arsStructure)));
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
