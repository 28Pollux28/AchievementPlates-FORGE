package me.pollux28.achievementplates.data.loot;

import me.pollux28.achievementplates.setup.ModBlocks;
import me.pollux28.achievementplates.setup.Registration;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLoot {
    @Override
    protected void addTables() {
        Registration.BLOCKS.getEntries().forEach(block -> {
            if(block.get()== ModBlocks.WALL_PLATE_BLOCK.get()){
//                dropSelf(ModBlocks.PLATE_BLOCK.get());
                this.add(ModBlocks.WALL_PLATE_BLOCK.get(),block1 -> {
                    return LootTable.lootTable().withPool(applyExplosionCondition(ModBlocks.PLATE_BLOCK.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModBlocks.PLATE_BLOCK.get()).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("display","BlockEntityTag.display", CopyNbtFunction.MergeStrategy.REPLACE).copy("player_name","BlockEntityTag.player_name", CopyNbtFunction.MergeStrategy.REPLACE).copy("CustomName","display.Name", CopyNbtFunction.MergeStrategy.REPLACE)))));

                });
            }else{
                this.add(ModBlocks.PLATE_BLOCK.get(),block1 -> {
                    return LootTable.lootTable().withPool(applyExplosionCondition(ModBlocks.PLATE_BLOCK.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModBlocks.PLATE_BLOCK.get()).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("display", "BlockEntityTag.display", CopyNbtFunction.MergeStrategy.REPLACE).copy("player_name", "BlockEntityTag.player_name", CopyNbtFunction.MergeStrategy.REPLACE).copy("CustomName","display.Name", CopyNbtFunction.MergeStrategy.REPLACE)))));
                });
            }
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .collect(Collectors.toList());
    }
}
