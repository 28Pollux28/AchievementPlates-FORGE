package me.pollux28.achievementplates.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.*;
import java.util.function.Predicate;

public final class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklist;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> whitelist;
    public static final ForgeConfigSpec.ConfigValue<Boolean> useBlacklist;
    public static final ForgeConfigSpec.ConfigValue<Boolean> useWhitelist;
    public static final ForgeConfigSpec.ConfigValue<Boolean> useClaimMessages;
    public static final ForgeConfigSpec.ConfigValue<Integer> tickDelayBetweenMessages;
    static{
        BUILDER.push("Config for Achievement Plates");
        BUILDER.pop();
        Predicate<Object> entryValidator = o -> {
            if(o instanceof String s){
                if(s.contains("=")){
                    String[] values = s.split("=");
                    if(values.length==2){
                        values[0] = values[0].toLowerCase(Locale.ROOT);
                        values[1] = values[1].toLowerCase(Locale.ROOT);
                        if(values[0].equalsIgnoreCase("namespace") || values[0].equalsIgnoreCase("identifier")){
                            return true;
                        }else if(values[0].equalsIgnoreCase("type")){
                            return values[1].equalsIgnoreCase("challenge") || values[1].equalsIgnoreCase("task") || values[1].equalsIgnoreCase("goal");
                        }
                    }
                }
            }
            return false;
        };
        blacklist = BUILDER.comment("Blacklist advancement by namespace, id or type","To use it, specify it in the form \"key=value\"","keys available : namespace, identifier, type","Values for the \"type\" key : task, challenge, goal").defineList("BlackList", List.of("namespace=examplenamespace", "identifier=modid:advancement_id", "type=task","type=goal"),entryValidator);
        whitelist = BUILDER.comment("WhiteList advancement by namespace, id or type","To use it, specify it in the form \"key=value\"","keys available : namespace, identifier, type","Values for the \"type\" key : task, challenge, goal").defineList("WhiteList", List.of("namespace=examplenamespace", "identifier=modid:advancement_id", "type=task","type=goal"),entryValidator);
        useBlacklist = BUILDER.comment("True to use the blacklist").define("useBlacklist", false);
        useWhitelist = BUILDER.comment("True to use the whitelist").define("useWhitelist", false);
        useClaimMessages = BUILDER.comment("Set to true to activate chat messages to claim trophies. If set to false, trophies will be given immediately upon advancement completion.").define("useClaimMessages", true);
        tickDelayBetweenMessages = BUILDER.comment("Number of ticks between two claim messages").define("tickDelayBetweenMessages", 3600);
        SPEC = BUILDER.build();
    }
}
