package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockBuilder;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisDoorsPoliceBoxBlockBuilder extends BaseTardisDoorsBlockBuilder {
    public TardisDoorsPoliceBoxBlockBuilder(String name) {
        super(name, new TardisDoorsPoliceBoxBlock(getBlockSettings()));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisDoorsPoliceBoxModel.LAYER_LOCATION, TardisDoorsPoliceBoxModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_DOORS_POLICE_BOX, TardisDoorsPoliceBoxBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisDoorsPoliceBoxItemRenderer());
    }
}
