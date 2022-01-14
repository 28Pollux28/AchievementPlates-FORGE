package me.pollux28.achievementplates.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Map;

public class WallPlateBlock extends AbstractPlateBlock{
    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(3.0,3,15.0,13.0,13,16.0), Direction.SOUTH, Block.box(3.0,3,0.0,13.0,13,1.0), Direction.EAST, Block.box(0.0,3,3.0,1.0,13,13.0), Direction.WEST, Block.box(15.0,3,3.0,16.0,13,13.0)));

    public WallPlateBlock(Properties p_49795_) {
        super(p_49795_);
    }
    public boolean canSurvive(BlockState p_58073_, LevelReader p_58074_, BlockPos p_58075_) {
        return p_58074_.getBlockState(p_58075_.relative(p_58073_.getValue(FACING).getOpposite())).getMaterial().isSolid();
    }
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_58071_) {
        BlockState blockstate = this.defaultBlockState();
        FluidState fluidstate = p_58071_.getLevel().getFluidState(p_58071_.getClickedPos());
        LevelReader levelreader = p_58071_.getLevel();
        BlockPos blockpos = p_58071_.getClickedPos();
        Direction[] adirection = p_58071_.getNearestLookingDirections();

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction1 = direction.getOpposite();
                return blockstate.setValue(FACING, direction1);
            }
        }

        return null;
    }

    public BlockState updateShape(BlockState p_58083_, Direction p_58084_, BlockState p_58085_, LevelAccessor p_58086_, BlockPos p_58087_, BlockPos p_58088_) {
        return p_58084_.getOpposite() == p_58083_.getValue(FACING) && !p_58083_.canSurvive(p_58086_, p_58087_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_58083_, p_58084_, p_58085_, p_58086_, p_58087_, p_58088_);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        switch (state.getValue(FACING)){
            case NORTH ->{
                return Shapes.box(3.0/16D,3/16D,15/16D,13.0/16D,13/16D,16.0/16D);
            }
            case SOUTH -> {
                VoxelShape top = Shapes.box(3.0/16D,3/16D,0.0/16D,13.0/16D,13/16D,1.0/16D);
                return top;
            }
            case WEST -> {
                return Shapes.box(15.0/16D,3.0/16D,3.0/16D,16.0/16D,13.0/16D,13.0/16D);
            }
            case EAST -> {
                return Shapes.box(0.0/16D,3/16D,3.0/16D,1/16D,13/16D,13.0/16D);
            }
            default -> {
                return Shapes.block();
            }
        }
    }
}
