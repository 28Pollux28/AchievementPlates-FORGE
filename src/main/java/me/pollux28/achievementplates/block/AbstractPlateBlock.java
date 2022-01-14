package me.pollux28.achievementplates.block;

import me.pollux28.achievementplates.block.entity.PlateBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public abstract class AbstractPlateBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AbstractPlateBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }


    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof PlateBlockEntity entity ? entity.getPickStack() : super.getCloneItemStack(state, target, level,pos,player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, placer, itemStack);
        CompoundTag tag = itemStack.getOrCreateTag();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {

            if (tag.contains("BlockEntityTag")) {
                CompoundTag compoundTag = tag.getCompound("BlockEntityTag");
                if (compoundTag.contains("display") && compoundTag.contains("player_name")) {
                    plateBlockEntity.load(compoundTag);
                    DisplayInfo display = plateBlockEntity.getDisplay();
                    if(!plateBlockEntity.getPlayerName().equalsIgnoreCase("")){
                        plateBlockEntity.setCustomName((TextComponent) new TextComponent(plateBlockEntity.getPlayerName()).copy().setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)).append(new TextComponent("'s ").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))).append(display.getTitle().copy().setStyle(Style.EMPTY.withColor(display.getFrame().getChatColor()))).append(new TextComponent(" trophy plate").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))));
                    }
                }
            }
            plateBlockEntity.setChanged();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }

    @Override
    public void destroy(LevelAccessor p_49860_, BlockPos p_49861_, BlockState p_49862_) {
        super.destroy(p_49860_, p_49861_, p_49862_);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(level.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PlateBlockEntity plateBlockEntity) {
            return plateBlockEntity.onUse(state, level, pos , player, hand, hit);
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos,state);
    }

}
