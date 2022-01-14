package me.pollux28.achievementplates.command;

import java.util.Collection;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.pollux28.achievementplates.AchievementPlates;
import me.pollux28.achievementplates.advancement.PlayerTrophyManager;

public class ModCommands
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){

            LiteralArgumentBuilder<CommandSourceStack> pre = Commands.literal("achievement_plates")
                    .requires(executor -> executor.hasPermission(0));
            pre.then(Commands.literal("debug").requires(serverCommandSource -> serverCommandSource.hasPermission(4)).executes(commandContext -> {
                return execute((CommandSourceStack) commandContext.getSource());
            }));
            pre.then(Commands.literal("claim").executes(commandContext ->{
                return executeC(commandContext.getSource(), commandContext.getSource().getPlayerOrException());
            }));
            dispatcher.register(pre);


    }

    private static int executeC(CommandSourceStack source, ServerPlayer player) {
        PlayerTrophyManager playerTrophyManager = AchievementPlates.trophyManagers.get(player);
        if(playerTrophyManager !=null){
            playerTrophyManager.giveTrophies(source);
            return 1;
        }else{
            source.sendSuccess(new TranslatableComponent("achievement_plates.chat.claim_message_no_trophy").withStyle(style -> style.withColor(ChatFormatting.RED)),false);
            return 1;
        }
    }

    private static int execute(CommandSourceStack commandSource){
        try {
            commandSource.getServer().getPlayerList().getPlayerAdvancements(commandSource.getPlayerOrException());
            Collection<Advancement> advancements = Minecraft.getInstance().getConnection().getAdvancements().getAdvancements().getAllAdvancements();
            advancements.forEach(advancement -> {
                if(advancement.getDisplay() !=null && advancement.getDisplay().shouldShowToast()){
                    commandSource.sendSuccess(new TextComponent(advancement.getId().toString()), false);
                }
            });
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }


        return 1;
    }
}
