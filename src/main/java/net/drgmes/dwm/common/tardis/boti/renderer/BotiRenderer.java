package net.drgmes.dwm.common.tardis.boti.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.boti.storage.BotiStorage;
import net.drgmes.dwm.common.tardis.boti.storage.wrappers.BotiBlockEntityWrapper;
import net.drgmes.dwm.common.tardis.boti.storage.wrappers.BotiBlockWrapper;
import net.drgmes.dwm.setup.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class BotiRenderer {
    private static final List<BotiEntraceData> entracesData = new ArrayList<>();
    private static final Minecraft mc = Minecraft.getInstance();
    private static final BotiVBO vbo = new BotiVBO();

    private static TextureTarget fbo;
    private static boolean inRenderProcess;

    @SubscribeEvent
    public static void onDrawSelection(DrawSelectionEvent event) {
        if (event.getTarget() instanceof BlockHitResult blockHitResult) {
            Block hitBlock = mc.level.getBlockState(blockHitResult.getBlockPos()).getBlock();

            for (BotiEntraceData entraceData : entracesData) {
                Block block = mc.level.getBlockState(new BlockPos(entraceData.getPosition())).getBlock();
                if (block == hitBlock) event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelLast(RenderLevelLastEvent event) {
        if (ModConfig.CLIENT.botiEnabled.get()) {
            PoseStack poseStack = event.getPoseStack();
            Vec3 proj = mc.gameRenderer.getMainCamera().getPosition();

            inRenderProcess = true;
            poseStack.pushPose();

            for (BotiEntraceData entraceData : entracesData) {
                if (entraceData.getBotiStorage() == null) continue;

                poseStack.pushPose();
                poseStack.translate(-proj.x, -proj.y, -proj.z);
                poseStack.translate(entraceData.getPosition().x(), entraceData.getPosition().y(), entraceData.getPosition().z());
                start(entraceData, poseStack, event.getProjectionMatrix(), event.getPartialTick());
                poseStack.popPose();
            }

            poseStack.popPose();
            inRenderProcess = false;
        }

        entracesData.clear();
    }

    public static void addEntraceData(BotiEntraceData entraceData) {
        if (!inRenderProcess) {
            entracesData.add(entraceData);
        }
    }

    public static void start(BotiEntraceData entraceData, PoseStack poseStack, Matrix4f matrix4f, float partialTicks) {
        vbo.init();
        setupFBO();
        setFBOColor();

        poseStack.pushPose();
        draw(entraceData, poseStack, matrix4f, partialTicks);
        poseStack.popPose();

        BufferBuilder underlyingBuffer = Tesselator.getInstance().getBuilder();
        BufferSource imBuffer = MultiBufferSource.immediate(underlyingBuffer);

        entraceData.renderDoors(poseStack, imBuffer);
        imBuffer.endBatch();

        mc.getMainRenderTarget().bindWrite(false);

        setupStencil(poseStack, imBuffer, entraceData::renderBoti);
        fbo.blitToScreen(fbo.viewWidth, fbo.viewHeight, true);
        endStencil();

        RenderSystem.resetTextureMatrix();
        endFBO();
    }

    @SuppressWarnings("deprecation")
    private static void draw(BotiEntraceData entraceData, PoseStack poseStack, Matrix4f matrix4f, float partialTicks) {
        entraceData.transformBoti(poseStack);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        poseStack.pushPose();
        mc.textureManager.bindForSetup(TextureAtlas.LOCATION_BLOCKS);

        BotiStorage botiStorage = entraceData.getBotiStorage();
        if (botiStorage.isUpdated || true) {
            botiStorage.isUpdated = false;

            for (RenderType type : RenderType.chunkBufferLayers()) {
                vbo.begin(type);
                type.setupRenderState();
                vbo.resetData(type);

                // boolean isRendered = false;
                PoseStack innerPoseStack = poseStack;
                // PoseStack innerPoseStack = new PoseStack();

                for (Entry<BlockPos, BotiBlockEntityWrapper> entry : botiStorage.blockEntities.entrySet()) {
                    Vec3 pos = Vec3.atLowerCornerOf(entry.getKey());

                    BotiBlockEntityWrapper botiBlockEntityWrapper = entry.getValue();
                    BlockState blockState = botiBlockEntityWrapper.getBlockState();
                    BlockEntity blockEntity = botiBlockEntityWrapper.createBlockEntity(mc.level);

                    poseStack.pushPose();
                    poseStack.translate(pos.x(), pos.y(), pos.z());

                    if (ItemBlockRenderTypes.canRenderInLayer(blockState, type)) {
                        BlockEntityRenderer<BlockEntity> renderer = mc.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
                        if (renderer != null) {
                            renderer.render(blockEntity, partialTicks, poseStack, vbo.getBufferSource(type), LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
                            botiStorage.blocks.remove(entry.getKey());
                            // isRendered = true;
                        }
                    }

                    poseStack.popPose();
                }

                for (Entry<BlockPos, BotiBlockWrapper> entry : botiStorage.blocks.entrySet()) {
                    Vec3 pos = Vec3.atLowerCornerOf(entry.getKey());

                    BotiBlockWrapper botiBlockWrapper = entry.getValue();
                    BlockPos blockPos = botiBlockWrapper.getBlockPos();
                    BlockState blockState = botiBlockWrapper.getBlockState();
                    FluidState fluidState = botiBlockWrapper.getFluidState();

                    innerPoseStack.pushPose();
                    innerPoseStack.translate(pos.x(), pos.y(), pos.z());

                    if (fluidState.isEmpty()) {
                        if (ItemBlockRenderTypes.canRenderInLayer(blockState, type)) {
                            IModelData modelData = EmptyModelData.INSTANCE;

                            if (blockState.getBlock() instanceof EntityBlock entityBlock) {
                                BlockEntity blockEntity = entityBlock.newBlockEntity(blockPos, blockState);
                                if (blockEntity != null && blockEntity.getModelData() != null) {
                                    modelData = blockEntity.getModelData();
                                }
                            }

                            mc.getBlockRenderer().renderSingleBlock(blockState, innerPoseStack, vbo.getBufferSource(type), LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, modelData);
                            // isRendered = true;
                        }
                    }
                    else if (ItemBlockRenderTypes.canRenderInLayer(fluidState, type)) {
                        // TODO
                        // if (!vbo.getBufferBuilder(type).building()) vbo.getBufferBuilder(type).begin(VertexFormat.Mode.QUADS, vbo.format);
                        // mc.getBlockRenderer().renderLiquid(blockPos, mc.level, vbo.getBufferBuilder(type), blockState, fluidState);
                        // isRendered = true;
                    }

                    innerPoseStack.popPose();
                }

                // TODO
                // if (isRendered) {
                //     vbo.upload(type);
                // }

                vbo.unbind(type);
                type.clearRenderState();
            }
        }
        // TODO
        // else {
        //     poseStack.pushPose();
        //     vbo.draw(poseStack.last().pose());
        //     poseStack.popPose();
        // }

        poseStack.popPose();
    }

    private static void setupFBO() {
        RenderTarget mainRenderTarget = mc.getMainRenderTarget();
        mainRenderTarget.unbindWrite();

        if (fbo != null && (fbo.viewWidth != mainRenderTarget.viewWidth || fbo.viewHeight != mainRenderTarget.viewHeight)) {
            fbo = null;
        }

        if (fbo == null) {
            fbo = new TextureTarget(mainRenderTarget.viewWidth, mainRenderTarget.viewHeight, true, Minecraft.ON_OSX);
        }

        fbo.bindWrite(false);
        fbo.checkStatus();

        if (!fbo.isStencilEnabled()) {
            fbo.enableStencil();
        }
    }

    private static void setFBOColor() {
        if (fbo == null) return;
        Vec3 skyColor = mc.level.getSkyColor(mc.player.position(), mc.getFrameTime());
        fbo.setClearColor((float) skyColor.x, (float) skyColor.y, (float) skyColor.z, 0);
    }

    private static void endFBO() {
        fbo.unbindWrite();
        fbo.clear(Minecraft.ON_OSX);
        mc.getMainRenderTarget().bindWrite(false);
    }

    private static void setupStencil(PoseStack poseStack, BufferSource buffer, BiConsumer<PoseStack, BufferSource> consumer) {
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GL11.glStencilMask(0xFF);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

        RenderSystem.depthMask(false);
        consumer.accept(poseStack, buffer);
        buffer.endBatch();
        RenderSystem.depthMask(true);

        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);

    }

    private static void endStencil() {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
    }
}
