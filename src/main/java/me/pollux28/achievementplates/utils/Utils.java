package me.pollux28.achievementplates.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.pollux28.achievementplates.AchievementPlates;
import me.pollux28.achievementplates.advancement.PlayerTrophyManager;
import me.pollux28.achievementplates.config.Config;
import me.pollux28.achievementplates.setup.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelResource;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Utils {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @NotNull
    public static ItemStack getPlateItemStack(TextComponent playerName, DisplayInfo display) {
        CompoundTag tag1 = new CompoundTag();
        CompoundTag tag2 = new CompoundTag();
        writeDisplayToNbt(tag2, display);
        tag2.putString("player_name", playerName.getContents());
        tag1.put("BlockEntityTag", tag2);
        ItemStack itemStack = ModBlocks.PLATE_ITEM.get().getDefaultInstance();
        itemStack.setTag(tag1);
        itemStack.setHoverName(playerName.copy().setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)).append(new TextComponent("'s ").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))).append(display.getTitle().copy().setStyle(Style.EMPTY.withColor(display.getFrame().getChatColor()))).append(new TextComponent(" trophy plate").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))));
        return itemStack;
    }

    public static void writeDisplayToNbt(CompoundTag nbt, DisplayInfo display) {
        CompoundTag nbtCompound = new CompoundTag();
        if (display.getTitle() instanceof TranslatableComponent title) {
            nbtCompound.putString("display_title", title.getKey());
        } else {
            nbtCompound.putString("display_title", display.getTitle().getContents());
        }
        if (display.getDescription() instanceof TranslatableComponent desc) {
            nbtCompound.putString("display_desc", desc.getKey());
        } else {
            nbtCompound.putString("display_desc", display.getDescription().getContents());
        }

        if (display.getBackground() != null) {
            nbtCompound.putString("display_background", display.getBackground().toString());
        }
        nbtCompound.put("display_icon", display.getIcon().save(new CompoundTag()));
        nbtCompound.putString("display_frame_id", display.getFrame().getName());
        nbtCompound.putBoolean("display_toast", display.shouldShowToast());
        nbtCompound.putBoolean("display_chat", display.shouldAnnounceChat());
        nbtCompound.putBoolean("display_hidden", display.isHidden());
        nbt.put("display", nbtCompound);
    }

    public static boolean giveItemToPlayer(Player playerEntity, ItemStack itemStack) {
        if (playerEntity.addItem(itemStack)) {
            playerEntity.level.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            return true;
        } else {
            return false;
        }
    }

    private static Path checkPlayerFile(Player playerEntity) {
        UUID uuid = playerEntity.getUUID();
        MinecraftServer server = playerEntity.getServer();
        if (server == null) {
            throw new IllegalStateException("I don't know how you got there...");
        }
        Path path = server.getWorldPath(LevelResource.ROOT);
        Path path2 = Paths.get(path.toString(), "achievement_plates");
        if (!Files.isDirectory(path2)) {
            path2.toFile().mkdirs();
        }
        Path playerFile = Paths.get(path2.toString(), uuid.toString().toLowerCase(Locale.ROOT) + ".json");
        if (!Files.exists(playerFile)) {
            try {
                Files.createFile(playerFile);
                BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile.toFile()));
                writer.write(gson.toJson(new JsonObject()));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return playerFile;
    }

    public static boolean checkJson(Player playerEntity, Map<Advancement, Boolean> shouldGive) {
        Path playerFile = checkPlayerFile(playerEntity);
        try {
            JsonObject obj = new JsonParser().parse(new FileReader(playerFile.toFile())).getAsJsonObject();
            shouldGive.forEach((advancement, aBoolean) -> {
                if (aBoolean) {
                    String advancementID = advancement.getId().toDebugFileName();
                    if (obj.has(advancementID)) {
                        if (!obj.getAsJsonPrimitive(advancementID).getAsBoolean()) {
//                            obj.addProperty(advancementID, true);
                            shouldGive.replace(advancement, true);
                        }
                        shouldGive.replace(advancement, false);

                    } else {
//                        obj.add(advancementID, new JsonPrimitive(true));
                        shouldGive.replace(advancement, true);
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void writeToJson(Player playerEntity, JsonObject JsonToAdd) {
        try{
            Path playerFile = checkPlayerFile(playerEntity);
            JsonObject obj = new JsonParser().parse(new FileReader(playerFile.toFile())).getAsJsonObject();
            JsonToAdd.entrySet().forEach(stringJsonElementEntry -> obj.add(stringJsonElementEntry.getKey(),stringJsonElementEntry.getValue()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile.toFile()));
            writer.write(gson.toJson(obj));
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void removeAdvancement(ServerPlayer playerEntity, Set<ResourceLocation> set) {
        PlayerTrophyManager playerTrophyManager = PlayerTrophyManager.create(playerEntity);
        for (ResourceLocation id : set){
            playerTrophyManager.removeTrophy(id);
        }
        UUID uuid = playerEntity.getUUID();
        Path playerFile = checkPlayerFile(playerEntity);
        if (playerFile == null) {
            AchievementPlates.LOGGER.error("Couldn't create file for {} with UUID {}", playerEntity.getName(), uuid);
        }
        try {
            JsonObject obj = new JsonParser().parse(new FileReader(playerFile.toFile())).getAsJsonObject();
            for (ResourceLocation id : set) {
                obj.remove(id.toDebugFileName());
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile.toFile()));
            writer.write(gson.toJson(obj));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shouldGiveAdvancement(Map<Advancement, AdvancementProgress> advancementToProgress, ServerPlayer playerEntity, Map<Advancement, Boolean> shouldGive) {
        for (Map.Entry<Advancement, AdvancementProgress> advancementAdvancementProgressEntry : advancementToProgress.entrySet()) {
            if (advancementAdvancementProgressEntry.getValue().isDone()) {
                Advancement advancement = advancementAdvancementProgressEntry.getKey();
                DisplayInfo display = advancement.getDisplay();
                if (display != null && display.shouldShowToast()) {
                    boolean flag = true;
                    if (Config.useWhitelist.get()) {
                        List<? extends String> whiteList = Config.whitelist.get();
                        boolean namespace = false;
                        boolean id = false;
                        boolean type = false;
                        if (whiteList.contains("namespace=" + advancement.getId().getNamespace())) {
                            namespace = true;
                        }
                        if (whiteList.contains("identifier=" + advancement.getId().toDebugFileName())) {
                            id = true;
                        }
                        if (whiteList.contains("type=" + display.getFrame().getName())) {
                            type = true;
                        }
                        flag = flag && (namespace || id || type);
                    }
                    if (Config.useBlacklist.get() && flag) {
                        List<? extends String> blacklist = Config.blacklist.get();
                        boolean namespace = true;
                        boolean id = true;
                        boolean type = true;
                        if (blacklist.contains("namespace=" + advancement.getId().getNamespace())) {
                            namespace = false;
                        }
                        if (blacklist.contains("identifier=" + advancement.getId().toDebugFileName())) {
                            id = false;
                        }
                        if (blacklist.contains("type=" + display.getFrame().getName())) {
                            type = false;
                        }
                        flag = flag && (namespace && id && type);
                    }
                    PlayerTrophyManager playerTrophyManager = PlayerTrophyManager.create(playerEntity);
                    flag = flag && !playerTrophyManager.containsTrophy(advancement.getId());
                    shouldGive.put(advancement, flag);
                }
            }
            checkJson(playerEntity, shouldGive);
        }

    }


}
