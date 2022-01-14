package me.pollux28.achievementplates.data;

import me.pollux28.achievementplates.AchievementPlates;
import me.pollux28.achievementplates.data.client.ModBlockStateProvider;
import me.pollux28.achievementplates.data.client.ModItemModelProvider;
import me.pollux28.achievementplates.data.client.EnUsModLanguageProvider;
import me.pollux28.achievementplates.data.loot.ModLootTableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = AchievementPlates.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators(){}
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        gen.addProvider(new ModBlockStateProvider(gen,existingFileHelper));
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));

        gen.addProvider(new ModLootTableProvider(gen));
        gen.addProvider(new EnUsModLanguageProvider(gen,AchievementPlates.MOD_ID));
    }

}
