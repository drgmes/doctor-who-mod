package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockBuilder;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class TardisDoorsPoliceBoxBlockBuilder extends BaseTardisDoorsBlockBuilder {
    public TardisDoorsPoliceBoxBlockBuilder(String name) {
        super(name, () -> new TardisDoorsPoliceBoxBlock(getBlockBehaviour()));
    }

    @Override
    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TARDIS_DOORS_POLICE_BOX.get(), TardisDoorsPoliceBoxBlockRenderer::new);
    }

    @Override
    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisDoorsPoliceBoxModel.LAYER_LOCATION, TardisDoorsPoliceBoxModel::createBodyLayer);
    }
}
