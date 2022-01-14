package me.pollux28.achievementplates.mixin;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.pollux28.achievementplates.predicate.CustomNBTPredicate;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Mixin(value = ItemPredicate.class, priority = 800)
public class ItemPredicateMixin {
    @Inject(method = "fromJson(Lcom/google/gson/JsonElement;)Lnet/minecraft/advancements/critereon/ItemPredicate;",
            at = @At("RETURN"),
            slice = @Slice(
                    from = @At(value ="INVOKE" , target ="Lnet/minecraft/advancements/critereon/EnchantmentPredicate;fromJsonArray(Lcom/google/gson/JsonElement;)[Lnet/minecraft/advancements/critereon/EnchantmentPredicate;" ),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/ItemPredicate;<init>(Lnet/minecraft/tags/Tag;Ljava/util/Set;Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;[Lnet/minecraft/advancements/critereon/EnchantmentPredicate;[Lnet/minecraft/advancements/critereon/EnchantmentPredicate;Lnet/minecraft/world/item/alchemy/Potion;Lnet/minecraft/advancements/critereon/NbtPredicate;)V",shift = At.Shift.AFTER)
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void injectFromJSON(JsonElement rl, CallbackInfoReturnable<ItemPredicate> cir, JsonObject jsonObject, MinMaxBounds.Ints intRange, MinMaxBounds.Ints intRange2, NbtPredicate nbtPredicate, Set set, JsonArray var6, Tag tag, Potion potion, EnchantmentPredicate enchantmentPredicates[], EnchantmentPredicate enchantmentPredicates2[]){
        if(cir.getReturnValue()==ItemPredicate.ANY){
            cir.setReturnValue(ItemPredicate.ANY);
        }else{
            CustomNBTPredicate nbtPredicat = (CustomNBTPredicate) CustomNBTPredicate.fromJson(jsonObject.get("nbt"));
            cir.setReturnValue(new ItemPredicate(tag,set,intRange,intRange2,enchantmentPredicates, enchantmentPredicates2,potion,nbtPredicat));
        }
    }
}
