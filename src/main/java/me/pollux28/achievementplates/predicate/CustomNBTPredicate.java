package me.pollux28.achievementplates.predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

public class CustomNBTPredicate extends NbtPredicate {

    public static CustomNBTPredicate ANY = new CustomNBTPredicate((CompoundTag)null);
    private final CompoundTag nbt;

    public CustomNBTPredicate(@Nullable CompoundTag nbt) {
        super(nbt);
        this.nbt = nbt;
    }

    @Override
    public boolean matches(@Nullable Tag element) {
        if(this.nbt !=null && this.nbt.size()==0){
            return element == null;
        }
        if (element == null) {
            return this == ANY;
        } else {
            return this.nbt == null || NbtUtils.compareNbt(this.nbt, element, true);
        }
    }
    public static CustomNBTPredicate fromJson(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            CompoundTag nbtCompound2;
            try {
                nbtCompound2 = TagParser.parseTag(GsonHelper.convertToString(json, "nbt"));
            } catch (CommandSyntaxException var3) {
                throw new JsonSyntaxException("Invalid nbt tag: " + var3.getMessage());
            }

            return new CustomNBTPredicate(nbtCompound2);
        } else {
            return ANY;
        }
    }
}
