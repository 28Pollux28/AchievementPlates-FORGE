package me.pollux28.achievementplates;

import com.mojang.brigadier.CommandDispatcher;
import me.pollux28.achievementplates.advancement.PlayerTrophyManager;
import me.pollux28.achievementplates.block.entity.renderer.PlateBlockEntityRenderer;
import me.pollux28.achievementplates.command.ModCommands;
import me.pollux28.achievementplates.config.Config;
import me.pollux28.achievementplates.setup.ModTileEntityTypes;
import me.pollux28.achievementplates.setup.Registration;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AchievementPlates.MOD_ID)
public class AchievementPlates {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "achievementplates";
    public static HashMap<Player, PlayerTrophyManager> trophyManagers = new HashMap<>();


    public AchievementPlates() {
        Registration.register();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBERenderer);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
//        MinecraftForge.EVENT_BUS.addListener(this::onAdvancement);
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommandEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC,"achievementplate-common.toml");
    }

    private void registerBERenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModTileEntityTypes.PLATE_BLOCK_ENTITY.get(), PlateBlockEntityRenderer::new);
    }


    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
//        LOGGER.info("HELLO FROM PREINIT");
//        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
//        InterModComms.sendTo("achievementplates", "helloworld", () -> {
//            LOGGER.info("Hello world from the MDK");
//            return "Hello world";
//        });
    }
//    private void onAdvancement(final AdvancementEvent event){
//        Player player1 = event.getPlayer();
//        ServerPlayer player = player1.getServer().getPlayerList().getPlayer(player1.getUUID());
//        player.displayClientMessage(new TextComponent("Yo!"),false);
//        Advancement advancement = event.getAdvancement();
//        PlayerTrophyManager playerTrophyManager =PlayerTrophyManager.create(player);
//        boolean shouldGive = Utils.shouldGiveAdvancement(advancement, player);
//        if(shouldGive) playerTrophyManager.addTrophy(advancement);
//    }
    private void onTick(TickEvent.WorldTickEvent event) {
        Level level =event.world;
        if(event.phase != Phase.END || level.dimension()!=Level.OVERWORLD) return;
        MinecraftServer server = level.getServer();
        if(!Config.useClaimMessages.get() || event.world.getGameTime()%Config.tickDelayBetweenMessages.get()==0) {
            for (var entryIterator = trophyManagers.entrySet().iterator(); entryIterator.hasNext(); ) {
                var entry = entryIterator.next();
                if (server.getPlayerList().getPlayer(entry.getKey().getUUID()).hasDisconnected()) {
                    entryIterator.remove();
                } else {
                    entry.getValue().tick(server);
                }
            }
        }
    }
    private void registerCommandEvent(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        ModCommands.register(commandDispatcher);
    }
    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m -> m.messageSupplier().get()).
//                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
//        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
//            LOGGER.info("HELLO from Register Block");
        }
    }
}
