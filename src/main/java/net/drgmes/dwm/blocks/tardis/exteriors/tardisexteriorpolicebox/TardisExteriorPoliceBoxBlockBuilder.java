package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockBuilder;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class TardisExteriorPoliceBoxBlockBuilder extends BaseTardisExteriorBlockBuilder {
    public TardisExteriorPoliceBoxBlockBuilder(String name) {
        super(name, () -> new TardisExteriorPoliceBoxBlock(getBlockBehaviour()));
    }

    @Override
    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX.get(), TardisExteriorPoliceBoxBlockRenderer::new);
    }

    @Override
    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisExteriorPoliceBoxModel.LAYER_LOCATION, TardisExteriorPoliceBoxModel::createBodyLayer);
    }
}
