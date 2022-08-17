package net.drgmes.dwm.entities.tardis.consoles.controls;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TardisConsoleControlEntityRenderer extends EntityRenderer<Entity> {
    public TardisConsoleControlEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected boolean hasLabel(Entity entity) {
        return true;
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return null;
    }

    @Override
    protected void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
    }
}
