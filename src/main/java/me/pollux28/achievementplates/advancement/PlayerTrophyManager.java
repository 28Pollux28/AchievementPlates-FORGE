package me.pollux28.achievementplates.advancement;

import com.google.gson.JsonObject;
import me.pollux28.achievementplates.AchievementPlates;
import me.pollux28.achievementplates.config.Config;
import me.pollux28.achievementplates.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.command.TextComponentHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerTrophyManager {
    ArrayList<Advancement> toGiveTrophies = new ArrayList<>();
    ServerPlayer player;
    private PlayerTrophyManager(ServerPlayer player){
        this.player = player;
        AchievementPlates.trophyManagers.put(player,this);
    }

    public static PlayerTrophyManager create(ServerPlayer player){
        PlayerTrophyManager playerTrophyManager = AchievementPlates.trophyManagers.get(player);
        if(playerTrophyManager == null){
            PlayerTrophyManager manager = new PlayerTrophyManager(player);
            AchievementPlates.trophyManagers.put(player,manager);
            return manager;
        }
        return playerTrophyManager;
    }
    public void tick(MinecraftServer server){
        if(Config.useClaimMessages.get()){
            if(toGiveTrophies.size()!=0) sendClaimMessage();
        }else{
            giveTrophies(null);
        }
    }
    public void addTrophy(Advancement advancement){
        toGiveTrophies.add(advancement);
    }
    public void removeTrophy(ResourceLocation id){
        toGiveTrophies.removeIf(advancement1 -> advancement1.getId()==id);
    }
    public boolean containsTrophy(ResourceLocation id){
        return toGiveTrophies.stream().anyMatch(advancement -> advancement.getId()==id);
    }

    private void sendClaimMessage(){
        TranslatableComponent text =getClaimText();
        player.displayClientMessage(new TranslatableComponent("achievement_plates.chat.claim_message",toGiveTrophies.size(), text).withStyle(style -> style.withColor(ChatFormatting.YELLOW)), false);
        player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f,1.0f);

    }

    public void giveTrophies(CommandSourceStack source) {
        int n = toGiveTrophies.size();
        Iterator<Advancement> iterator = toGiveTrophies.iterator();
        JsonObject obj = new JsonObject();
        while(iterator.hasNext()){
            Advancement advancement = iterator.next();
            DisplayInfo display = advancement.getDisplay();
            ItemStack itemStack = Utils.getPlateItemStack((TextComponent) player.getName().copy(), display);
            boolean flag = Utils.giveItemToPlayer(player, itemStack);
            if(!flag){
                break;
            }
            iterator.remove();
            obj.addProperty(advancement.getId().toDebugFileName(),true);

        }
        Utils.writeToJson(player,obj);
        if(source!=null) {
            int size = toGiveTrophies.size();
            TranslatableComponent text = getClaimText();
            TranslatableComponent text2 = (TranslatableComponent) new TranslatableComponent("achievement_plates.chat.claim_message", toGiveTrophies.size(), text).withStyle(style -> style.withColor(ChatFormatting.YELLOW));
            MutableComponent textF = new TranslatableComponent("achievement_plates.chat.claim_message_not_enough_space", text2).withStyle(style -> style.withColor(ChatFormatting.YELLOW));

            if (size == n && size != 0) {
                source.sendSuccess(textF, false);
            } else if (size != 0) {
                source.sendSuccess(new TranslatableComponent("achievement_plates.chat.claim_message_given", n - size).withStyle(style -> style.withColor(ChatFormatting.GREEN)), false);
                source.sendSuccess(textF, false);
            } else if (n == 0) {
                source.sendSuccess(new TranslatableComponent("achievement_plates.chat.claim_message_no_trophy").withStyle(style -> style.withColor(ChatFormatting.RED)), false);
            } else {
                source.sendSuccess(new TranslatableComponent("achievement_plates.chat.claim_message_given", n - size).withStyle(style -> style.withColor(ChatFormatting.GREEN)), false);
            }
        }
    }

    private TranslatableComponent getClaimText() {
        return (TranslatableComponent) ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("achievement_plates.chat.claim_message_action")).withStyle((style) -> {
            Style var10000 = style.withColor(ChatFormatting.GREEN);
            ClickEvent.Action var10003 = ClickEvent.Action.RUN_COMMAND;
            return var10000.withClickEvent(new ClickEvent(var10003, "/achievement_plates claim")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("achievement_plates.chat.claim_message_tooltip")));
        });
    }
}
