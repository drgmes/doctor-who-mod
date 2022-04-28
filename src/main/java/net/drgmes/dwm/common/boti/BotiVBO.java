package net.drgmes.dwm.common.boti;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;

public class BotiVBO {
    public VertexFormat format = DefaultVertexFormat.BLOCK;
    public Map<RenderType, BufferSource> sources = Maps.newHashMap();
    public Map<RenderType, VertexBuffer> vbos = Maps.newHashMap();

    public BotiVBO() {
        init();
    }

    public void init() {
        for (RenderType type : RenderType.chunkBufferLayers()) {
            BufferBuilder buffer = new BufferBuilder(format.getVertexSize());

            vbos.put(type, new VertexBuffer());
            sources.put(type, MultiBufferSource.immediate(buffer));
        }
    }

    public void begin(RenderType type) {
        this.getVBO(type).bind();
    }

    public VertexBuffer getVBO(RenderType type) {
        return this.vbos.getOrDefault(type, this.vbos.get(RenderType.solid()));
    }

    public BufferSource getBufferSource(RenderType type) {
        return this.sources.getOrDefault(type, this.sources.get(RenderType.solid()));
    }

    public BufferBuilder getBufferBuilder(RenderType type) {
        return (BufferBuilder) this.getBufferSource(type).getBuffer(type);
    }

    public void resetData(RenderType type) {
        this.getBufferBuilder(type).discard();
        this.getBufferBuilder(type).clear();
    }

    public void upload(RenderType type) {
        BufferBuilder bb = this.getBufferBuilder(type);

        if (bb.building()) {
            bb.end();
        }

        try {
            this.getVBO(type).upload(bb);
        } catch (Exception e) {
        }
    }

    public void unbind(RenderType type) {
        this.getBufferBuilder(type).clear();
        this.getBufferBuilder(type).discard();
        VertexBuffer.unbind();
    }

    public void draw(Matrix4f matrix) {
        for (RenderType type : this.vbos.keySet()) {
            this.begin(type);
            format.setupBufferState();
            type.setupRenderState();
            this.getVBO(type).draw();
            // this.getVBO(type).drawWithShader(matrix, matrix, GameRenderer.getBlockShader());
            type.clearRenderState();
            format.clearBufferState();
            VertexBuffer.unbind();
        }
    }
}