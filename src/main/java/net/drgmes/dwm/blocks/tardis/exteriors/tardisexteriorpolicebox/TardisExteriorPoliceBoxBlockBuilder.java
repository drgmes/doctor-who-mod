package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockBuilder;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisExteriorPoliceBoxBlockBuilder extends BaseTardisExteriorBlockBuilder {
    public TardisExteriorPoliceBoxBlockBuilder(String name) {
        super(name, new TardisExteriorPoliceBoxBlock(getBlockSettings()));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisExteriorPoliceBoxModel.LAYER_LOCATION, TardisExteriorPoliceBoxModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX, TardisExteriorPoliceBoxBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisExteriorPoliceBoxItemRenderer());
    }
}
