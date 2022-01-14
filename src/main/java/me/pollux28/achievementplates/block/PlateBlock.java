package me.pollux28.achievementplates.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class PlateBlock extends AbstractPlateBlock{
    public PlateBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).getMaterial().isSolid();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        switch (state.getValue(FACING)){
            case NORTH ->{
                VoxelShape bottom =Shapes.box(3.0/16D, 0/16D,7.0/16D,13.0/16D,1.0/16D,10.0/16D);
                VoxelShape top = Shapes.box(3.0/16D,1/16D,8.0/16D,13.0/16D,10/16D,9.0/16D);
                return Shapes.join(bottom,top, BooleanOp.OR);
            }
            case SOUTH -> {
                VoxelShape bottom =Shapes.box(3.0/16D, 0,6.0/16D,13.0/16D,1.0/16D,9.0/16D);
                VoxelShape top = Shapes.box(3.0/16D,1/16D,7.0/16D,13.0/16D,10/16D,8.0/16D);
                return Shapes.join(bottom,top, BooleanOp.OR);
            }
            case WEST -> {
                VoxelShape bottom =Shapes.box(7.0/16D, 0/16D,3.0/16D,10.0/16D,1.0/16D,13.0/16D);
                VoxelShape top = Shapes.box(8.0/16D,1/16D,3.0/16D,9.0/16D,10/16D,13.0/16D);
                return Shapes.join(bottom,top, BooleanOp.OR);
            }
            case EAST -> {
                VoxelShape bottom = Shapes.box(6.0/16D, 0/16D,3.0/16D,9.0/16D,1.0/16D,13.0/16D);
                VoxelShape top = Shapes.box(7.0/16D,1/16D,3.0/16D,8.0/16D,10/16D,13.0/16D);
                return Shapes.join(bottom,top, BooleanOp.OR);
            }
            default -> {
                return Shapes.block();
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return direction==Direction.DOWN && !this.canSurvive(state,level,pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state,direction,neighborState,level,pos,neighborPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING,ctx.getHorizontalDirection().getOpposite());
    }


}
