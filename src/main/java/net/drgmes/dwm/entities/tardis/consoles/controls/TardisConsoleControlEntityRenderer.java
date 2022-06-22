package net.drgmes.dwm.entities.tardis.consoles.controls;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TardisConsoleControlEntityRenderer extends EntityRenderer<Entity> {
    public TardisConsoleControlEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected boolean shouldShowName(Entity entity) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return null;
    }

    @Override
    protected void renderNameTag(Entity entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    }
}
