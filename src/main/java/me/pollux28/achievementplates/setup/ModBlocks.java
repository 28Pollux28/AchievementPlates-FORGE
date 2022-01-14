package me.pollux28.achievementplates.setup;

import me.pollux28.achievementplates.block.PlateBlock;
import me.pollux28.achievementplates.block.WallPlateBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final RegistryObject<PlateBlock> PLATE_BLOCK = registerNoItem("achievement_plate", ()-> new PlateBlock( Block.Properties
            .of(Material.METAL)
            .strength(1f,0.5f)
            .sound(SoundType.GILDED_BLACKSTONE)));
    public static final RegistryObject<WallPlateBlock> WALL_PLATE_BLOCK = registerNoItem("achievement_wall_plate", ()-> new WallPlateBlock(Block.Properties
            .of(Material.METAL)
            .strength(1f,0.5f)
            .sound(SoundType.GILDED_BLACKSTONE)));
    public static final RegistryObject<Item> PLATE_ITEM = Registration.ITEMS.register("achievement_plate",() -> new StandingAndWallBlockItem(PLATE_BLOCK.get(),WALL_PLATE_BLOCK.get(),new Item.Properties()));



    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block){
        return Registration.BLOCKS.register(name,block);
    }
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block){
        RegistryObject<T> ret = registerNoItem(name,block);
//        Registration.ITEMS.register(name,() -> new StandingAndWallBlockItem(ret.get(),new Item.Properties()));

        return ret;
    }

    static void register(){}
}
