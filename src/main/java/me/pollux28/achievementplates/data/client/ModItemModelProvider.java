package me.pollux28.achievementplates.data.client;

import me.pollux28.achievementplates.AchievementPlates;
import me.pollux28.achievementplates.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AchievementPlates.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Registration.BLOCKS.getEntries().forEach(block -> blockBuilder(block.get()));

    }

    private void blockBuilder(Block block) {
        ResourceLocation resourceLocation = block.getRegistryName();
        if(resourceLocation!= null){
            String path = resourceLocation.getPath();
            withExistingParent(path,modLoc("block/"+ path));
        }
    }


    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/"+name);
    }
}
