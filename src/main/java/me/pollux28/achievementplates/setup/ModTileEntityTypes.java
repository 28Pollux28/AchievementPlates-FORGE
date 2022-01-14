package me.pollux28.achievementplates.setup;

import me.pollux28.achievementplates.block.entity.PlateBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ModTileEntityTypes {
    static void register(){}

    public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE_BLOCK_ENTITY = Registration.TILE_ENTITIES.register("plate_block_entity", ()-> BlockEntityType.Builder.of(PlateBlockEntity::new,ModBlocks.PLATE_BLOCK.get(),ModBlocks.WALL_PLATE_BLOCK.get()).build(null));

    private static <T extends BlockEntity, S extends Block> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<S>... blocks) {
        Block[] blocks1 = (Block[]) Arrays.stream(blocks).map(registryObject -> (S)registryObject.get()).toArray();
        return Registration.TILE_ENTITIES.register(name,()-> BlockEntityType.Builder.of(factory, blocks1).build(null));
    }


}
