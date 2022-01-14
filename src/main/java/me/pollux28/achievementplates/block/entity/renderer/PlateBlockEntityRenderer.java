package me.pollux28.achievementplates.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import me.pollux28.achievementplates.block.PlateBlock;
import me.pollux28.achievementplates.block.entity.PlateBlockEntity;
import me.pollux28.achievementplates.client.render.PlateRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PlateBlockEntityRenderer implements BlockEntityRenderer<PlateBlockEntity> {
    public static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("textures/gui/advancements/widgets.png");

    public PlateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx){
        super();
    }

    @Override
    public void render(PlateBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        BlockState blockState = entity.getBlockState();
        Direction direction = blockState.getValue(PlateBlock.FACING);
        ItemStack itemStack = entity.getDisplay().getIcon();
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        if(blockState.getBlock() instanceof PlateBlock){
            this.renderSides(entity, matrix4f, multiBufferSource.getBuffer(this.getLayer()),light);
            poseStack.popPose();

            poseStack.pushPose();
            float g = direction.toYRot();
            int id = direction.get3DDataValue();
            switch (id) {
                case 2 -> poseStack.translate(0.50D, 0.34375D, 0.53125D);
                case 3 -> poseStack.translate(0.50D, 0.34375D, 0.46875D);
                case 4 -> poseStack.translate(0.53125D, 0.34375D, 0.5D);
                case 5 -> poseStack.translate(0.46875D, 0.34375D, 0.50D);
                default -> poseStack.translate(0.50D,0.34375D, 0.53125D);
            }

            poseStack.scale(0.25F, 0.25F, 0.25F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180 -g));
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, light, overlay, poseStack, multiBufferSource, 54564451);
            poseStack.popPose();
        }else{
            this.renderSidesWall(entity, matrix4f, multiBufferSource.getBuffer(this.getLayer()),light);
            poseStack.popPose();
            poseStack.pushPose();
            float g = direction.toYRot();
            int id = direction.get3DDataValue();
            switch (id) {
                case 2 -> poseStack.translate(0.50D, 0.5D, 0.96875D); //North
                case 3 -> poseStack.translate(0.50D, 0.5D, 0.03125D); //South
                case 4 -> poseStack.translate(0.96875D, 0.5D, 0.5D); //West
                case 5 -> poseStack.translate(0.03125D, 0.5D, 0.50D); //East
                default -> poseStack.translate(0.50D,0.5D, 0.96875D);
            }

            poseStack.scale(0.25F, 0.25F, 0.25F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180 -g));
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, light, overlay, poseStack, multiBufferSource, 54564451);
            poseStack.popPose();
        }
    }

    private void renderSides(PlateBlockEntity entity, Matrix4f matrix4f, VertexConsumer buffer,int light) {
        float zSouthOffset = entity.getBlockState().getValue(PlateBlock.FACING).equals(Direction.NORTH) ? 0.0625F:0;
        float xWestOffset = entity.getBlockState().getValue(PlateBlock.FACING).equals(Direction.WEST) ? 0.0625F:0;
        float u1 =entity.getDisplay().getFrame().getTexture()/256F;
        float u2 =u1 +0.1015625F;
        float v1 =0.5F;
        float v2 =v1+0.1015625F;
        this.renderSide(entity, matrix4f, buffer, 0.3F, 0.7F, 0.125F+0.01875F, 0.5625F-0.01875F, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, u1,u2,v1,v2,Direction.SOUTH,light);
        this.renderSide(entity, matrix4f, buffer, 0.3F, 0.7F, 0.5625F-0.01875F, 0.125F+0.01875F, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset, 0.46875F+zSouthOffset,u2,u1,v2,v1, Direction.NORTH, light);
        this.renderSide(entity, matrix4f, buffer, 0.46875F+xWestOffset, 0.46875F+xWestOffset, 0.5625F-0.01875F, 0.125F+0.01875F, 0.3F, 0.7F, 0.7F, 0.3F,u2,u1,v2,v1, Direction.EAST, light);
        this.renderSide(entity, matrix4f, buffer, 0.46875F+xWestOffset, 0.46875F+xWestOffset, 0.125F+0.01875F, 0.5625F-0.01875F, 0.3F, 0.7F, 0.7F, 0.3F, u1,u2,v1,v2,Direction.WEST, light);

    }

    private void renderSide(PlateBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4,float u1,float u2, float v1, float v2, Direction side,int light) {
        if (entity.shouldRenderFrame(side)) {
            vertices.vertex(model, x1, y1, z1).color(1.0F,1.0F,1.0F,1.0F).uv(u1,v2).uv2(light-25).endVertex();
            vertices.vertex(model, x2, y1, z2).color(1.0F,1.0F,1.0F,1.0F).uv(u2,v2).uv2(light-25).endVertex();
            vertices.vertex(model, x2, y2, z3).color(1.0F,1.0F,1.0F,1.0F).uv(u2,v1).uv2(light-25).endVertex();
            vertices.vertex(model, x1, y2, z4).color(1.0F,1.0F,1.0F,1.0F).uv(u1,v1).uv2(light-25).endVertex();
        }

    }

    private void renderSidesWall(PlateBlockEntity entity, Matrix4f matrix4f, VertexConsumer buffer, int light) {
        float zSouthOffset = entity.getBlockState().getValue(PlateBlock.FACING).equals(Direction.NORTH) ? 0.9375F:0;
        float xWestOffset = entity.getBlockState().getValue(PlateBlock.FACING).equals(Direction.WEST) ? 0.9375F:0;
        float u1 =entity.getDisplay().getFrame().getTexture()/256F;
        float u2 =u1 +0.1015625F;
        float v1 =0.5F;
        float v2 =v1+0.1015625F;
        this.renderSideWall(entity, matrix4f, buffer, 0.3F, 0.7F, 0.125F+0.01875F+0.15625F, 0.5625F-0.01875F+0.15625F, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, u1,u2,v1,v2,Direction.SOUTH,light);
        this.renderSideWall(entity, matrix4f, buffer, 0.3F, 0.7F, 0.5625F-0.01875F+0.15625F, 0.125F+0.01875F+0.15625F, 0.031255F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset, 0.03125F+zSouthOffset,u2,u1,v2,v1, Direction.NORTH, light);
        this.renderSideWall(entity, matrix4f, buffer, 0.03125F+xWestOffset, 0.03125F+xWestOffset, 0.5625F-0.01875F+0.15625F, 0.125F+0.01875F+0.15625F, 0.3F, 0.7F, 0.7F, 0.3F,u2,u1,v2,v1, Direction.EAST, light);
        this.renderSideWall(entity, matrix4f, buffer, 0.03125F+xWestOffset, 0.03125F+xWestOffset, 0.125F+0.01875F+0.15625F, 0.5625F-0.01875F+0.15625F, 0.3F, 0.7F, 0.7F, 0.3F, u1,u2,v1,v2,Direction.WEST, light);

    }

    private void renderSideWall(PlateBlockEntity entity, Matrix4f model, VertexConsumer vertices, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4,float u1,float u2, float v1, float v2, Direction side,int light) {
        if (entity.shouldRenderFrame(side)) {
            vertices.vertex(model, x1, y1, z1).color(1.0F,1.0F,1.0F,1.0F).uv(u1,v2).uv2(light-25).endVertex();
            vertices.vertex(model, x2, y1, z2).color(1.0F,1.0F,1.0F,1.0F).uv(u2,v2).uv2(light-25).endVertex();
            vertices.vertex(model, x2, y2, z3).color(1.0F,1.0F,1.0F,1.0F).uv(u2,v1).uv2(light-25).endVertex();
            vertices.vertex(model, x1, y2, z4).color(1.0F,1.0F,1.0F,1.0F).uv(u1,v1).uv2(light-25).endVertex();
        }

    }
    private RenderType getLayer(){
        return PlateRenderType.createPlateLayer();
    }

}
