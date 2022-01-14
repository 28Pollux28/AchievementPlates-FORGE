package me.pollux28.achievementplates.block.entity;

import me.pollux28.achievementplates.block.PlateBlock;
import me.pollux28.achievementplates.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static me.pollux28.achievementplates.setup.ModTileEntityTypes.PLATE_BLOCK_ENTITY;

public class PlateBlockEntity extends BlockEntity {
    public static int PLATE_BLOCK_ENTITY_ID = 14587;
    private DisplayInfo display = new DisplayInfo(new ItemStack((Items.BARRIER.asItem())), new TranslatableComponent("advancement_plates.notitle"), new TranslatableComponent("advancement_plates.nodesc"), (ResourceLocation) null, FrameType.TASK, true, true, false);
    private String playerName = "";
    private TextComponent customName;

    public PlateBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState state, String playerName, Advancement advancement) {
        super(blockEntityType, blockPos, state);
        this.playerName = playerName;
        this.display = advancement.getDisplay();
    }

    public PlateBlockEntity(BlockPos pos, BlockState state) {
        super(PLATE_BLOCK_ENTITY.get(), pos, state);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public DisplayInfo getDisplay() {
        return display;
    }

    public void setDisplay(DisplayInfo display) {
        this.display = display;
        this.setChanged();
    }

    public TextComponent getCustomName() {
        return customName;
    }

    public void setCustomName(TextComponent customName) {
        this.customName = customName;
        this.setChanged();
    }

    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        TextComponent text = getCustomName();
        if (text != null) {
            player.displayClientMessage(new TextComponent("Advancement ").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)).append(ComponentUtils.mergeStyles((MutableComponent) display.getTitle(), Style.EMPTY.withColor(display.getFrame().getChatColor()))).append(new TextComponent(" was obtained by ")).append(new TextComponent(playerName).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA))), true);
            player.displayClientMessage(new TranslatableComponent("chat.type.advancement." + this.display.getFrame().getName(), new TextComponent(this.playerName).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), display.getTitle()), true);

        } else {
            player.displayClientMessage(new TextComponent("This trophy was not obtained !").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
        }
        return InteractionResult.CONSUME;
    }

    public boolean shouldRenderFrame(Direction dir) {
        return this.getBlockState().getValue(PlateBlock.FACING).getAxis().equals(dir.getAxis());
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        writeDisplayToNbt(nbt);
        nbt.putString("player_name", playerName);
        if (this.customName != null) {
            nbt.putString("CustomName", TextComponent.Serializer.toJson(this.customName));
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        writeDisplayToNbt(nbt);
        nbt.putString("player_name", playerName);
        if (this.customName != null) {
            nbt.putString("CustomName", TextComponent.Serializer.toJson(this.customName));
        }
        return nbt;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.display = readDisplayFromNBT(tag.getCompound("display"));
        this.playerName = tag.getString("player_name");
        if (tag.contains("CustomName", 8)) {
            this.customName = (TextComponent) TextComponent.Serializer.fromJson(tag.getString("CustomName"));
        }
        this.setChanged();
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private void writeDisplayToNbt(CompoundTag tag) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("display_title", ((TranslatableComponent) display.getTitle()).getKey());
        compoundTag.putString("display_desc", ((TranslatableComponent) display.getDescription()).getKey());
        if (display.getBackground() != null) {
            compoundTag.putString("display_background", display.getBackground().toString());
        }
        compoundTag.put("display_icon", display.getIcon().save(new CompoundTag()));
        compoundTag.putString("display_frame_id", display.getFrame().getName());
        compoundTag.putBoolean("display_toast", display.shouldShowToast());
        compoundTag.putBoolean("display_chat", display.shouldAnnounceChat());
        compoundTag.putBoolean("display_hidden", display.isHidden());
        tag.put("display", compoundTag);
    }

    private DisplayInfo readDisplayFromNBT(CompoundTag nbt) {
        TranslatableComponent title = new TranslatableComponent(nbt.getString("display_title"));
        TranslatableComponent desc = new TranslatableComponent(nbt.getString("display_desc"));
        ResourceLocation background = nbt.contains("display_background") ? new ResourceLocation(nbt.getString("display_background")) : null;
        ItemStack icon = ItemStack.of(nbt.getCompound("display_icon"));
        FrameType frame = FrameType.byName(nbt.getString("display_frame_id"));
        boolean displayToast = nbt.getBoolean("display_toast");
        boolean displayChat = nbt.getBoolean("display_chat");
        boolean displayHidden = nbt.getBoolean("display_hidden");
        return new DisplayInfo(icon, title, desc, background, frame, displayToast, displayChat, displayHidden);
    }

    public ItemStack getPickStack() {
        return Utils.getPlateItemStack(new TextComponent(playerName), this.display);
    }
}
