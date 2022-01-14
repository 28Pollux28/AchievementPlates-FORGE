package me.pollux28.achievementplates.mixin;

import me.pollux28.achievementplates.predicate.CustomNBTPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemPredicate.Builder.class)
public class ItemPredicateBuilderMixin {
    @Shadow
    private NbtPredicate nbt;

    @Inject(method = "Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;hasNbt(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;", at= @At("TAIL"))
    private void injectBuilderNBT(CompoundTag tag, CallbackInfoReturnable<ItemPredicate.Builder> cir){
        this.nbt = new CustomNBTPredicate(tag);
    }
}
