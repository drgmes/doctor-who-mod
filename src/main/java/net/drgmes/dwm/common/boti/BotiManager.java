package net.drgmes.dwm.common.boti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import org.lwjgl.opengl.GL11;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class BotiManager {
    private static final List<BotiEntraceData> entracesData = new ArrayList<>();
    private static final Minecraft mc = Minecraft.getInstance();
    private static final BotiVBO vbo = new BotiVBO();

    private static TextureTarget fbo;
    private static boolean inRenderProcess;

    @SubscribeEvent
    public static void removeHighlight(DrawSelectionEvent event) {
        if (event.getTarget() instanceof BlockHitResult blockHitResult) {
            Block hitBlock = mc.level.getBlockState(blockHitResult.getBlockPos()).getBlock();

            for (BotiEntraceData entraceData : entracesData) {
                Block block = mc.level.getBlockState(new BlockPos(entraceData.getPosition())).getBlock();
                if (block == hitBlock) event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelLastEvent event) {
        PoseStack poseStack = event.getPoseStack();
        Vec3 proj = mc.gameRenderer.getMainCamera().getPosition();

        inRenderProcess = true;
        poseStack.pushPose();

        for (BotiEntraceData entraceData : entracesData) {
            poseStack.pushPose();
            poseStack.translate(-proj.x, -proj.y, -proj.z);
            poseStack.translate(entraceData.getPosition().x(), entraceData.getPosition().y(), entraceData.getPosition().z());
            start(entraceData, poseStack, event.getProjectionMatrix(), event.getPartialTick());
            poseStack.popPose();
        }

        entracesData.clear();
        poseStack.popPose();
        inRenderProcess = false;
    }

    public static void addEntraceData(BotiEntraceData entraceData) {
        if (!inRenderProcess) {
            entracesData.add(entraceData);
        }
    }

    public static void start(BotiEntraceData entraceData, PoseStack poseStack, Matrix4f matrix4f, float partialTicks) {
        setupFBO();
        setFBOColor();

        if (ModConfig.CLIENT.enableBoti.get()) {
            poseStack.pushPose();
            draw(entraceData, poseStack, matrix4f, partialTicks);
            poseStack.popPose();
        }

        BufferBuilder underlyingBuffer = Tesselator.getInstance().getBuilder();
        BufferSource imBuffer = MultiBufferSource.immediate(underlyingBuffer);

        entraceData.renderDoors(poseStack, imBuffer);
        imBuffer.endBatch();

        mc.getMainRenderTarget().bindWrite(false);

        setupStencil(poseStack, imBuffer, entraceData::renderBoti);
        fbo.blitToScreen(fbo.viewWidth, fbo.viewHeight, true);
        endStencil(poseStack, imBuffer, entraceData::renderBoti);

        RenderSystem.resetTextureMatrix();
        endFBO();
    }

    @SuppressWarnings("deprecation")
    private static void draw(BotiEntraceData entraceData, PoseStack poseStack, Matrix4f matrix4f, float partialTicks) {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        mc.textureManager.bindForSetup(TextureAtlas.LOCATION_BLOCKS);
        entraceData.transformBoti(poseStack);
        poseStack.translate(0, -2, 0);

        BotiBlocksStorage storage = BotiBlocksStorage.getStorage(entraceData.getTardisLevelUUID());

        if (storage.isUpdated || true) {
            storage.isUpdated = false;

            for (RenderType type : RenderType.chunkBufferLayers()) {
                vbo.begin(type);
                type.setupRenderState();
                vbo.resetData(type);

                boolean hasRenderedInLayer = false;

                for (Entry<BlockPos, BlockState> entry : storage.blockEntries.entrySet()) {
                    Vec3 pos = Vec3.atLowerCornerOf(entry.getKey());
                    BlockState blockState = entry.getValue();

                    poseStack.pushPose();
                    poseStack.translate(pos.x(), pos.y(), pos.z());

                    if (blockState.getFluidState().isEmpty()) {
                        if (ItemBlockRenderTypes.canRenderInLayer(blockState, type)) {
                            mc.getBlockRenderer().renderSingleBlock(blockState, poseStack, vbo.getBufferSource(type), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
                            hasRenderedInLayer = true;
                        }
                    }
                    else if (ItemBlockRenderTypes.canRenderInLayer(blockState.getFluidState(), type)) {
                        if (!vbo.getBufferBuilder(type).building()) {
                            vbo.getBufferBuilder(type).begin(VertexFormat.Mode.QUADS, vbo.format);
                        }

                        mc.getBlockRenderer().renderLiquid(entry.getKey(), mc.level, vbo.getBufferBuilder(type), blockState, blockState.getFluidState());
                        hasRenderedInLayer = true;
                    }

                    poseStack.popPose();
                }

                if (hasRenderedInLayer) {
                    vbo.upload(type);
                }

                vbo.unbind(type);
                type.clearRenderState();
            }
        }
        else {
            poseStack.pushPose();
            vbo.draw(poseStack.last().pose());
            poseStack.popPose();
        }
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

    private static void endStencil(PoseStack poseStack, BufferSource buffer, BiConsumer<PoseStack, BufferSource> consumer) {
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

        GL11.glColorMask(false, false, false, false);
        RenderSystem.depthMask(false);
        // consumer.accept(poseStack, buffer);
        buffer.endBatch();

        RenderSystem.depthMask(true);
        GL11.glColorMask(true, true, true, true);
    }
}
