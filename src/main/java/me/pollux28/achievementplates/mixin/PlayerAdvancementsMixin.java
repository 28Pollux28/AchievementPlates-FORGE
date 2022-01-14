package me.pollux28.achievementplates.mixin;

import com.google.common.collect.Maps;
import me.pollux28.achievementplates.advancement.PlayerTrophyManager;
import me.pollux28.achievementplates.utils.Utils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.Set;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Shadow
    private ServerPlayer player;
    @Shadow
    private boolean isFirstPacket;
    @Final
    @Shadow
    private Set<Advancement> visible;
    @Final
    @Shadow
    private Map<Advancement, AdvancementProgress> advancements;


    @Inject(method = "flushDirty",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injectAdvancementLoader(ServerPlayer player, CallbackInfo ci, Map<ResourceLocation, AdvancementProgress> map, Set<Advancement> set, Set<ResourceLocation> set2) {
        Map<Advancement, Boolean> shouldGive = Maps.newHashMap();
        Utils.shouldGiveAdvancement(advancements, player, shouldGive);
        PlayerTrophyManager playerTrophyManager =PlayerTrophyManager.create(player);
        shouldGive.keySet().stream().filter(shouldGive::get).forEach(playerTrophyManager::addTrophy);
        Utils.removeAdvancement(player, set2);
    }
}
