package net.drgmes.dwm.common.tardis.boti.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.drgmes.dwm.common.tardis.boti.storage.BotiStorage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BotiEntraceData {
    private final Vec3 position;
    private final String tardisLevelUUID;

    private BotiStorage botiStorage;
    private Consumer<PoseStack> botiTransformer;
    private BiConsumer<PoseStack, MultiBufferSource> doorsRenderer;
    private BiConsumer<PoseStack, MultiBufferSource> botiRenderer;

    public BotiEntraceData(Vec3 position, String tardisLevelUUID) {
        this.position = position;
        this.tardisLevelUUID = tardisLevelUUID;
    }

    public BotiEntraceData(BlockPos blockPos, String tardisLevelUUID) {
        this(Vec3.atLowerCornerOf(blockPos), tardisLevelUUID);
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public String getTardisLevelUUID() {
        return this.tardisLevelUUID;
    }

    public BotiStorage getBotiStorage() {
        return this.botiStorage;
    }

    public void setBotiStorage(BotiStorage botiStorage) {
        this.botiStorage = botiStorage;
    }

    public void setDoorsRenderer(BiConsumer<PoseStack, MultiBufferSource> consumer) {
        this.doorsRenderer = consumer;
    }

    public void setBotiRenderer(BiConsumer<PoseStack, MultiBufferSource> consumer) {
        this.botiRenderer = consumer;
    }

    public void setBotiTransformer(Consumer<PoseStack> consumer) {
        this.botiTransformer = consumer;
    }

    public void renderDoors(PoseStack poseStack, MultiBufferSource bufferSource) {
        if (this.doorsRenderer != null) {
            this.doorsRenderer.accept(poseStack, bufferSource);
        }
    }

    public void renderBoti(PoseStack poseStack, MultiBufferSource bufferSource) {
        if (this.botiRenderer != null) {
            this.botiRenderer.accept(poseStack, bufferSource);
        }
    }

    public void transformBoti(PoseStack poseStack) {
        if (this.botiTransformer != null) {
            this.botiTransformer.accept(poseStack);
        }
    }
}
