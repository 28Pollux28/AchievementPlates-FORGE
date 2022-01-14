package me.pollux28.achievementplates.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import me.pollux28.achievementplates.block.entity.renderer.PlateBlockEntityRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class PlateRenderType extends RenderType {
    public PlateRenderType(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);

    }
    public static RenderType createPlateLayer(){
        return create("plate", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false,false,CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_CUTOUT_MIPPED_SHADER).setTextureState(MultiTextureStateShard.builder().add(PlateBlockEntityRenderer.WIDGETS_TEXTURE, false, true).build()).setLightmapState(RenderStateShard.LIGHTMAP).createCompositeState(false));

    }
}
