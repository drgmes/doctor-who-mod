package net.drgmes.dwm.items.tardis.tardissystem;

import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class TardisSystemItemBuilder extends ItemBuilder {
    public TardisSystemItemBuilder(String name, Class<? extends ITardisSystem> systemType) {
        super(name, () -> new TardisSystemItem(getItemProperties(), systemType));
    }

    public static Item.Properties getItemProperties() {
        return ItemBuilder.getItemProperties().stacksTo(1);
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        try {
            ModelFile itemGenerated = provider.getExistingFile(provider.modLoc("item/tardis_systems/" + this.name));
            provider.getBuilder(this.name).parent(itemGenerated);
        } catch (IllegalStateException e) {
            ItemModelBuilder builder = provider.getBuilder(this.name);
            ModelHelper.applyExternalOBJModel(builder, "item/tardis_systems/" + this.name, true);
            ModelHelper.rotateToBlockStyle(builder, 0.3F);

            builder.transforms().transform(ItemTransforms.TransformType.GUI).translation(-1.75F, 0.5F, 0);
        }
    }
}
