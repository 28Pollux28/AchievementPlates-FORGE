package me.pollux28.achievementplates.data.client;

import me.pollux28.achievementplates.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnUsModLanguageProvider extends LanguageProvider {
    public EnUsModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.PLATE_BLOCK.get(),"You're not supposed to get this !! If not on purpose open an issue");
        add("advancement.cheater.title","You're A Cheater !");
        add("advancement.cheater.desc","Caught red-handed !");
        add("achievement_plates.chat.claim_message", "You have %s trophies to %s !");
        add("achievement_plates.chat.claim_message_action","claim");
        add("achievement_plates.chat.claim_message_tooltip","Click to obtain your trophies");
        add("achievement_plates.chat.claim_message_no_trophy","You don't have any trophy to claim !");
        add("achievement_plates.chat.claim_message_not_enough_space","There was not enough space in your inventory. %s");
        add("achievement_plates.chat.claim_message_given","You recieved %s trophies");
    }
}
